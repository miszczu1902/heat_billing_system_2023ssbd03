package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AnnualBalance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatingPlaceAndCommunalAreaAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.MonthPayoff;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.BalanceFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.BuildingFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.PlaceFacade;

import java.math.BigDecimal;
import java.util.List;

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

}
