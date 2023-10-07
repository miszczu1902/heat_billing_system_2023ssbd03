package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
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

@ApplicationScoped
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PlaceServiceImpl extends AbstractService implements PlaceService, SessionSynchronization {

    @Inject PlaceFacade placeFacade;

    @Inject MessageSigner messageSigner;

    @Inject OwnerFacade ownerFacade;

    @Inject AccountFacade accountFacade;

    @Inject HotWaterAdvanceFacade hotWaterAdvanceFacade;

    @Inject HeatingPlaceAndCommunalAreaAdvanceFacade heatingPlaceAndCommunalAreaAdvanceFacade;

    @Context SecurityContext securityContext;

    @Inject BalanceService balanceService;

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

        final BigDecimal sum = place.getBuilding().getPlaces().stream()
                .map(Place::getArea)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(place.getArea());

        final BigDecimal newCommunalAreaAggregate = place.getBuilding().getTotalArea().subtract(sum).subtract(area);
        final int comparisonResult = newCommunalAreaAggregate.compareTo(place.getBuilding().getTotalArea().multiply(BigDecimal.valueOf(0.1)));
        if (comparisonResult < 0) {
            throw AppException.lackOfSpaceInTheBuildingException();
        }
        if (area != null) {
            place.setArea(area);
            place.getBuilding().setCommunalAreaAggregate(newCommunalAreaAggregate);
        }
        placeFacade.edit(place);
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

        if (isManager && place.getOwner().getAccount().getUsername().equals(username)
                && place.getPredictedHotWaterConsumption().intValue() != 0) {
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
        final Owner owner = account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Owner && accessLevel.getIsActive())
                .map(accessLevel -> (Owner) accessLevel)
                .findAny()
                .orElseThrow(AppException::createAccountIsNotOwnerException);
        return placeFacade.findByOwner(owner.getId(), pageNumber, pageSize);
    }
}
