package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AdvanceServiceImpl extends AbstractService implements AdvanceService {

    @Inject
    private PlaceFacade placeFacade;

    @Inject
    private ManagerFacade managerFacade;

    @Inject
    private BalanceFacade balanceFacade;

    @Inject
    private HotWaterAdvanceFacade hotWaterAdvanceFacade;

    @Inject
    private PastQuarterHotWaterPayoffFacade pastQuarterHotWaterPayoffFacade;

    @Inject
    private HeatingPlaceAndCommunalAreaAdvanceFacade heatingPlaceAndCommunalAreaAdvanceFacade;

    @Inject
    private HeatDistributionCentrePayoffFacade heatDistributionCentrePayoffFacade;

    @Inject
    private BalanceService balanceService;

    @Override
    @PermitAll
    public void calculateHotWaterAdvance() {
        boolean newOwner = false;
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

            newOwner = lastThreeEntries.stream()
                    .noneMatch(entry -> entry.getCreatedBy().getId().equals(place.getOwner().getId()));
            if (newOwner) {
                newOwner = lastThreeEntries.stream()
                        .map(entry -> entry.getCreatedBy().getId())
                        .anyMatch(createdById -> managers.stream()
                                .anyMatch(manager -> manager.getId().equals(createdById) && !manager.getId().equals(place.getOwner().getId())));
            }

            if (hotWaterEntries.size() >= 3 && !newOwner) {
                final BigDecimal sum = lastThreeEntries.get(2).getEntryValue().subtract(lastThreeEntries.get(0).getEntryValue());
                final LocalDate firstEntryDate = lastThreeEntries.get(0).getDate();
                final LocalDate thirdEntryDate = lastThreeEntries.get(2).getDate();
                final long daysBetween = ChronoUnit.DAYS.between(firstEntryDate, thirdEntryDate);
                averageValue = sum.divide(BigDecimal.valueOf(daysBetween), 2, BigDecimal.ROUND_HALF_UP);
                final Integer daysOfQuarter = lastThreeEntries.get(0).getDate().lengthOfMonth() + lastThreeEntries.get(1).getDate().lengthOfMonth() +
                        lastThreeEntries.get(2).getDate().lengthOfMonth();
                final PastQuarterHotWaterPayoff pastQuarterHotWaterPayoff = new PastQuarterHotWaterPayoff(place, averageValue, daysOfQuarter);
                pastQuarterHotWaterPayoffFacade.create(pastQuarterHotWaterPayoff);
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
}