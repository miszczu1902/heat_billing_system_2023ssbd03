package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AdvanceServiceImpl extends AbstractService implements AdvanceService {

    @Inject PlaceFacade placeFacade;

    @Inject ManagerFacade managerFacade;

    @Inject HotWaterAdvanceFacade hotWaterAdvanceFacade;

    @Inject HeatingPlaceAndCommunalAreaAdvanceFacade heatingPlaceAndCommunalAreaAdvanceFacade;

    @Inject BalanceService balanceService;

    @Override
    @PermitAll
    public void calculateHotWaterAdvance() {
        BigDecimal averageValue = null;
        final List<Place> places = placeFacade.findAllPlaces()
                .stream()
                .filter(Place::getHotWaterConnection)
                .toList();
        final BigDecimal pricePerCubicMeter = balanceService.getUnitWarmCostReportHotWater();

        for (Place place : places) {
            final HeatingPlaceAndCommunalAreaAdvance heatingPlaceAndCommunalAreaAdvance = heatingPlaceAndCommunalAreaAdvanceFacade.findTheNewestAdvanceChangeFactor(place.getBuilding().getId());
            final List<HotWaterEntry> hotWaterEntries = place.getHotWaterEntries();
            final List<HotWaterEntry> lastThreeEntries = hotWaterEntries.subList(hotWaterEntries.size() - 3, hotWaterEntries.size());
            final List<Manager> managers = managerFacade.getListOfManagers();

            boolean oldOwner = lastThreeEntries.stream()
                    .map(entry -> entry.getCreatedBy().getId())
                    .distinct()
                    .count() == 1;

            if (!oldOwner) {
                oldOwner = lastThreeEntries.stream()
                        .anyMatch(entry -> managers.stream()
                                .anyMatch(manager -> manager.getAccount().getId().equals(entry.getCreatedBy().getId()) && !manager.getAccount().getId().equals(place.getOwner().getAccount().getId())));
            }

            if (hotWaterEntries.size() >= 3 && oldOwner) {
                final BigDecimal sum = lastThreeEntries.get(2).getEntryValue().subtract(lastThreeEntries.get(0).getEntryValue());
                final LocalDate firstEntryDate = lastThreeEntries.get(0).getDate();
                final LocalDate thirdEntryDate = lastThreeEntries.get(2).getDate();
                final long daysBetween = ChronoUnit.DAYS.between(firstEntryDate, thirdEntryDate);
                averageValue = sum.divide(BigDecimal.valueOf(daysBetween), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                averageValue = place.getPredictedHotWaterConsumption().divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_HALF_UP);
            }
            for (int i = 0; i < 3; i++) {
                final LocalDate currentDate = LocalDate.now().plusMonths(i);
                final LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
                final BigDecimal hotWaterAdvance = averageValue.multiply(BigDecimal.valueOf(LocalDate.now().plusMonths(i).lengthOfMonth()))
                        .multiply(pricePerCubicMeter)
                        .multiply(heatingPlaceAndCommunalAreaAdvance.getAdvanceChangeFactor());
                final HotWaterAdvance advance = new HotWaterAdvance(firstDayOfMonth, place, hotWaterAdvance);
                hotWaterAdvanceFacade.create(advance);
            }
        }
    }

    @Override
    @PermitAll
    public void calculateHeatingPlaceAndCommunalAreaAdvance() {
        final BigDecimal costPerSquerMeter = calculateCostPerSquerMeterFromPastQuarterAdvances();

        final List<Place> places = placeFacade.findAllPlaces()
                .stream()
                .filter(Place::getHotWaterConnection)
                .toList();
        for (Place place : places) {
            final HeatingPlaceAndCommunalAreaAdvance heatingPlaceAndCommunalAreaAdvance = heatingPlaceAndCommunalAreaAdvanceFacade.findTheNewestAdvanceChangeFactorByPlaceId(place.getId());
            final BigDecimal placeHeatingAdvance = costPerSquerMeter
                    .multiply(place.getArea())
                    .multiply(heatingPlaceAndCommunalAreaAdvance.getAdvanceChangeFactor());
            final BigDecimal communalAreaAdvance = place.getBuilding().getCommunalAreaAggregate()
                    .divide(BigDecimal.valueOf(placeFacade.findPlacesByBuildingId(place.getBuilding().getId()).size()), 2, RoundingMode.HALF_UP);
            heatingPlaceAndCommunalAreaAdvance.setHeatingPlaceAdvanceValue(placeHeatingAdvance);
            heatingPlaceAndCommunalAreaAdvance.setHeatingCommunalAreaAdvanceValue(communalAreaAdvance);
            heatingPlaceAndCommunalAreaAdvanceFacade.edit(heatingPlaceAndCommunalAreaAdvance);
            for (int i = 1; i < 3; i++) {
                final HeatingPlaceAndCommunalAreaAdvance advance = new HeatingPlaceAndCommunalAreaAdvance(LocalDate.now().plusMonths(i), place, placeHeatingAdvance, communalAreaAdvance, heatingPlaceAndCommunalAreaAdvance.getAdvanceChangeFactor());
                heatingPlaceAndCommunalAreaAdvanceFacade.create(advance);
            }
        }
    }

    @PermitAll
    private BigDecimal calculateCostPerSquerMeterFromPastQuarterAdvances() {
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
}