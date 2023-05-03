package pl.lodz.p.it.ssbd2023.ssbd03.auth;

import jakarta.ejb.Stateless;
import org.apache.commons.lang3.RandomStringUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;

@Stateless
public class ConfirmationTokenGenerator {
    public String createAccountConfirmationToken() {
        return RandomStringUtils.randomAlphanumeric(Integer.parseInt(LoadConfig.loadPropertyFromConfig("confirmation.token.length")));
    }
}
