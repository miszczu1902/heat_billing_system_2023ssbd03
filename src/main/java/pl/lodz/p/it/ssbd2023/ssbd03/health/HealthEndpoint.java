package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/health-check")
@PermitAll
public class HealthEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response checkHealthy() {
        return Response.ok().build();
    }
}
