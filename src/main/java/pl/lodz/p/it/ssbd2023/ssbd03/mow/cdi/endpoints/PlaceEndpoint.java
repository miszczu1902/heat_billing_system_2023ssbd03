package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.PlaceService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

@Path("/places")
@RequestScoped
public class PlaceEndpoint {
    @Inject
    private PlaceService placeService;

    @Inject
    MessageSigner messageSigner;

    protected static final Logger LOGGER = Logger.getGlobal();

    //MOW 10
    @GET
    @Path("/place/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response getPlace(@NotBlank @PathParam("placeId") String placeId) {
        return Response.status(200).entity(placeService.getPlace(placeId)).build();
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
    @POST
    @Path("/place/{placeId}/predicted-hot-water-consumption")
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response enterPredictedHotWaterConsumption(@NotBlank @PathParam("placeId") String placeId, BigDecimal consumption) {
        placeService.enterPredictedHotWaterConsumption(placeId, consumption);
        return Response.status(200).build();
    }

    //MOW ?????????
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
