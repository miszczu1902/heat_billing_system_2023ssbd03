package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Readiness
@ApplicationScoped
public class DatabaseMonitor implements HealthCheck {
    @Inject
    DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
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
