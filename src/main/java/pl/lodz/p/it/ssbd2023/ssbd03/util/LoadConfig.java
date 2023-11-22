package pl.lodz.p.it.ssbd2023.ssbd03.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadConfig {
    public static String loadPropertyFromConfig(String variable) {
        try (InputStream input = BcryptHashGenerator.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty(variable);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }
}
