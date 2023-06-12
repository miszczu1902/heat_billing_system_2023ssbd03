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
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.PlaceFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Internationalization;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private SecurityContext securityContext;

    @Override
    @RolesAllowed(Roles.MANAGER)
    public void modifyPlaceOwner() {
        throw new UnsupportedOperationException();

        //poproszę o wyzerowanie predictedHotWaterConsumption przy zmianie właściciela
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public void modifyPlace(String placeId, BigDecimal area, String etag, Long version) {
        final Long id = Long.valueOf(placeId);
        Place place = placeFacade.findPlaceById(id);

        if (!etag.equals(messageSigner.sign(place))) {
            throw AppException.createVerifierException();
        }

        if (!Objects.equals(version, place.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        if (area != null) {
            place.setArea(area);
        }

        placeFacade.edit(place);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void enterHotWaterConsumption(String placeId, LocalDate date, BigDecimal value) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void enterPredictedHotWaterConsumption(String placeId, BigDecimal consumption, String etag, Long version,
                                                  String username, boolean isOwner) {
        final Long id = Long.valueOf(placeId);
        Place place = placeFacade.findPlaceById(id);

        if (isOwner && !place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createNotOwnerOfPlaceException();
        }

        if (!etag.equals(messageSigner.sign(place))) {
            throw AppException.createVerifierException();
        }

        if (!Objects.equals(version, place.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        if (place.getPredictedHotWaterConsumption().intValue() != 0.00 && isOwner) {
            throw AppException.createPredictedHotWaterConsumptionValueAlreadySetException();
        }

        place.setPredictedHotWaterConsumption(consumption);
        placeFacade.edit(place);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Place getPlace(String placeId) {
        final Long id = Long.valueOf(placeId);
        return placeFacade.findPlaceById(id);
    }

    @Override
    @RolesAllowed(Roles.OWNER)
    public List<Place> getSelfAllPlaces(int pageNumber, int pageSize) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByUsername(username);
        return placeFacade.findByOwner(account, pageNumber, pageSize);
    }
}
