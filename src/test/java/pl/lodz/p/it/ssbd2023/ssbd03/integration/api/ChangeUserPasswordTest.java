package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangeUserPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AccountInfoDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeUserPasswordTest extends BasicIntegrationConfigTest {
    private static final String PASSWORD = "Password$123";
    private static final String USERNAME = "johndoe";
    private static final String USERNAME_FOR_PASSWORD_CHANGE = "mariasilva";
    private static final String USERNAME_FOR_SUCCESS_PASSWORD_CHANGE = "janekowalski";
    private static final String BAD_PASSWORD = "Pass";
    private static final String NEW_PASSWORD = PASSWORD + "1";
    private static final String URL_GET = "/accounts/" + USERNAME_FOR_PASSWORD_CHANGE;
    private static final String URL_PASSWORD = "/accounts/" + USERNAME_FOR_PASSWORD_CHANGE + "/password";
    private static final String URL_GET_SUCCESS = "/accounts/" + USERNAME_FOR_SUCCESS_PASSWORD_CHANGE;
    private static final String URL_PASSWORD_SUCCESS = "/accounts/" + USERNAME_FOR_SUCCESS_PASSWORD_CHANGE + "/password";

    @Before
    public void initialize() {
        auth(new LoginDTO(USERNAME, PASSWORD));
    }

    @Test
    public void shouldReturnBadRequestWhenEtagNotMatch() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                URL_GET_SUCCESS,
                null,
                ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                URL_PASSWORD,
                null,
                ContentType.JSON);
        assertEquals(400, enableUser.getStatusCode());
    }

    @Test
    public void shouldReturnConflictForWrongVersion() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                URL_GET,
                null,
                ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeUserPasswordDTO changeUserPasswordDTO = new ChangeUserPasswordDTO(NEW_PASSWORD, NEW_PASSWORD,
                accountInfoDTO.getVersion() + 1);
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeUserPasswordDTO, ContentType.JSON);
        assertEquals(409, response.getStatusCode(), "Check if request responses conflict.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldReturnConflictSameOldAndNewPassword() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                URL_GET,
                null,
                ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeUserPasswordDTO changeUserPasswordDTO = new ChangeUserPasswordDTO(PASSWORD, PASSWORD,
                accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeUserPasswordDTO, ContentType.JSON);
        assertEquals(409, response.getStatusCode(), "Check if request responses conflict.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldReturnBadRequestForNotSameNewPasswordAndRepeatedPassword() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                URL_GET,
                null,
                ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeUserPasswordDTO changeUserPasswordDTO = new ChangeUserPasswordDTO(NEW_PASSWORD, PASSWORD,
                accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeUserPasswordDTO, ContentType.JSON);
        assertEquals(400, response.getStatusCode(), "Check if request responses bad request.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldReturnBadRequestForBadNewPasswordPattern() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                URL_GET,
                null,
                ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeUserPasswordDTO changeUserPasswordDTO = new ChangeUserPasswordDTO(BAD_PASSWORD, PASSWORD,
                accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeUserPasswordDTO, ContentType.JSON);
        assertEquals(400, response.getStatusCode(), "Check if request responses bad request.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldReturnBadRequestForBadRepeatedNewPasswordPattern() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                URL_GET,
                null,
                ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeUserPasswordDTO changeUserPasswordDTO = new ChangeUserPasswordDTO(PASSWORD, BAD_PASSWORD,
                accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeUserPasswordDTO, ContentType.JSON);
        assertEquals(400, response.getStatusCode(), "Check if request responses bad request.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldChangeUserPasswordTest() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                URL_GET_SUCCESS,
                null,
                ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeUserPasswordDTO changeUserPasswordDTO = new ChangeUserPasswordDTO(NEW_PASSWORD, NEW_PASSWORD,
                accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD_SUCCESS, changeUserPasswordDTO, ContentType.JSON);
        assertEquals(204, response.getStatusCode(), "Check if request responses no content.");
        Response responseLogin = sendRequestAndGetResponse(Method.POST,
                "/accounts/login",
                new LoginDTO(USERNAME_FOR_SUCCESS_PASSWORD_CHANGE, NEW_PASSWORD),
                ContentType.JSON);
        assertEquals(200, responseLogin.getStatusCode(), "Check if request responses ok.");
        logger.info("PASSED!");
    }
}
