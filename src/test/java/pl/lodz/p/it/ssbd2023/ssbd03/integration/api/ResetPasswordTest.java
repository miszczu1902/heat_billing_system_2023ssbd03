package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ResetPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResetPasswordTest extends BasicIntegrationConfigTest {
    private static final CreateOwnerDTO USER_NOT_ACTIVE = new CreateOwnerDTO(
            "Bartosz",
            "Miszczak",
            "notActive",
            "notActive@fakemail.com",
            "Password$123",
            "Password$123",
            "PL",
            "997654321"
    );
    private static final String USERNAME_FOR_SUCCESS_REQUEST = "mariasilva";
    private static final String URL_RESET_PASSWORD = "/accounts/reset-password";

    @BeforeClass
    public static void prepareTest() {
        setETAG("");
        setBEARER_TOKEN("");
    }

    @Test
    public void shouldReturnConflictForNotActivatedAccount() {
        int statusCode = sendRequestAndGetResponse(Method.POST, "/accounts/register", USER_NOT_ACTIVE, ContentType.JSON)
                .getStatusCode();
        assertEquals(201, statusCode);
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO(USER_NOT_ACTIVE.getUsername());
        Response response = sendRequestAndGetResponse(Method.POST, URL_RESET_PASSWORD, resetPasswordDTO, ContentType.JSON);
        assertEquals(409, response.getStatusCode());
    }

    @Test
    public void shouldReturnNotFoundForNotExistingAccount() {
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO("notExistingTest");
        Response response = sendRequestAndGetResponse(Method.POST, URL_RESET_PASSWORD, resetPasswordDTO, ContentType.JSON);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void shouldReturnBadRequestForNotValidUsername() {
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO("notExistingTest");
        Response response = sendRequestAndGetResponse(Method.POST, URL_RESET_PASSWORD, resetPasswordDTO, ContentType.JSON);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void shouldReturnNoContentForValidAccount() {
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO(USERNAME_FOR_SUCCESS_REQUEST);
        Response response = sendRequestAndGetResponse(Method.POST, URL_RESET_PASSWORD, resetPasswordDTO, ContentType.JSON);
        assertEquals(204, response.getStatusCode());
    }
}
