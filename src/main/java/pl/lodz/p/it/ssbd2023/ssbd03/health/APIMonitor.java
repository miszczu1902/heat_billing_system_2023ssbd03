package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.*;

@ApplicationScoped
@Liveness
public class APIMonitor implements HealthCheck {
    @Inject
    AppStatus appStatus;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Dzialanie aplikacji");
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        double memoryUsage = (double) (maxMemory - freeMemory) / maxMemory;
        responseBuilder.withData("usage", "Obecne zuzycie pamieci %s %%".formatted(memoryUsage * 100));
        responseBuilder.withData("connection", "%s".formatted(appStatus.isAvailable())).status(appStatus.isAvailable());
        return responseBuilder.build();
    }
}
