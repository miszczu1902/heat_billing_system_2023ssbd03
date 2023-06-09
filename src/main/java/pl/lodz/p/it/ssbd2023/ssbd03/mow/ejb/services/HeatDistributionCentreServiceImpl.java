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
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class HeatDistributionCentreServiceImpl extends AbstractService implements HeatDistributionCentreService, SessionSynchronization {
    @Inject
    private HeatDistributionCentrePayoffFacade heatDistributionCentrePayoffFacade;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private HotWaterEntryFacade hotWaterEntryFacade;

    @Inject
    private PlaceFacade placeFacade;

    @Inject
    private MonthPayoffFacade monthPayoffFacade;

    @Inject
    private Internationalization internationalization;

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private MessageSigner messageSigner;

    @Inject
    private HeatDistributionCentreFacade heatDistributionCentreFacade;

    @Inject
    private ManagerFacade managerFacade;

    @Override
    @RolesAllowed({Roles.MANAGER})
    public Void getHeatDistributionCentreParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void modifyHeatingAreaFactor(BigDecimal heatingAreaFactorValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void modifyConsumption(BigDecimal consumptionValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void addConsumptionFromInvoice(BigDecimal consumption, BigDecimal consumptionCost, BigDecimal heatingAreaFactor) {
        final Manager manager = managerFacade.findManagerByUsername(securityContext.getCallerPrincipal().getName());

        if (!heatDistributionCentrePayoffFacade.checkIfRecordForThisMonthNotExists()) {
            throw AppException.consumptionAddException();
        }
        final List<HeatDistributionCentre> heatDistributionCentre = heatDistributionCentreFacade.getListOfHeatDistributionCentre();
        if (heatDistributionCentre.isEmpty()) {
            throw AppException.noHeatDistributionCentreException();
        }
        final HeatDistributionCentrePayoff heatDistributionCentrePayoff = new HeatDistributionCentrePayoff(consumption, consumptionCost, LocalDate.now(), heatingAreaFactor, manager, heatDistributionCentre.get(0));
        heatDistributionCentrePayoffFacade.create(heatDistributionCentrePayoff);

        final List<Place> places = placeFacade.findAllPlaces();
        final BigDecimal waterHeatingUnitCost = heatDistributionCentrePayoff.getConsumptionCost().multiply(new BigDecimal(1)
                .subtract(heatDistributionCentrePayoff.getHeatingAreaFactor())).divide(heatDistributionCentrePayoff.getConsumption(), 2, RoundingMode.CEILING);
        final BigDecimal centralHeatingUnitCost = heatDistributionCentrePayoff.getConsumptionCost().multiply(heatDistributionCentrePayoff.getHeatingAreaFactor())
                .divide(heatDistributionCentrePayoff.getConsumption(), 2, RoundingMode.CEILING);

        for (Place place : places) {

            final List<HotWaterEntry> hotWaterEntries = hotWaterEntryFacade.getListOfHotWaterEntriesForPlace(place.getId());
            final MonthPayoff monthPayoff;

            if (hotWaterEntries.size() == 1) {
                final BigDecimal hotWaterConsumption = hotWaterEntries.get(0).getEntryValue();
                monthPayoff = new MonthPayoff(LocalDate.now(), waterHeatingUnitCost, centralHeatingUnitCost, hotWaterConsumption, place, place.getOwner());
            } else if (hotWaterEntries.isEmpty()) {
                monthPayoff = new MonthPayoff(LocalDate.now(), waterHeatingUnitCost, centralHeatingUnitCost, place.getPredictedHotWaterConsumption(), place, place.getOwner());
            } else {
                final BigDecimal hotWaterConsumption = hotWaterEntries.get(0).getEntryValue().subtract(hotWaterEntries.get(1).getEntryValue());
                monthPayoff = new MonthPayoff(LocalDate.now(), waterHeatingUnitCost, centralHeatingUnitCost, hotWaterConsumption, place, place.getOwner());
            }

            monthPayoffFacade.create(monthPayoff);
        }
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public HeatDistributionCentre getHeatDistributionCentre(Long id) {
        return heatDistributionCentreFacade.getHeatDistributionCentre(id);
    }
}
