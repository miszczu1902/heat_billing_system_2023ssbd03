package pl.lodz.p.it.ssbd2023.ssbd03.auth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;

public class JwtGenerator {
    private static final String SECRET = "f4h9t87t3g473HGufuJ8fFHU4j39j48fmu948cx48cu2j9fj";//przeniesc do config properties
    private static final long timeout = 30*60*1000;//przeniesc do config properties - bcrypt hash generator sprawdzic

    public String generateJWT(String login,String[] roles) {

        return Jwts.builder()

                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setSubject(login)
                .setIssuedAt(new Date())
//                .claim("role", roles)
                .claim("role", Roles.OWNER)
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .compact();
    }

    public Jws<Claims> parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(jwt);
    }
}
