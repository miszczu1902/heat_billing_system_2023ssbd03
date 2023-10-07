package pl.lodz.p.it.ssbd2023.ssbd03.auth;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import org.apache.commons.lang3.RandomStringUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;

import java.util.Properties;

@Stateless
public class TokenGenerator {
    private final Properties properties = new Properties();

    @PostConstruct
    public void init() {
        properties.put("confirmation.token.length", LoadConfig.loadPropertyFromConfig("confirmation.token.length"));
        properties.put("reset.password.token.length", LoadConfig.loadPropertyFromConfig("reset.password.token.length"));
    }

    public String createAccountConfirmationToken() {
        return RandomStringUtils.randomAlphanumeric(Integer.parseInt(properties.getProperty("confirmation.token.length")));
    }

    public String createResetPasswordToken() {
        return RandomStringUtils.randomAlphanumeric(Integer.parseInt(properties.getProperty("reset.password.token.length")));
    }
}
