package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.BalanceService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.BalanceMapper;

import java.util.logging.Logger;

@Path("/balances")
@RequestScoped
public class BalanceEndpoint {
    @Inject
    private BalanceService balanceService;

    @Inject
    MessageSigner messageSigner;

    protected static final Logger LOGGER = Logger.getGlobal();

    //MOW 1
    @Path("/unit-cost-report")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.GUEST, Roles.MANAGER, Roles.OWNER})
    public Response getUnitWarmCostReport() {
        return Response.ok().entity(balanceService.getUnitWarmCostReport()).build();
    }

    //MOW 2
    @Path("/self/report/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfReport(@NotBlank @PathParam("placeId") String placeId) {
        return Response.ok().entity(balanceService.getSelfReport(placeId)).build();
    }

    //MOW 2
    @Path("/report/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getUserReport(@NotBlank @PathParam("placeId") String placeId) {
        return Response.ok().entity(balanceService.getUserReport(placeId)).build();
    }

    //MOW 3
    @Path("/all-reports/{buildingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getAllReports(@DefaultValue("0") @QueryParam("pageNumber") int pageNumber,
                                  @DefaultValue("10") @QueryParam("pageSize") int pageSize,
                                  @NotBlank @PathParam("buildingId") String buildingId) {
        return Response.ok().entity(balanceService.getAllReports(pageNumber, pageSize, buildingId).stream()
                .map(BalanceMapper::balancesToAnnualBalancesForListDTO)
                .toList()).build();
    }

    //MOW 9
    @Path("/self/all-reports")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfReports() {
        return Response.ok().entity(balanceService.getSelfReports()).build();
    }

    //MOW 7
    @Path("/self/{placeId}/water-advance-value")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfWaterAdvanceValue() {
        return Response.ok().entity(balanceService.getSelfWaterAdvanceValue()).build();
    }

    //MOW 7
    @Path("/self/{placeId}/place-water-advance")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfWaterAdvance() {
        return Response.ok().entity(balanceService.getSelfWaterAdvance()).build();
    }

    //MOW 7
    @Path("/{placeId}/water-advance-value")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getUserWaterAdvanceValue() {
        return Response.ok().entity(balanceService.getUserWaterAdvanceValue()).build();
    }

    //MOW 7
    @Path("/{placeId}/advance")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getUserWaterAdvance() {
        return Response.ok().entity(balanceService.getUserWaterAdvance()).build();
    }

    //MOW 7
    @Path("/self/{placeId}/heat-advance-value")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfHeatingAdvanceValue() {
        return Response.ok().entity(balanceService.getSelfHeatingAdvanceValue()).build();
    }

    //MOW 7
    @Path("/self/{placeId}/advance")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfHeatingAdvance() {
        return Response.ok().entity(balanceService.getSelfHeatingAdvance()).build();
    }

    //MOW 7
    @Path("/{placeId}/advance-value")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getUserHeatingAdvanceValue() {
        return Response.ok().entity(balanceService.getUserHeatingAdvanceValue()).build();
    }

    //MOW 7
    @Path("/{placeId}/heat-advance")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getUserHeatingAdvance() {
        return Response.ok().entity(balanceService.getUserHeatingAdvance()).build();
    }

    //MOW8
    @Path("self/cost-balance")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfHeatingBalance() {
        return Response.ok().entity(balanceService.getSelfHeatingBalance()).build();
    }

    //MOW 8
    @Path("{user}/cost-balance")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getUserHeatingBalance() {
        return Response.ok().entity(balanceService.getUserHeatingBalance()).build();
    }
}