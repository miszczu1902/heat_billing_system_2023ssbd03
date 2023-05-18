package pl.lodz.p.it.ssbd2023.ssbd03.config;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;

import java.time.ZoneId;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    public static ZoneId TIME_ZONE;

    @PostConstruct
    public void init() {
        TIME_ZONE = ZoneId.of(LoadConfig.loadPropertyFromConfig("zone"));
    }
}