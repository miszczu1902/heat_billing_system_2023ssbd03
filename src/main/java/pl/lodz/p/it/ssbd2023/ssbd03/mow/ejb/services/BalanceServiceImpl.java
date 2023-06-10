package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.BalanceFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.BuildingFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.PlaceFacade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    public HotWaterAdvance getSelfWaterAdvanceValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public HotWaterAdvance getSelfWaterAdvance() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public HotWaterAdvance getUserWaterAdvanceValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public HotWaterAdvance getUserWaterAdvance() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public HeatingPlaceAndCommunalAreaAdvance getSelfHeatingAdvanceValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.OWNER})
    public HeatingPlaceAndCommunalAreaAdvance getSelfHeatingAdvance() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public HeatingPlaceAndCommunalAreaAdvance getUserHeatingAdvanceValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public HeatingPlaceAndCommunalAreaAdvance getUserHeatingAdvance() {
        throw new UnsupportedOperationException();
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
        final Short year = (short) LocalDateTime.now(TIME_ZONE).getYear();
        final BigDecimal bigDecimal = new BigDecimal(0);
        for (Place place : places) {
            final AnnualBalance annualBalance = new AnnualBalance(year, bigDecimal, bigDecimal, bigDecimal, bigDecimal,
                    bigDecimal, bigDecimal, place);
            balanceFacade.create(annualBalance);
        }
    }
}
