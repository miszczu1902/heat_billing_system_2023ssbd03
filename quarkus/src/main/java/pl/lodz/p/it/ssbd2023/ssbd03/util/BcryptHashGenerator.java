package pl.lodz.p.it.ssbd2023.ssbd03.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.PasswordHash;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

@ApplicationScoped
public class BcryptHashGenerator implements PasswordHash {
    @Override
    public void initialize(Map<String, String> parameters) {
        PasswordHash.super.initialize(parameters);
    }

    @Override
    public String generate(char[] chars) {
        final String salt = LoadConfig.loadPropertyFromConfig("bcrypt.salt");
        return BCrypt.hashpw(new String(chars), salt);
    }

    @Override
    public boolean verify(char[] chars, String hashedPassword) {
        return BCrypt.checkpw(new String(chars), hashedPassword);
    }

    public boolean encryptAndVerify(char[] chars, String hashedPassword) {
        return verify(chars, generate(chars));
    }
}
