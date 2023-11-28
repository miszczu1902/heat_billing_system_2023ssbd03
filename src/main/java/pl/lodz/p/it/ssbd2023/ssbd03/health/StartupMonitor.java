package pl.lodz.p.it.ssbd2023.ssbd03.health;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@Startup
@ApplicationScoped
public class StartupMonitor implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("Application started!").up().build();
    }
}
