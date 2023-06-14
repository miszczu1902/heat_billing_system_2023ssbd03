package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.*;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Internationalization;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PlaceServiceImpl extends AbstractService implements PlaceService, SessionSynchronization {

    @Inject
    private Internationalization internationalization;

    @Inject
    private PlaceFacade placeFacade;

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private MessageSigner messageSigner;

    @Inject
    private OwnerFacade ownerFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private BalanceFacade balanceFacade;

    @Inject
    private HotWaterAdvanceFacade hotWaterAdvanceFacade;

    @Inject
    private HeatingPlaceAndCommunalAreaAdvanceFacade heatingPlaceAndCommunalAreaAdvanceFacade;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private BalanceService balanceService;

    @Override
    @RolesAllowed(Roles.MANAGER)
    public void modifyPlaceOwner(Long placeId, String username, String etag, Long version) {
        final Owner owner = ownerFacade.findOwnerByUsername(username);
        final String ManagerUsername = securityContext.getCallerPrincipal().getName();

        if (!owner.getAccount().getIsActive()) {
            throw AppException.createAccountIsNotActivatedException();
        }

        final Place place = placeFacade.findPlaceById(placeId);

        if (!etag.equals(messageSigner.sign(place))) {
            throw AppException.createVerifierException();
        }

        if (!Objects.equals(version, place.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }
        if (place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.userIsAlreadyOwnerOfThisPlaceException();
        }

        if (owner.getAccount().getUsername().equals(ManagerUsername)) {
            throw AppException.canNotMakeYourselfOwnerOfThePlaceException();
        }
        if (place.getOwner().getAccount().getUsername().equals(ManagerUsername)) {
            throw AppException.canNotMakeSomeoneOwnerOfYourPlaceException();
        }
        place.setOwner(owner);
        place.setPredictedHotWaterConsumption(new BigDecimal(0));
        placeFacade.edit(place);
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public void modifyPlace(String placeId, BigDecimal area, String etag, Long version,
                            String username, boolean isOwner, boolean isManager) {
        final Long id = Long.valueOf(placeId);
        Place place = placeFacade.findPlaceById(id);

        if (isManager && place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createManagerCouldNotEditOwnedPlaceException();
        }

        if (!etag.equals(messageSigner.sign(place))) {
            throw AppException.createVerifierException();
        }

        if (!Objects.equals(version, place.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        final List<Place> places = place.getBuilding().getPlaces().stream().filter(p -> !Objects.equals(p.getId(), id)).toList();

        BigDecimal areaPlaces = place.getBuilding().getCommunalAreaAggregate();

        final BigDecimal sumOfAreas = places.stream()
                .map(Place::getArea) // Assuming 'getArea' returns a BigDecimal representing the area
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        areaPlaces = areaPlaces.add(sumOfAreas).add(area);

        if (place.getBuilding().getTotalArea().compareTo(areaPlaces) != 0) {
            throw AppException.createTooBigPlaceAreaException();
        }

        if (area != null) {
            place.setArea(area);
        }

        placeFacade.edit(place);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void enterHotWaterConsumption(String placeId, LocalDate date, BigDecimal value) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void enterPredictedHotWaterConsumption(String placeId, BigDecimal consumption, String etag, Long version,
                                                  String username, boolean isOwner, boolean isManager) {
        final Long id = Long.valueOf(placeId);
        Place place = placeFacade.findPlaceById(id);

        if (!isManager && !place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createNotOwnerOfPlaceException();
        }

        if (isManager && place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createManagerCouldNotEditOwnedPlaceException();
        }

        if (!etag.equals(messageSigner.sign(place))) {
            throw AppException.createVerifierException();
        }

        if (!Objects.equals(version, place.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        if (place.getPredictedHotWaterConsumption().intValue() != 0.00 && isOwner && !isManager) {
            throw AppException.createPredictedHotWaterConsumptionValueAlreadySetException();
        }

        place.setPredictedHotWaterConsumption(consumption);
        placeFacade.edit(place);

        changeHotWaterAdvanceForNewPlace(place);
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    private void changeHotWaterAdvanceForNewPlace(Place place) {
        final BigDecimal averageValue = place.getPredictedHotWaterConsumption().divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_HALF_UP);
        final HeatingPlaceAndCommunalAreaAdvance heatingPlaceAndCommunalAreaAdvance = heatingPlaceAndCommunalAreaAdvanceFacade.findTheNewestAdvanceChangeFactor(place.getBuilding().getId());
        final BigDecimal pricePerCubicMeter = balanceService.getUnitWarmCostReportHotWater();
        final int month = LocalDate.now().getMonthValue();
        int count = 0;

        if (month == 1 || month == 4 || month == 7 || month == 10) {
            count = 3;
        }
        if (month == 2 || month == 5 || month == 8 || month == 11) {
            count = 2;
        }
        for (int i = 1; i < count; i++) {
            final LocalDate currentDate = LocalDate.now().minusMonths(2).plusMonths(i);
            final LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
            final HotWaterAdvance hotWaterAdvance = hotWaterAdvanceFacade.findByDate(firstDayOfMonth, place.getId());
            if (hotWaterAdvance != null) {
                final BigDecimal hotWaterAdvancePrice = averageValue.multiply(BigDecimal.valueOf(LocalDate.now().plusMonths(i).lengthOfMonth()))
                        .multiply(pricePerCubicMeter)
                        .multiply(heatingPlaceAndCommunalAreaAdvance.getAdvanceChangeFactor());
                hotWaterAdvance.setHotWaterAdvanceValue(hotWaterAdvancePrice);
                hotWaterAdvanceFacade.edit(hotWaterAdvance);
            }
        }
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Place getPlace(String placeId) {
        final Long id = Long.valueOf(placeId);
        return placeFacade.findPlaceById(id);
    }

    @Override
    @RolesAllowed(Roles.OWNER)
    public List<Place> getSelfAllPlaces(int pageNumber, int pageSize) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByUsername(username);
        return placeFacade.findByOwner(account, pageNumber, pageSize);
    }
}
