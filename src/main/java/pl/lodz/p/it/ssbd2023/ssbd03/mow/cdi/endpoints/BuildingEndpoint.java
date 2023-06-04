package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateBuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.BuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.BuildingService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.BuildingMapper;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.PlaceMapper;

import java.util.logging.Logger;

import static pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.BuildingMapper.createBuildingToBuildingDTO;

@Path("/buildings")
@RequestScoped
public class BuildingEndpoint {
    @Inject
    private BuildingService buildingService;

    @Inject
    MessageSigner messageSigner;

    protected static final Logger LOGGER = Logger.getGlobal();

    //MOW 5
    @GET
    @Path("/building/{buildingId}/places")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response getAllPlacesInBuilding(@NotBlank @PathParam("buildingId") String buildingId,
                                           @DefaultValue("0") @QueryParam("pageNumber") int pageNumber,
                                           @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        return Response
                .status(200)
                .entity(
                        buildingService.getAllPlaces(buildingId, pageNumber, pageSize).stream()
                                .map(PlaceMapper::createPlaceToPlaceDTO)
                                .toList()).build();
    }

    //MOW
    @GET
    @Path("/building/{buildingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response getBuilding(@NotBlank @PathParam("buildingId") String buildingId) {
        BuildingDTO buildingDTO = createBuildingToBuildingDTO(buildingService.getBuilding(buildingId));
        return Response
                .status(200)
                .entity(buildingDTO)
                .build();
    }

    //MOW
    @PATCH
    @Path("/building/{buildingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response modifyBuilding(@NotBlank @PathParam("buildingId") String buildingId) {
        buildingService.modifyBuilding(buildingId);
        return Response.status(200).build();
    }

    //MOW 19
    @POST
    @Path("/building")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER})
    public Response addBuilding(@NotNull @Valid CreateBuildingDTO createBuildingDTO) {
        buildingService.addBuilding(BuildingMapper.createBuilding(createBuildingDTO));
        return Response.status(Response.Status.CREATED).build();
    }

    //MOW 18
    @PATCH
    @Path("/building/{buildingId}/place")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER})
    public Response addPlaceToBuilding(@NotBlank @PathParam("buildingId") String buildingId) {
        buildingService.addPlaceToBuilding();
        return Response.status(200).build();
    }

    //MOW 6
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response getAllBuildings(@DefaultValue("0") @QueryParam("pageNumber") int pageNumber,
                                    @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        return Response
                .status(200)
                .entity(
                        buildingService.getAllBuildings(pageNumber, pageSize).stream()
                                .map(BuildingMapper::createBuildingToBuildingDTO)
                                .toList()).build();
    }
}
