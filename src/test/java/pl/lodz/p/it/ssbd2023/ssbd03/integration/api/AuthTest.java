package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.junit.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest extends BasicIntegrationConfigTest {

    private static final CreateOwnerDTO initalizedOwner = new CreateOwnerDTO(
            "Bartosz",
            "Miszczak",
            "miszczu1000",
            "mailik1000@fakemail.com",
            "Password$123",
            "Password$123",
            "PL",
            "997654322"
    );

    public int authenticate(LoginDTO loginDTO) {
        return sendRequestAndGetResponse(Method.POST, "/accounts/login", loginDTO, ContentType.JSON)
                .getStatusCode();
    }

    @Test
    public void Authenticate() {
        LoginDTO loginDTO = new LoginDTO(
                Account.MANAGER, Account.PASSWORD
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(200, statusCode, "check if status code 200 after authentication");

        logger.info("PASSED!");
    }

    @Test
    public void AuthenticateWithTooShortAndTooLongUsername() {
        LoginDTO loginDTOShort = new LoginDTO(
                RandomStringUtils.randomAlphanumeric(5), Account.PASSWORD
        );

        int statusCode = authenticate(loginDTOShort);
        assertEquals(400, statusCode, "check if status code is 400 because of too short username");

        LoginDTO loginDTOLong = new LoginDTO(
                RandomStringUtils.randomAlphanumeric(20), Account.PASSWORD
        );

        statusCode = authenticate(loginDTOLong);
        assertEquals(400, statusCode, "check if status code is 400 because of too long username");

        logger.info("PASSED!");
    }

    @Test
    public void AuthenticateWithTooShortAndTooLongPassword() {
        LoginDTO loginDTOShort = new LoginDTO(
                Account.MANAGER, RandomStringUtils.randomAlphanumeric(5)
        );

        int statusCode = authenticate(loginDTOShort);
        assertEquals(400, statusCode, "check if status code is 400 because of too short password");

        LoginDTO loginDTOLong = new LoginDTO(
                Account.MANAGER, RandomStringUtils.randomAlphanumeric(34)
        );

        statusCode = authenticate(loginDTOLong);
        assertEquals(400, statusCode, "check if status code is 400 because of too long password");
        logger.info("PASSED!");
    }

    @Test
    public void InvalidLogin() {
        LoginDTO loginDTO = new LoginDTO(
                Account.OWNER + "A", Account.PASSWORD
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode, "check if status code 401 after invalid login");

        logger.info("PASSED!");
    }

    @Test
    public void InvalidPassword() {
        LoginDTO loginDTO = new LoginDTO(
                Account.OWNER, Account.PASSWORD + "!"
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode, "check if status code 401 after invalid password");

        logger.info("PASSED!");
    }

    @Test
    public void InValidCredentialsOnDisabledUser() {
        LoginDTO loginDTO = new LoginDTO(
                Account.OWNER, Account.PASSWORD + "!"
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode, "check if status code 401 after invalid password");

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode, "check if status code 401 after invalid password");

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode, "check if status code 401 after invalid password");

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode, "check if status code 401 after invalid password when account is disabled");

        logger.info("PASSED!");
    }

    @Test
    public void ValidCredentialsOnDisabledUser() {
        LoginDTO loginDTO = new LoginDTO(
                Account.OWNER, Account.PASSWORD + "!"
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode, "check if status code 401 after invalid password");

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode, "check if status code 401 after invalid password");

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode, "check if status code 401 after invalid password");

        LoginDTO loginDTOValidCredentials = new LoginDTO(
                Account.OWNER, Account.PASSWORD
        );

        statusCode = authenticate(loginDTOValidCredentials);
        assertEquals(401, statusCode, "check if status code 401 after valid credentials but account is disabled");

        logger.info("PASSED!");
    }

    @Test
    public void ValidCredentialsOnNotActiveUser() {
        int statusCode = sendRequestAndGetResponse(Method.POST, "/accounts/register", initalizedOwner, ContentType.JSON)
                .getStatusCode();
        assertEquals(201, statusCode);

        LoginDTO loginDTOValidCredentials = new LoginDTO(
                "miszczu1000", "Password$123"
        );

        statusCode = authenticate(loginDTOValidCredentials);
        assertEquals(401, statusCode, "check if status code 401 when credentials are valid but account is not activated");
    }
}
