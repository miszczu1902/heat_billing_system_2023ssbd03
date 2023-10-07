package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class MemoryMonitor implements HealthCheck {
    @Inject
    @ConfigProperty(name = "memory.threshold", defaultValue = "0.9")
    private double memoryThreshold;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Zuzycie pamieci dla SSBD03");
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        double memoryUsage = (double) (maxMemory - freeMemory) / maxMemory;
        if (memoryUsage <= memoryThreshold) {
            responseBuilder.up()
                    .withData("usage", "Obecne zuzycie pamieci  %s%%".formatted(memoryUsage * 100));
        } else {
            responseBuilder.down().withData("usage", "Obecne zuzycie pamieci %s %%".formatted(memoryUsage * 100));
        }
        return responseBuilder.build();
    }
}
