package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.junit.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.factory.IntegrationTestObjectsFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTest extends BasicIntegrationConfigTest {
    @Test
    public void registerOwnerTest() {
        CreateOwnerDTO owner = IntegrationTestObjectsFactory.createAccountToRegister();
        int statusCode = sendRequestAndGetResponse(Method.POST, "/accounts/register", owner, ContentType.JSON)
                .getStatusCode();
        assertEquals(201, statusCode);
        logger.info("PASSED!");
    }

    @Test
    public void validationDataTest() {
        Arrays.asList("firstName", "surname", "username", "email", "password", "language", "phoneNumber").forEach(field -> {
            int statusCode = sendRequestAndGetResponse(Method.POST, "/accounts/register", getInvalidOwnerData(field), ContentType.JSON)
                    .getStatusCode();
            assertEquals(400, statusCode, "Field: " + field + " validation.");
            logger.info("PASSED!");
        });
    }

    @Test
    public void tryCreateOwnerWithNotUniqueDataTest() {
        Arrays.asList("username", "email", "phoneNumber").forEach(field -> {
            int statusCode = sendRequestAndGetResponse(Method.POST, "/accounts/register", createNotUniqueOwner(field), ContentType.JSON)
                    .getStatusCode();
            assertEquals(409, statusCode, "Field: " + field + " unique validation.");
            logger.info("PASSED!");
        });
    }

    private CreateOwnerDTO getInvalidOwnerData(String field) {
        CreateOwnerDTO owner = IntegrationTestObjectsFactory.createAccountToRegister();

        switch (field) {
            case "firstName" -> owner.setFirstName(RandomStringUtils.randomAlphanumeric(33));
            case "surname" -> owner.setSurname(RandomStringUtils.randomAlphanumeric(33));
            case "username" -> owner.setUsername(RandomStringUtils.randomAlphanumeric(20));
            case "email" -> owner.setEmail(RandomStringUtils.randomAlphanumeric(10));
            case "password" -> {
                owner.setPassword(RandomStringUtils.randomAlphanumeric(5));
                owner.setRepeatedPassword(RandomStringUtils.randomAlphanumeric(5));
            }
            case "language" -> owner.setLanguage("X");
            case "phoneNumber" -> owner.setPhoneNumber(RandomStringUtils.randomAlphanumeric(20));
        }

        return owner;
    }

    private CreateOwnerDTO createNotUniqueOwner(String field) {
        CreateOwnerDTO owner = IntegrationTestObjectsFactory.createAccountToRegister();

        switch (field) {
            case "username" -> owner.setUsername("johndoe");
            case "email" -> owner.setEmail("johndoe@example.com");
            case "phoneNumber" -> owner.setPhoneNumber("123456789");
        }

        return owner;
    }
}
