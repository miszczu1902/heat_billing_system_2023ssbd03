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
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.AddPlaceToBuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateBuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AccountInfoDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.BuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database.OptimisticLockAppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.transactions.TransactionRollbackException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.BuildingService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.BuildingMapper;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.PlaceMapper;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.BuildingMapper.createBuildingToBuildingDTO;

@Path("/buildings")
@RequestScoped
public class BuildingEndpoint {
    @Inject
    private BuildingService buildingService;

    @Inject
    private BuildingMapper buildingMapper;

    @Inject
    MessageSigner messageSigner;

    protected static final Logger LOGGER = Logger.getGlobal();

    private int txRetries = Integer.parseInt(LoadConfig.loadPropertyFromConfig("tx.retries"));

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

    @GET
    @Path("/building/{buildingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Response getBuilding(@NotBlank @PathParam("buildingId") String buildingId) {
        BuildingDTO buildingDTO = createBuildingToBuildingDTO(buildingService.getBuilding(buildingId));
        return Response
                .status(200)
                .entity(buildingDTO)
                .header("ETag", messageSigner.sign(buildingDTO))
                .build();
    }

    @POST
    @Path("/building")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER})
    public Response addBuilding(@NotNull @Valid CreateBuildingDTO createBuildingDTO) {
        buildingService.addBuilding(buildingMapper.createBuilding(createBuildingDTO));
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/place")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER})
    public Response addPlaceToBuilding(@NotNull @Valid AddPlaceToBuildingDTO addPlaceToBuildingDTO, @Context HttpServletRequest request) {
        final String etag = request.getHeader("If-Match");
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                buildingService.addPlaceToBuilding(addPlaceToBuildingDTO.getArea(), addPlaceToBuildingDTO.getHotWaterConnection(),
                         addPlaceToBuildingDTO.getPredictedHotWaterConsumption(), addPlaceToBuildingDTO.getBuildingId(),
                        addPlaceToBuildingDTO.getOwnerId(), etag, addPlaceToBuildingDTO.getVersion());
                rollbackTX = buildingService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (OptimisticLockAppException | TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        buildingService.addPlaceToBuilding(addPlaceToBuildingDTO.getArea(), addPlaceToBuildingDTO.getHotWaterConnection(),
                 addPlaceToBuildingDTO.getPredictedHotWaterConsumption(), addPlaceToBuildingDTO.getBuildingId(),
                addPlaceToBuildingDTO.getOwnerId(), etag, addPlaceToBuildingDTO.getVersion());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER})
    public Response getAllBuildings(@DefaultValue("0") @QueryParam("pageNumber") int pageNumber,
                                    @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        return Response
                .status(200)
                .entity(
                        buildingService.getAllBuildings(pageNumber, pageSize).stream()
                                .map(BuildingMapper::createBuildingToBuildingDTO)
                                .toList()).build();
    }

    @GET
    @Path("/owners")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.MANAGER})
    public Response getListOfOwners() {
        final List<AccountInfoDTO> listOfOwners = buildingService.getListOfOwners()
                .stream()
                .map(AccountMapper::createAccountInfoDTOEntity)
                .toList();
        return Response.ok().entity(listOfOwners).build();
    }
}
