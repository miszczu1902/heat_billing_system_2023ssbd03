package pl.lodz.p.it.ssbd2023.ssbd03.config;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@DeclareRoles({Roles.GUEST, Roles.OWNER, Roles.ADMIN, Roles.MANAGER})
public class ApplicationConfig extends Application {
}