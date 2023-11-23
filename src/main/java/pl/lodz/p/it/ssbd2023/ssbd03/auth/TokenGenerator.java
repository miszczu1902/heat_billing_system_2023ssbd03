package pl.lodz.p.it.ssbd2023.ssbd03.auth;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import org.apache.commons.lang3.RandomStringUtils;


@Boundary
public class TokenGenerator {
    @Inject
    @ConfigProperty(name = "confirmation.token.length")
    Integer confirmationTokenLength;

    @Inject
    @ConfigProperty(name = "reset.password.token.length")
    Integer resetPasswordTokenLength;

    public String createAccountConfirmationToken() {
        return RandomStringUtils.randomAlphanumeric(confirmationTokenLength);
    }

    public String createResetPasswordToken() {
        return RandomStringUtils.randomAlphanumeric(resetPasswordTokenLength);
    }
}
