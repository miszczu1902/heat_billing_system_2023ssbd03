package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJBTransactionRolledbackException;
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
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.EnterPredictedHotWaterConsumptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlaceDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.PlaceService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.PlaceMapper;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/places")
@RequestScoped
public class PlaceEndpoint {
    @Inject
    private PlaceService placeService;

    @Inject
    MessageSigner messageSigner;

    protected static final Logger LOGGER = Logger.getGlobal();

    private int txRetries = Integer.parseInt(LoadConfig.loadPropertyFromConfig("tx.retries"));


    //MOW 10
    @GET
    @Path("/place/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response getPlace(@NotBlank @PathParam("placeId") String placeId) {
        final Place place = placeService.getPlace(placeId);
        final PlaceDTO placeDTO = PlaceMapper.createPlaceToPlaceDTO(place);
        return Response.status(200)
                .header("ETag", messageSigner.sign(placeDTO))
                .entity(placeDTO)
                .build();
    }

    //MOW 20
    @PATCH
    @Path("/owner/{placeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response modifyPlaceOwner(@NotBlank @PathParam("placeId") String placeId) {
        placeService.modifyPlaceOwner();
        return Response.status(200).build();
    }

    //MOW 21
    @PATCH
    @Path("/place/{placeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response modifyPlace(@NotBlank @PathParam("placeId") String placeId) {
        placeService.modifyPlace();
        return Response.status(200).build();
    }

    //MOW 16
    @POST
    @Path("/place/{placeId}/hot-water-watermeter")
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response enterHotWaterConsumption(@NotBlank @PathParam("placeId") String placeId, BigDecimal value) {
        placeService.enterHotWaterConsumption(placeId, LocalDate.now(), value);
        return Response.status(200).build();
    }

    //MOW M17
    @PATCH
    @Path("/place/{placeId}/predicted-hot-water-consumption")
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response enterPredictedHotWaterConsumption(@NotBlank @PathParam("placeId") String placeId,
                                                      @NotNull @Valid EnterPredictedHotWaterConsumptionDTO enterPredictedHotWaterConsumptionDTO,
                                                      @Context HttpServletRequest request) {
        final String etag = request.getHeader("If-Match");

        final String user = request.getUserPrincipal().getName();
        final boolean userRole = request.isUserInRole("OWNER");

        int retryTXCounter = txRetries; //limit prób ponowienia transakcji
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                placeService.enterPredictedHotWaterConsumption(placeId, enterPredictedHotWaterConsumptionDTO.getConsumption(),
                        etag, enterPredictedHotWaterConsumptionDTO.getVersion(), user, userRole);
                rollbackTX = placeService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (EJBTransactionRolledbackException ex) {
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
                etag, enterPredictedHotWaterConsumptionDTO.getVersion(), user, userRole);
        return Response.noContent().build();
    }

    //MOW
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{ownerUsername}")
    @RolesAllowed({Roles.MANAGER})
    public Response getOwnerAllPlaces(@NotBlank @PathParam("ownerUsername") String ownerUsername) {
        return Response.status(200).entity(placeService.getOwnerAllPlaces(ownerUsername)).build();
    }

    //MOW 11
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/self")
    @RolesAllowed({Roles.MANAGER})
    public Response getSelfAllPlaces() {
        return Response.status(200).entity(placeService.getSelfAllPlaces()).build();
    }
}
