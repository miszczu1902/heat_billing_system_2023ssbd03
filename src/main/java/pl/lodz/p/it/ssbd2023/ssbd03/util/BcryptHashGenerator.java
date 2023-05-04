package pl.lodz.p.it.ssbd2023.ssbd03.util;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptHashGenerator {
    public static String generateHash(String plainText) {
        String salt = LoadConfig.loadPropertyFromConfig("bcrypt.salt");
        return BCrypt.hashpw(plainText, salt);
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
