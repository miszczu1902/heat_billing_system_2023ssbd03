package pl.lodz.p.it.ssbd2023.ssbd03.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.ejb.Stateless;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Long.parseLong;

@Stateless
public class JwtGenerator {
    private final long timeout = parseLong(LoadConfig.loadPropertyFromConfig("timeout"));
    private final String secret = LoadConfig.loadPropertyFromConfig("secret");

    public String generateJWT(String login, Set<String> roles) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secret)
                .setSubject(login)
                .setIssuedAt(new Date())
                .claim("role", String.join(",", roles))
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .compact();
    }

    public Jws<Claims> parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt);
    }

    public String refreshTokenJWT(String token) {
        Set<String> roles = new HashSet<>();
        final Claims claims = parseJWT(token).getBody();
        final String rolesString = claims.get("role", String.class);
        final String[] rolesArray = rolesString.split(",");
        for (String role : rolesArray) {
            roles.add(role.trim());
        }
        final String username = claims.get("sub", String.class);
        return generateJWT(username, roles);
    }
}
