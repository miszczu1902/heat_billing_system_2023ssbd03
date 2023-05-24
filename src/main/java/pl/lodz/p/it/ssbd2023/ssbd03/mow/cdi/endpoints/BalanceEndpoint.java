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

import java.util.logging.Logger;

@Path("/balances")
@RequestScoped
public class BalanceEndpoint {
    @Inject
    private BalanceService balanceService;

    @Inject
    MessageSigner messageSigner;

    protected static final Logger LOGGER = Logger.getGlobal();

    @Path("/unit-cost-report")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.GUEST, Roles.MANAGER, Roles.OWNER})
    public Response getUnitWarmCostReport() {
        return Response.ok().entity(balanceService.getUnitWarmCostReport()).build();
    }

    @Path("/self/report/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfReport(@NotBlank @PathParam("placeId") String placeId) {
        return Response.ok().entity(balanceService.getSelfReport(placeId)).build();
    }

    @Path("/report/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getUserReport(@NotBlank @PathParam("placeId") String placeId) {
        return Response.ok().entity(balanceService.getUserReport(placeId)).build();
    }

    @Path("/all-reports")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getAllReports() {
        return Response.ok().entity(balanceService.getAllReports()).build();
    }
}
