package pl.lodz.p.it.ssbd2023.ssbd03.auth;

import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;

import java.util.HashSet;
import java.util.Set;

public class AuthMechanism implements HttpAuthenticationMechanism {
    @Inject
    private JwtGenerator generator;

    private final String bearer = LoadConfig.loadPropertyFromConfig("bearer");
    private final String authorization = LoadConfig.loadPropertyFromConfig("authorization");

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                HttpMessageContext httpMessageContext) {
        String header = httpServletRequest.getHeader(authorization);
        Set<String> roles = new HashSet<>();
        if (header != null) {
            if (header.startsWith(bearer)) {
                try {
                    String token = header.replace(bearer, "");
                    Claims claims = generator.parseJWT(token).getBody();
                    String rolesString = claims.get("role", String.class);
                    String[] rolesArray = rolesString.split(",");
                    for (String role : rolesArray) {
                        roles.add(role.trim());
                    }
                    return httpMessageContext.notifyContainerAboutLogin(claims.getSubject(), roles);
                } catch (Exception e) {
                    return httpMessageContext.responseUnauthorized();
                }
            }
        }
        roles.add(Roles.GUEST);
        return httpMessageContext.notifyContainerAboutLogin("guest", roles);
    }
}