package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.core.Context;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@ApplicationScoped
@Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = AppException.class)
public class BalanceServiceImpl extends AbstractService implements BalanceService {
    @Inject
    BalanceFacade balanceFacade;

    @Inject
    BuildingFacade buildingFacade;

    @Inject
    HeatDistributionCentrePayoffFacade heatDistributionCentrePayoffFacade;

    @Inject
    PlaceFacade placeFacade;

    @Inject
    MonthPayoffFacade monthPayoffFacade;

    @Context
    SecurityContext securityContext;

    @Override
    @PermitAll
    public BigDecimal getUnitWarmCostReportHotWater() {
        MonthPayoff monthPayoff = monthPayoffFacade.findWaterHeatingUnitCost();
        return monthPayoff.getWaterHeatingUnitCost();
    }

    @Override
    @RolesAllowed({Roles.GUEST, Roles.MANAGER, Roles.OWNER})
    public BigDecimal getUnitWarmCostReportCentralHeating() {
        final HeatDistributionCentrePayoff heatDistributionCentrePayoff = heatDistributionCentrePayoffFacade.findLatestHeatDistributionCentrePayoff();
        final List<Building> buildings = buildingFacade.findAllBuildings();
        final BigDecimal totalArea = buildings.stream()
                .map(Building::getTotalArea)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal heatingPrice = heatDistributionCentrePayoff.getConsumptionCost().multiply(heatDistributionCentrePayoff.getHeatingAreaFactor());
        final BigDecimal pricePerSquareMeter = heatingPrice.divide(totalArea, 2, BigDecimal.ROUND_HALF_UP);

        return pricePerSquareMeter;
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public AnnualBalance getYearReport(Long reportId) {
        return balanceFacade.findBalanceById(reportId);
    }

    @Override
    @RolesAllowed(Roles.OWNER)
    public AnnualBalance getOwnerYearReport(Long reportId) {
        final AnnualBalance annualBalance = balanceFacade.findBalanceById(reportId);
        final String username = securityContext.getCallerPrincipal().getName();
        if (!annualBalance.getPlace().getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createNotOwnerOfPlaceException();
        }
        return annualBalance;
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<AnnualBalance> getAllReports(int pageNumber, int pageSize, Long buildingId) {
        return balanceFacade.getListOfAnnualBalancesFromBuilding(pageNumber, pageSize, placeFacade.findPlacesByBuildingId(buildingId));
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public List<AnnualBalance> getSelfReports(int pageNumber, int pageSize) {
        final String username = securityContext.getCallerPrincipal().getName();
        return balanceFacade.getListOfAnnualBalancesForOwner(pageNumber, pageSize, username);
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public List<HotWaterAdvance> getSelfWaterAdvanceValue(Long placeId, Integer year) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Place place = placeFacade.findPlaceById(placeId);

        if (!place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createNotOwnerOfPlaceException();
        }

        return balanceFacade.findAllHotWaterAdvancesForPlace(placeId, year);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<HotWaterAdvance> getUserWaterAdvanceValue(Long placeId, Integer year) {
        return balanceFacade.findAllHotWaterAdvancesForPlace(placeId, year);
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public List<HeatingPlaceAndCommunalAreaAdvance> getSelfHeatingAdvanceValue(Long placeId, Integer year) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Place place = placeFacade.findPlaceById(placeId);

        if (!place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createNotOwnerOfPlaceException();
        }

        return balanceFacade.findAllHeatingPlaceAndCommunalAreaAdvancesForPlace(placeId, year);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<HeatingPlaceAndCommunalAreaAdvance> getUserHeatingAdvanceValue(Long placeId, Integer year) {
        return balanceFacade.findAllHeatingPlaceAndCommunalAreaAdvancesForPlace(placeId, year);
    }


    @Override
    @PermitAll
    public void createYearReports() {
        final List<Place> places = placeFacade.findAllPlaces();
        final Short year = (short) LocalDate.now(TIME_ZONE).getYear();
        final BigDecimal bigDecimal = new BigDecimal(0);
        for (Place place : places) {
            final AnnualBalance annualBalance = new AnnualBalance(year, bigDecimal, bigDecimal, bigDecimal, bigDecimal,
                    bigDecimal, bigDecimal, place);
            balanceFacade.create(annualBalance);
        }
    }

    @PermitAll
    public void updateTotalCostYearReports() {
        final short year = (short) LocalDate.now(TIME_ZONE).getYear();
        final Month month = LocalDate.now(TIME_ZONE).getMonth();

        final List<AnnualBalance> annualBalanceList;
        if (Month.JANUARY == month) { //jezeli mamy styczen to aktualizujemy jeszcze raport z poprzedniego roku
            annualBalanceList = balanceFacade.getListOfAnnualBalancesForYear((short) (year - 1));
        } else {
            annualBalanceList = balanceFacade.getListOfAnnualBalancesForYear(year);
        }

        annualBalanceList.forEach(annualBalance -> {
            final Place place = annualBalance.getPlace();
            final Optional<MonthPayoff> monthPayoffForThisYearOptional = place.getMonthPayoffs().stream()
                    .filter(monthPayoff -> monthPayoff.getPayoffDate().getYear() == year
                            && monthPayoff.getPayoffDate().getMonth() == month).findFirst();

            monthPayoffForThisYearOptional.ifPresent(monthPayoffForThisYear -> {
                final LocalDateTime firstDayOfPreviousMonth = LocalDateTime.now(TIME_ZONE).minusMonths(1).with(TemporalAdjusters
                        .firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
                final List<Place> newPlacesList = placeFacade.findAllPlacesByBuildingIdAndNewerThanDate(
                        place.getBuilding().getId(), firstDayOfPreviousMonth);
                BigDecimal communalAreaInLastMonth = place.getBuilding().getCommunalAreaAggregate();
                if (!newPlacesList.isEmpty()) {
                    final BigDecimal totalAreaOfNewPlaces = newPlacesList.stream()
                            .map(Place::getArea)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    communalAreaInLastMonth = communalAreaInLastMonth.add(totalAreaOfNewPlaces);
                }

                final BigDecimal heatingCommunalAreaCost = monthPayoffForThisYear.getCentralHeatingUnitCost()
                        .multiply(communalAreaInLastMonth);
                final BigDecimal heatingPlaceCost = monthPayoffForThisYear.getCentralHeatingUnitCost()
                        .multiply(place.getArea());
                final BigDecimal hotWaterCost = monthPayoffForThisYear.getHotWaterConsumption()
                        .multiply(monthPayoffForThisYear.getWaterHeatingUnitCost());

                annualBalance.setTotalHotWaterCost(annualBalance.getTotalHotWaterCost().add(hotWaterCost));
                annualBalance.setTotalHeatingPlaceCost(annualBalance.getTotalHeatingPlaceCost().add(heatingPlaceCost));
                annualBalance.setTotalHeatingCommunalAreaCost(annualBalance.getTotalHeatingCommunalAreaCost()
                        .add(heatingCommunalAreaCost));

                balanceFacade.edit(annualBalance);
            });
        });
    }
}
