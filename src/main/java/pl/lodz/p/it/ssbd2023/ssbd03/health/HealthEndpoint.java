package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;

@Path("/health-check")
@ApplicationScoped
public class HealthEndpoint {

    @Inject
    AppStatus appStatus;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response checkHealthy() {
        return Response.ok().build();
    }

    @POST
    @Path("/change")
    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public Response changeAppStatus() {
        appStatus.setAvailable(!appStatus.isAvailable());
        return Response.noContent().build();
    }
}
