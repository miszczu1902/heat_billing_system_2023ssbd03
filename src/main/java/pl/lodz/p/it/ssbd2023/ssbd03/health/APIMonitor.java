package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Liveness
@Readiness
@ApplicationScoped
public class APIMonitor implements HealthCheck {
    @Inject
    @ConfigProperty(name = "user.hostname", defaultValue = "0.0.0.0")
    String userServiceHost;

    @Inject
    @ConfigProperty(name = "user.port", defaultValue = "8080")
    int userServicePort;

    @Inject
    @ConfigProperty(name = "user.path", defaultValue = "/api/health-check")
    String userServicePath;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("UserService connection");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://%s:%s%s".formatted(userServiceHost, userServicePort, userServicePath)))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            responseBuilder.status(response.statusCode() == Response.Status.OK.getStatusCode());
        } catch (Exception e) {
            responseBuilder.down()
                    .withData("error", e.getMessage());
        }
        return responseBuilder.build();
    }
}
