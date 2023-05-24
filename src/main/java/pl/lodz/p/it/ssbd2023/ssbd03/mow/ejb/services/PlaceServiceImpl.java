package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.PlaceFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Internationalization;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PlaceServiceImpl extends AbstractService implements PlaceService, SessionSynchronization {

    @Inject
    private Internationalization internationalization;

    @Inject
    private PlaceFacade placeFacade;

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private MessageSigner messageSigner;

    @Override
    @RolesAllowed(Roles.MANAGER)
    public void modifyPlaceOwner() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public void modifyPlace() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void enterHotWaterConsumption(String placeId, LocalDate date, BigDecimal value) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void enterPredictedHotWaterConsumption(String placeId, BigDecimal consumption) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Place getPlace(String placeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public void addPlace() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<Place> getOwnerAllPlaces(String ownerUsername) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<Place> getSelfAllPlaces() {
        throw new UnsupportedOperationException();
    }
}
