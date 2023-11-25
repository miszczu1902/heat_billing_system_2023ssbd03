package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.*;

@Liveness
@ApplicationScoped
public class MemoryMonitor implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Zuzycie pamieci dla SSBD03");
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        double memoryUsage = (double) (maxMemory - freeMemory) / maxMemory;
        return responseBuilder.withData("usage", "Obecne zuzycie pamieci %s %%".formatted(memoryUsage * 100)).build();
    }
}
