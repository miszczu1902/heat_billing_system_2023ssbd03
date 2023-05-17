package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationTest extends BasicIntegrationConfigTest {

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
        return sendRequestAndGetResponse(Method.POST, "/accounts/login", loginDTO, ContentType.JSON, false)
                .getStatusCode();
    }

    @Test
    public void Authenticate() {
        LoginDTO loginDTO = new LoginDTO(
                "janekowalski", "Password$123"
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(200, statusCode);

        logger.info("PASSED!");
    }


    @Test
    public void InvalidLogin() {
        LoginDTO loginDTO = new LoginDTO(
                "mariasilvA", "Password$123"
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode);

        logger.info("PASSED!");
    }

    @Test
    public void InvalidPassword() {
        LoginDTO loginDTO = new LoginDTO(
                "mariasilva", "Password$123!"
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode);

        logger.info("PASSED!");
    }

    @Test
    public void InValidCredentialsOnDisabledUser() {
        LoginDTO loginDTO = new LoginDTO(
                "mariasilva", "Password$123!"
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode);

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode);

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode);

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode);

        logger.info("PASSED!");
    }

    @Test
    public void ValidCredentialsOnDisabledUser() {
        LoginDTO loginDTO = new LoginDTO(
                "johndoe", "Password$123!"
        );

        int statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode);

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode);

        statusCode = authenticate(loginDTO);
        assertEquals(401, statusCode);

        LoginDTO loginDTOValidCredentials = new LoginDTO(
                "johndoe", "Password$123"
        );

        statusCode = authenticate(loginDTOValidCredentials);
        assertEquals(401, statusCode);

        logger.info("PASSED!");
    }

    @Test
    public void ValidCredentialsOnNotActiveUser() {
        int statusCode = sendRequestAndGetResponse(Method.POST, "/accounts/register", initalizedOwner, ContentType.JSON, false)
                .getStatusCode();
        assertEquals(201, statusCode);

        LoginDTO loginDTOValidCredentials = new LoginDTO(
                "miszczu1000", "Password$123"
        );

        statusCode = authenticate(loginDTOValidCredentials);
        assertEquals(401, statusCode);
    }
}
