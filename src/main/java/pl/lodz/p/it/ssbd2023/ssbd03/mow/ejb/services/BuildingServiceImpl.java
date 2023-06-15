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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BuildingServiceImpl extends AbstractService implements BuildingService, SessionSynchronization {
    @Inject
    private BuildingFacade buildingFacade;

    @Inject
    private PlaceFacade placeFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private BalanceFacade balanceFacade;

    @Inject
    private OwnerFacade ownerFacade;

    @Inject
    private HotWaterAdvanceFacade hotWaterAdvanceFacade;

    @Inject
    private HeatingPlaceAndCommunalAreaAdvanceFacade heatingPlaceAndCommunalAreaAdvanceFacade;

    @Inject
    private BalanceService balanceService;

    @Inject
    private HeatDistributionCentrePayoffFacade heatDistributionCentrePayoffFacade;

    @Inject
    private Internationalization internationalization;

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private MessageSigner messageSigner;

    @Inject
    private SecurityContext securityContext;

    @Override
    @RolesAllowed(Roles.MANAGER)
    public List<Place> getAllPlaces(String buildingId, int pageNumber, int pageSize) {
        final Long id = Long.valueOf(buildingId);
        buildingFacade.findById(id);
        return placeFacade.findByBuildingId(id, pageNumber, pageSize);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Building getBuilding(String buildingId) {
        final Long id = Long.valueOf(buildingId);
        return buildingFacade.findById(id);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void modifyBuilding(String buildingId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void addBuilding(Building building) {
        buildingFacade.create(building);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void addPlaceToBuilding(BigDecimal area, Boolean hotWaterConnection, BigDecimal predictedHotWaterConsumption,
                                   Long buildingId, Long ownerId, String etag, Long version) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByUsername(username);

        final Owner owner = ownerFacade.findById(ownerId);

        if (owner.getId().equals(account.getId())) {
            throw AppException.addingPlaceToTheSameAccountException();
        }

        final Building building = buildingFacade.findById(buildingId);

        if (!etag.equals(messageSigner.sign(building))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, building.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        final BigDecimal sum = building.getPlaces().stream()
                .map(Place::getArea)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal newCommunalAreaAgregate = building.getTotalArea().subtract(sum).subtract(area);
        final int comparisonResult = newCommunalAreaAgregate.compareTo(building.getTotalArea().multiply(BigDecimal.valueOf(0.1)));
        if (comparisonResult < 0) {
            throw AppException.lackOfSpaceInTheBuildingException();
        }

        final Short placeNumber = (short) (building.getPlaces().size() + 1);
        final Place place = new Place(placeNumber, area, hotWaterConnection, true, predictedHotWaterConsumption, building, owner);
        placeFacade.create(place);

        building.setCommunalAreaAggregate(newCommunalAreaAgregate);
        building.getPlaces().add(place);
        buildingFacade.edit(building);

        final BigDecimal bigDecimal = new BigDecimal(0);
        final AnnualBalance annualBalance = new AnnualBalance((short) LocalDateTime.now().getYear(), bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, place);
        balanceFacade.create(annualBalance);

        if (place.getHotWaterConnection()) {
            calculateHotWaterAdvanceForNewPlace(place);
        }
        calculateHeatingPlaceAndCommunalAreaAdvanceForNewPlace(place);
    }

    @RolesAllowed({Roles.MANAGER})
    private void calculateHotWaterAdvanceForNewPlace(Place place) {
        final BigDecimal averageValue = place.getPredictedHotWaterConsumption().divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_HALF_UP);
        final HeatingPlaceAndCommunalAreaAdvance heatingPlaceAndCommunalAreaAdvance = heatingPlaceAndCommunalAreaAdvanceFacade.findTheNewestAdvanceChangeFactor(place.getBuilding().getId());
        final BigDecimal pricePerCubicMeter = balanceService.getUnitWarmCostReportHotWater();
        BigDecimal advanceChangeFactor = new BigDecimal(1);
        if (heatingPlaceAndCommunalAreaAdvance != null) {
            advanceChangeFactor = heatingPlaceAndCommunalAreaAdvance.getAdvanceChangeFactor();
        }
        final int month = LocalDate.now().getMonthValue();
        int count = 0;

        if (month == 1 || month == 4 || month == 7 || month == 10) {
            count = 3;
        }
        if (month == 2 || month == 5 || month == 8 || month == 11) {
            count = 2;
        }
        for (int i = 1; i < count; i++) {
            final LocalDate currentDate = LocalDate.now().plusMonths(i);
            final LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
            final BigDecimal hotWaterAdvance = averageValue.multiply(BigDecimal.valueOf(LocalDate.now().plusMonths(i).lengthOfMonth()))
                    .multiply(pricePerCubicMeter)
                    .multiply(advanceChangeFactor);
            final HotWaterAdvance advance = new HotWaterAdvance(firstDayOfMonth, place, hotWaterAdvance);
            hotWaterAdvanceFacade.create(advance);
        }
    }

    @RolesAllowed({Roles.MANAGER})
    private void calculateHeatingPlaceAndCommunalAreaAdvanceForNewPlace(Place place) {
        final BigDecimal costPerSquerMeter = calculateCostPerSquerMeterFromPastQuarterAdvancesForNewLocal();

        final HeatingPlaceAndCommunalAreaAdvance heatingPlaceAndCommunalAreaAdvance = heatingPlaceAndCommunalAreaAdvanceFacade.findTheNewestAdvanceChangeFactor(place.getBuilding().getId());
        final BigDecimal placeHeatingAdvance = costPerSquerMeter
                .multiply(place.getArea())
                .multiply(heatingPlaceAndCommunalAreaAdvance.getAdvanceChangeFactor());
        final BigDecimal communalAreaAdvance = place.getBuilding().getCommunalAreaAggregate()
                .divide(BigDecimal.valueOf(placeFacade.findPlacesByBuildingId(place.getBuilding().getId()).size()), 2, RoundingMode.HALF_UP);

        final int month = LocalDate.now().getMonthValue();
        int count = 0;

        if (month == 1 || month == 4 || month == 7 || month == 10) {
            count = 3;
        }
        else if (month == 2 || month == 5 || month == 8 || month == 11) {
            count = 2;
        }
        for (int i = 1; i < count; i++) {
            final HeatingPlaceAndCommunalAreaAdvance advance = new HeatingPlaceAndCommunalAreaAdvance(LocalDate.now().plusMonths(i), place, placeHeatingAdvance, communalAreaAdvance, heatingPlaceAndCommunalAreaAdvance.getAdvanceChangeFactor());
            heatingPlaceAndCommunalAreaAdvanceFacade.create(advance);
        }
    }

    @RolesAllowed({Roles.MANAGER})
    private BigDecimal calculateCostPerSquerMeterFromPastQuarterAdvancesForNewLocal() {
        final List<HeatingPlaceAndCommunalAreaAdvance> heatingPlaceAndCommunalAreaAdvances = heatingPlaceAndCommunalAreaAdvanceFacade.findLastAdvances()
                .stream()
                .toList();
        final List<Place> placesInTheBuilding = placeFacade.findPlacesByBuildingId(heatingPlaceAndCommunalAreaAdvances.get(0).getPlace().getBuilding().getId())
                .stream()
                .toList();
        final BigDecimal totalAdvanceSum = heatingPlaceAndCommunalAreaAdvances.stream()
                .filter(advance -> placesInTheBuilding.stream()
                        .anyMatch(place -> place.getId().equals(advance.getPlace().getId())))
                .map(advance -> advance.getHeatingPlaceAdvanceValue().add(advance.getHeatingCommunalAreaAdvanceValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal costPerSquareMeter = totalAdvanceSum.divide(heatingPlaceAndCommunalAreaAdvances.get(0).getPlace().getBuilding().getTotalArea(), 2, RoundingMode.HALF_UP);

        return costPerSquareMeter;
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<Building> getAllBuildings(int pageNumber, int pageSize) {
        return buildingFacade.getListOfBuildingsWithPaging(pageNumber, pageSize);
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public List<Account> getListOfOwners() {
        List<Owner> owners = ownerFacade.getListOfOwners();
        List<Account> accounts = owners.stream()
                .map(Owner::getAccount)
                .collect(Collectors.toList());
        return accounts;
    }
}
