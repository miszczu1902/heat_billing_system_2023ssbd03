package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtGenerator {
    private static final String SECRET = "f4h9t87t3g473HGufuJ8fFHU4j39j48fmu948cx48cu2j9fj";
    private static final long timeout = 30*60*1000;

    public String generateJWT(String login,String[] roles) {

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setSubject(login)
                .setIssuedAt(new Date())
//                .claim("role", roles)
                .claim("role", "OWNER")
                .setExpiration(new Date(System.currentTimeMillis() + timeout))

                .compact();
    }

    public Jws<Claims> parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(jwt);
    }

}
