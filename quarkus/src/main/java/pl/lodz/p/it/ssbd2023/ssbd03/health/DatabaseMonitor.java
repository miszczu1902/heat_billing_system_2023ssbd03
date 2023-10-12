package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import java.io.IOException;
import java.net.Socket;

@Readiness
@ApplicationScoped
public class DatabaseMonitor implements HealthCheck {
    @Inject
    @ConfigProperty(name = "db.hostname", defaultValue = "databaseRent")
    String dbHost;

    @Inject
    @ConfigProperty(name = "db.dbPort", defaultValue = "5432")
    int dbPort;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Polaczenie z bazą danych");
        try {
            pingServer(dbHost, dbPort);
            responseBuilder.up();
        } catch (Exception e) {
            responseBuilder.down()
                    .withData("Blad! ", e.getMessage());
        }
        return responseBuilder.build();
    }

    private void pingServer(String dbhost, int port) throws IOException {
        Socket socket = new Socket(dbhost, port);
        socket.close();
    }
}
