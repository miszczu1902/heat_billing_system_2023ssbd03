package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.BalanceFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.BuildingFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.HeatingPlaceAndCommunalAreaAdvanceFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.PlaceFacade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BalanceServiceImpl extends AbstractService implements BalanceService {
    @Inject
    private BalanceFacade balanceFacade;

    @Inject
    private BuildingFacade buildingFacade;

    @Inject
    private PlaceFacade placeFacade;

    @Inject
    private SecurityContext securityContext;

    @Override
    @RolesAllowed({Roles.GUEST, Roles.MANAGER, Roles.OWNER})
    public MonthPayoff getUnitWarmCostReport() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public AnnualBalance getSelfReport(String placeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public AnnualBalance getUserReport(String placeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<AnnualBalance> getAllReports(int pageNumber, int pageSize, Long buildingId) {
        return balanceFacade.getListOfAnnualBalancesFromBuilding(pageNumber, pageSize, placeFacade.findPlacesByBuildingId(buildingId));
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public AnnualBalance getSelfReports() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public List<HotWaterAdvance> getSelfWaterAdvanceValue(Long placeId) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Place place = placeFacade.findPlaceById(placeId);

        if (!place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createNotOwnerOfPlaceException();
        }

        return balanceFacade.findAllAdvancesForPlace(placeId).stream()
                .filter(advance -> advance instanceof HotWaterAdvance)
                .map(advance -> (HotWaterAdvance) advance)
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<HotWaterAdvance> getUserWaterAdvanceValue(Long placeId) {
        return balanceFacade.findAllAdvancesForPlace(placeId).stream()
                .filter(advance -> advance instanceof HotWaterAdvance)
                .map(advance -> (HotWaterAdvance) advance)
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public List<HeatingPlaceAndCommunalAreaAdvance> getSelfHeatingAdvanceValue(Long placeId) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Place place = placeFacade.findPlaceById(placeId);

        if (!place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createNotOwnerOfPlaceException();
        }

        return balanceFacade.findAllAdvancesForPlace(placeId).stream()
                .filter(advance -> advance instanceof HeatingPlaceAndCommunalAreaAdvance)
                .map(advance -> (HeatingPlaceAndCommunalAreaAdvance) advance)
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<HeatingPlaceAndCommunalAreaAdvance> getUserHeatingAdvanceValue(Long placeId) {
        return balanceFacade.findAllAdvancesForPlace(placeId).stream()
                .filter(advance -> advance instanceof HeatingPlaceAndCommunalAreaAdvance)
                .map(advance -> (HeatingPlaceAndCommunalAreaAdvance) advance)
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public BigDecimal getUserHeatingBalance() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public BigDecimal getSelfHeatingBalance() {
        throw new UnsupportedOperationException();
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
                final BigDecimal hotWaterCost = monthPayoffForThisYear.getHotWaterConsumption()
                        .multiply(monthPayoffForThisYear.getWaterHeatingUnitCost());
                final BigDecimal heatingPlaceCost = monthPayoffForThisYear.getCentralHeatingUnitCost()
                        .multiply(place.getArea());
                final BigDecimal heatingCommunalAreaCost = monthPayoffForThisYear.getCentralHeatingUnitCost()
                        .multiply(place.getBuilding().getCommunalAreaAggregate());

                annualBalance.setTotalHotWaterCost(annualBalance.getTotalHotWaterCost().add(hotWaterCost));
                annualBalance.setTotalHeatingPlaceCost(annualBalance.getTotalHeatingPlaceCost().add(heatingPlaceCost));
                annualBalance.setTotalHeatingCommunalAreaCost(annualBalance.getTotalHeatingCommunalAreaCost()
                        .add(heatingCommunalAreaCost));

                balanceFacade.edit(annualBalance);
            });
        });
    }
}
