package pl.lodz.p.it.ssbd2023.ssbd03.health;


import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/health-check")
public class HealthEndpoint {

    @GET
    @PermitAll
    public Response checkHealthy() {
        return Response.ok().build();
    }

}
