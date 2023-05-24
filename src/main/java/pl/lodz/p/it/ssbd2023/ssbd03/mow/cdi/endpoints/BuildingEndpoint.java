package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.BuildingService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.util.logging.Logger;

@Path("/buildings")
@RequestScoped
public class BuildingEndpoint {
    @Inject
    private BuildingService buildingService;

    @Inject
    MessageSigner messageSigner;

    protected static final Logger LOGGER = Logger.getGlobal();


    @GET
    @Path("/building/{buildingId}/places")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response getAllPlacesInBuilding(@NotBlank @PathParam("buildingId") String buildingId) {
        return Response.status(200).entity(buildingService.getAllPlaces(buildingId)).build();
    }

    @GET
    @Path("/building/{buildingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response getBuilding(@NotBlank @PathParam("buildingId") String buildingId) {
        return Response.status(200).entity(buildingService.getBuilding(buildingId)).build();
    }

    @PATCH
    @Path("/building/{buildingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response modifyBuilding(@NotBlank @PathParam("buildingId") String buildingId) {
        buildingService.modifyBuilding(buildingId);
        return Response.status(200).build();
    }

    @POST
    @Path("/building")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER})
    public Response addBuilding() {
        buildingService.addBuilding();
        return Response.status(200).build();
    }

    @PATCH
    @Path("/building/{buildingId}/place")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER})
    public Response addPlaceToBuilding(@NotBlank @PathParam("buildingId") String buildingId) {
        buildingService.addPlaceToBuilding();
        return Response.status(200).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response getAllBuildings() {
        return Response.status(200).entity(buildingService.getAllBuildings()).build();
    }
}
