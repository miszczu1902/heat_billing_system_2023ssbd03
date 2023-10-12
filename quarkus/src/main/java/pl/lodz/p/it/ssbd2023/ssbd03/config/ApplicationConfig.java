package pl.lodz.p.it.ssbd2023.ssbd03.config;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.ZoneId;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    public static ZoneId TIME_ZONE;

    @Inject
    @ConfigProperty(name = "zone", defaultValue = "Europe/Warsaw")
    String timeZone;

    @PostConstruct
    public void init() {
        TIME_ZONE = ZoneId.of(timeZone);
    }
}