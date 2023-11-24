package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Readiness
@ApplicationScoped
public class DatabaseMonitor implements HealthCheck {
    @Inject
    DataSource dataSource;

    @Inject
    @ConfigProperty(name = "memory.threshold", defaultValue = "0.9")
    double memoryThreshold;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Polaczenie z API");
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        double memoryUsage = (double) (maxMemory - freeMemory) / maxMemory;
        if (memoryUsage <= memoryThreshold) {
            responseBuilder.up()
                    .withData("usage", "Obecne zuzycie pamieci  %s%%".formatted(memoryUsage * 100));
        } else {
            responseBuilder.down().withData("usage", "Obecne zuzycie pamieci %s %%".formatted(memoryUsage * 100));
        }

        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                return HealthCheckResponse.up("Połączenie z bazą danych jest aktywne");
            } else {
                return HealthCheckResponse.down("Nie można połączyć się z bazą danych");
            }
        } catch (SQLException e) {
            return HealthCheckResponse.down("Wystąpił błąd podczas próby połączenia z bazą danych: %s".formatted(e.getMessage()));
        }
    }
}
