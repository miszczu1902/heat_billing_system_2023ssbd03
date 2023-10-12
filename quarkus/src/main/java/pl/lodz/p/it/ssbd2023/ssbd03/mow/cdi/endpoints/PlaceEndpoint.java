package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.EnterPredictedHotWaterConsumptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ModifyPlaceOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ModifyPlaceDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlaceInfoDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.transactions.TransactionRollbackException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.PlaceService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.EtagValidator;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.PlaceMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/places")
@RequestScoped
public class PlaceEndpoint {
    @Inject PlaceService placeService;

    @Inject
    MessageSigner messageSigner;

    @Inject
    @ConfigProperty(name = "tx.retries", defaultValue = "3")
    int txRetries;

    protected static final Logger LOGGER = Logger.getGlobal();

    @GET
    @Path("/place/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response getPlace(@NotBlank @PathParam("placeId") String placeId) {
        final Place place = placeService.getPlace(placeId);
        final PlaceInfoDTO placeInfoDTO = PlaceMapper.createPlaceToPlaceInfoDTO(place);
        return Response.status(200)
                .header("ETag", messageSigner.sign(placeInfoDTO))
                .entity(placeInfoDTO)
                .build();
    }

    @PATCH
    @EtagValidator
    @Path("/owner/{placeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response modifyPlaceOwner(@PathParam("placeId") Long placeId, @NotNull @Valid ModifyPlaceOwnerDTO modifyPlaceOwnerDTO, @Context HttpServletRequest request) {
        final String etag = request.getHeader("If-Match");

        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                placeService.modifyPlaceOwner(placeId, modifyPlaceOwnerDTO.getUsername(), etag, modifyPlaceOwnerDTO.getVersion());
                rollbackTX = placeService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        placeService.modifyPlaceOwner(placeId, modifyPlaceOwnerDTO.getUsername(), etag, modifyPlaceOwnerDTO.getVersion());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @EtagValidator
    @Path("/place/{placeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response modifyPlace(@NotBlank @PathParam("placeId") String placeId,
                                @NotNull @Valid ModifyPlaceDTO modifyPlaceDTO,
                                @Context HttpServletRequest request) {
        final String etag = request.getHeader("If-Match");

        final String user = request.getUserPrincipal().getName();
        final boolean isOwner = request.isUserInRole(Roles.OWNER);
        final boolean isManager = request.isUserInRole(Roles.MANAGER);

        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                placeService.modifyPlace(placeId, modifyPlaceDTO.getArea(), etag, modifyPlaceDTO.getVersion(),
                        user, isOwner, isManager);
                rollbackTX = placeService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        placeService.modifyPlace(placeId ,modifyPlaceDTO.getArea(), etag, modifyPlaceDTO.getVersion(),
                user, isOwner, isManager);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/place/{placeId}/predicted-hot-water-consumption")
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response enterPredictedHotWaterConsumption(@NotBlank @PathParam("placeId") String placeId,
                                                      @NotNull @Valid EnterPredictedHotWaterConsumptionDTO enterPredictedHotWaterConsumptionDTO,
                                                      @Context HttpServletRequest request) {
        final String etag = request.getHeader("If-Match");

        final String user = request.getUserPrincipal().getName();
        final boolean isOwner = request.isUserInRole(Roles.OWNER);
        final boolean isManager = request.isUserInRole(Roles.MANAGER);

        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                placeService.enterPredictedHotWaterConsumption(placeId, enterPredictedHotWaterConsumptionDTO.getConsumption(),
                        etag, enterPredictedHotWaterConsumptionDTO.getVersion(), user, isOwner, isManager);
                rollbackTX = placeService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        placeService.enterPredictedHotWaterConsumption(placeId, enterPredictedHotWaterConsumptionDTO.getConsumption(),
                etag, enterPredictedHotWaterConsumptionDTO.getVersion(), user, isOwner, isManager);
        return Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/self")
    @RolesAllowed({Roles.OWNER})
    public Response getSelfAllPlaces(@DefaultValue("0") @QueryParam("pageNumber") int pageNumber,
                                     @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        return Response
                .status(200)
                .entity(
                        placeService.getSelfAllPlaces(pageNumber, pageSize).stream()
                                .map(PlaceMapper::createPlaceToPlacesListDTO)
                                .toList()).build();
    }
}
