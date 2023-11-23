package pl.lodz.p.it.ssbd2023.ssbd03.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.PasswordHash;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

@ApplicationScoped
public class BcryptHashGenerator implements PasswordHash {
    @Inject
    @ConfigProperty(name = "bcrypt.salt")
    String salt;

    @Override
    public void initialize(Map<String, String> parameters) {
        PasswordHash.super.initialize(parameters);
    }

    @Override
    public String generate(char[] chars) {
        return BCrypt.hashpw(new String(chars), salt);
    }

    @Override
    public boolean verify(char[] chars, String hashedPassword) {
        return BCrypt.checkpw(new String(chars), hashedPassword);
    }
}
