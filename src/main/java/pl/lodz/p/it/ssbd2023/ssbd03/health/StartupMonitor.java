package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Startup
@ApplicationScoped
public class StartupMonitor implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("Aplikacja SSBD03 2023 uruchomiona!").up().build();
    }
}
