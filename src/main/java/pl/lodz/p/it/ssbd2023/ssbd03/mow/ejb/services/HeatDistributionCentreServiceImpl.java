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

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class HeatDistributionCentreServiceImpl extends AbstractService implements HeatDistributionCentreService, SessionSynchronization {
    @Inject
    private HeatDistributionCentrePayoffFacade heatDistributionCentrePayoffFacade;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private Internationalization internationalization;

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private MessageSigner messageSigner;

    @Inject
    private HeatDistributionCentreFacade heatDistributionCentreFacade;

    @Inject
    private HeatingPlaceAndCommunalAreaAdvanceFacade heatingPlaceAndCommunalAreaAdvanceFacade;

    @Inject
    private PlaceFacade placeFacade;

    @Inject
    private HotWaterEntryFacade hotWaterEntryFacade;

    @Inject
    private AccessLevelMappingFacade accessLevelMappingFacade;

    @Override
    @RolesAllowed({Roles.MANAGER})
    public Void getHeatDistributionCentreParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void insertHeatingAreaFactor(BigDecimal heatingAreaFactorValue, Long buildingId) {
        LocalDate date = LocalDate.now();
        if (date.getDayOfMonth() != 1) throw AppException.createAdvanceChangeFactorNotModifiedException();
        else date = date.minusMonths(3);

        if (heatingPlaceAndCommunalAreaAdvanceFacade.checkIfAdvanceChangeFactorNotModified(buildingId, date)) {
            List<Place> places = placeFacade.findPlacesByBuildingId(buildingId);
            places.forEach(place -> heatingPlaceAndCommunalAreaAdvanceFacade.create(
                    new HeatingPlaceAndCommunalAreaAdvance(LocalDate.now(), place,
                            new BigDecimal(0), new BigDecimal(0), heatingAreaFactorValue)));
        } else throw AppException.createAdvanceChangeFactorWasInsertedException();
    }

    @Override
    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public void insertConsumption(BigDecimal consumptionValue, Long placeId) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Place place = placeFacade.findPlaceByPlaceId(placeId);

        if (!hotWaterEntryFacade.checkIfHotWaterEntryWasInserted(placeId) && place != null) {
            HotWaterEntry hotWaterEntry;
            if (securityContext.isCallerInRole(Roles.MANAGER)) {
                hotWaterEntry = new HotWaterEntry(LocalDate.now(), consumptionValue, place, accessLevelMappingFacade.findManagerByUsername(username));
            } else {
                hotWaterEntry = new HotWaterEntry(LocalDate.now(), consumptionValue, place);
            }

            hotWaterEntryFacade.create(hotWaterEntry);
        } else {
            throw AppException.createHotWaterEntryCouldNotBeInsertedException();
        }
    }

    @Override
    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public void modifyConsumption(BigDecimal consumptionValue, Long placeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void addConsumptionFromInvoice(BigDecimal consumption, BigDecimal consumptionCost, BigDecimal heatingAreaFactor, Manager manager) {
        if (!heatDistributionCentrePayoffFacade.checkIfRecordForThisMonthNotExists()) {
            throw AppException.createConsumptionAddException();
        }
        final List<HeatDistributionCentre> heatDistributionCentre = heatDistributionCentreFacade.getListOfHeatDistributionCentre();
        if (heatDistributionCentre.isEmpty()) {
            throw AppException.createNoHeatDistributionCentreException();
        }
        HeatDistributionCentrePayoff heatDistributionCentrePayoff = new HeatDistributionCentrePayoff(consumption, consumptionCost, LocalDate.now(), heatingAreaFactor, manager, heatDistributionCentre.get(0));

        heatDistributionCentrePayoffFacade.create(heatDistributionCentrePayoff);
    }
}
