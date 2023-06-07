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
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.HeatDistributionCentreFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.HeatDistributionCentrePayoffFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.HeatingPlaceAndCommunalAreaAdvanceFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.PlaceFacade;
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

    @Override
    @RolesAllowed({Roles.MANAGER})
    public Void getHeatDistributionCentreParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void modifyHeatingAreaFactor(BigDecimal heatingAreaFactorValue, Long placeId) {
        LocalDate date = LocalDate.now();
        if (date.getDayOfMonth() != 1) throw AppException.advanceChangeFactorNotModifiedException();
        else date = date.minusMonths(3);

        if (heatingPlaceAndCommunalAreaAdvanceFacade.checkIfAdvanceChangeFactorNotModified(placeId, date)) {
            Place place = placeFacade.findByPlaceId(placeId);
            heatingPlaceAndCommunalAreaAdvanceFacade.create(
                    new HeatingPlaceAndCommunalAreaAdvance(LocalDate.now(), place,
                            new BigDecimal(0), new BigDecimal(0), heatingAreaFactorValue));

            //TODO - tutaj przy liczeniu zaliczek przez system trzeba wywołać metody ze schedulera do liczenia zaliczek
        } else throw AppException.advanceChangeFactorWasInsertedException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void modifyConsumption(BigDecimal consumptionValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void addConsumptionFromInvoice(BigDecimal consumption, BigDecimal consumptionCost, BigDecimal heatingAreaFactor, Manager manager) {
        if (!heatDistributionCentrePayoffFacade.checkIfRecordForThisMonthNotExists()) {
            throw AppException.consumptionAddException();
        }
        final List<HeatDistributionCentre> heatDistributionCentre = heatDistributionCentreFacade.getListOfHeatDistributionCentre();
        if (heatDistributionCentre.isEmpty()) {
            throw AppException.noHeatDistributionCentreException();
        }
        HeatDistributionCentrePayoff heatDistributionCentrePayoff = new HeatDistributionCentrePayoff(consumption, consumptionCost, LocalDate.now(), heatingAreaFactor, manager, heatDistributionCentre.get(0));

        heatDistributionCentrePayoffFacade.create(heatDistributionCentrePayoff);
    }
}
