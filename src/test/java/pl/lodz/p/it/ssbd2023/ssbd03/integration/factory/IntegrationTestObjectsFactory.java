package pl.lodz.p.it.ssbd2023.ssbd03.integration.factory;

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;

public class IntegrationTestObjectsFactory {
    public static CreateOwnerDTO createAccountToRegister() {
        return new CreateOwnerDTO(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10) + "@fakemailik.com",
                "Password$123",
                "Password$123",
                "PL",
                RandomStringUtils.randomNumeric(9)
        );
    }
}
