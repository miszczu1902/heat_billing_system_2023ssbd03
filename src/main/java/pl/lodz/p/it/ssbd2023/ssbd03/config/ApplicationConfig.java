package pl.lodz.p.it.ssbd2023.ssbd03.config;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@DeclareRoles({"GUEST","OWNER","ADMIN","MANAGER"})
public class ApplicationConfig extends Application {
}