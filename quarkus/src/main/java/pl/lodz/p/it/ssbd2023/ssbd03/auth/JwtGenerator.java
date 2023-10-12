package pl.lodz.p.it.ssbd2023.ssbd03.auth;

import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;

import java.util.Date;
import java.util.Set;

@Boundary
@Transactional
public class JwtGenerator {
    @Inject
    @ConfigProperty(name = "timeout", defaultValue = "1800000")
    long timeout;

    @Inject
    @ConfigProperty(name = "smallrye.jwt.new-token.issuer", defaultValue = "ssbd03")
    String issuer;

    @Inject
    @ConfigProperty(name = "secret")
    String secret;

    @Inject
    JsonWebToken jsonWebToken;


    public String generateJwtToken(String subject, Set<String> roles) {
        return Jwt.issuer(issuer)
                .upn(subject)
                .groups(roles)
                .issuedAt(new Date().getTime())
                .expiresAt(new Date(System.currentTimeMillis() + timeout).getTime())
                .jws()
                .algorithm(SignatureAlgorithm.RS256)
                .sign();
    }

    public String refreshTokenJWT() {
        return generateJwtToken(jsonWebToken.getSubject(), jsonWebToken.getGroups());
    }

    //        try {
//            Set<String> roles = new HashSet<>();
//            final Claims claims = parseJWT(token).getBody();
//            final String rolesString = claims.get("role", String.class);
//            final String[] rolesArray = rolesString.split(",");
//            for (String role : rolesArray) {
//                roles.add(role.trim());
//            }
//            final String username = claims.get("sub", String.class);
//            return generateJWT(username, roles);
//        } catch (SignatureException | MalformedJwtException e) {
//            throw AppException.tokenIsNotValidException();
//        }
}