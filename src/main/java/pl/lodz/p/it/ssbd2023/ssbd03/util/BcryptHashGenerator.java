package pl.lodz.p.it.ssbd2023.ssbd03.util;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BcryptHashGenerator {
    public static String generateHash(String plainText) {
        String salt = LoadConfig.loadSaltFromConfig("bcrypt.salt");
        return BCrypt.hashpw(plainText, salt);
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
