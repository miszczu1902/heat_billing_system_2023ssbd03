package pl.lodz.p.it.ssbd2023.ssbd03.util;

import org.mindrot.jbcrypt.BCrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BcryptHashGenerator {
    public static String generateHash(String plainText) {
        String salt = loadSaltFromConfig();
        return BCrypt.hashpw(plainText, salt);
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    private static String loadSaltFromConfig() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(fis);
            return properties.getProperty("bcrypt.salt");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }
}
