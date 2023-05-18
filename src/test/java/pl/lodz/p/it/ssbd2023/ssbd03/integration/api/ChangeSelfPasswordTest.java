package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangeSelfPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AccountInfoDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeSelfPasswordTest extends BasicIntegrationConfigTest {
    private static final String PASSWORD = "Password$123";
    private static final String USERNAME = "johndoe";
    private static final String USERNAME_FOR_SUCCESS_REQUEST = "mariasilva";
    private static final String URL_PASSWORD = "/accounts/self/password";
    private static final String URL_GET = "/accounts/self";
    private final String BAD_PASSWORD = "Pass";
    private final String NEW_PASSWORD = PASSWORD + "1";

    @Test
    public void shouldReturnBadRequestWhenEtagNotMatch() {
        initialize();
        Response getUserResponse = sendRequestAndGetResponse(Method.GET, "/accounts/mariasilva", null, ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        Response enableUser = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, null, ContentType.JSON);
        assertEquals(400, enableUser.getStatusCode());
    }

    @Test
    public void shouldReturnConflictForWrongVersion() {
        initialize();
        Response getUserResponse = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeSelfPasswordDTO changeSelfPasswordDTO = new ChangeSelfPasswordDTO(PASSWORD, NEW_PASSWORD, NEW_PASSWORD, accountInfoDTO.getVersion() + 1);
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeSelfPasswordDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(409, statusCode, "Check if request responses conflict.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldReturnConflictForSameOldAndNewPassword() {
        initialize();
        Response getUserResponse = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeSelfPasswordDTO changeSelfPasswordDTO = new ChangeSelfPasswordDTO(PASSWORD, PASSWORD, PASSWORD, accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeSelfPasswordDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(409, statusCode, "Check if request responses conflict.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldReturnBadRequestForNotSameNewPasswordAndRepeatedPassword() {
        initialize();
        Response getUserResponse = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeSelfPasswordDTO changeSelfPasswordDTO = new ChangeSelfPasswordDTO(PASSWORD, NEW_PASSWORD, PASSWORD, accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeSelfPasswordDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode, "Check if request responses bad request.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldReturnBadRequestForBadOldPasswordPattern() {
        initialize();
        Response getUserResponse = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeSelfPasswordDTO changeSelfPasswordDTO = new ChangeSelfPasswordDTO(BAD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD, accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeSelfPasswordDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode, "Check if request responses bad request.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldReturnBadRequestForBadNewPasswordPattern() {
        initialize();
        Response getUserResponse = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeSelfPasswordDTO changeSelfPasswordDTO = new ChangeSelfPasswordDTO(PASSWORD, BAD_PASSWORD, NEW_PASSWORD, accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeSelfPasswordDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode, "Check if request responses bad request.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldReturnBadRequestForBadRepeatedNewPasswordPattern() {
        initialize();
        Response getUserResponse = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeSelfPasswordDTO changeSelfPasswordDTO = new ChangeSelfPasswordDTO(PASSWORD, NEW_PASSWORD, BAD_PASSWORD, accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeSelfPasswordDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode, "Check if request responses bad request.");
        logger.info("PASSED!");
    }

    @Test
    public void shouldChangeSelfPasswordTest() {
        auth(new LoginDTO(USERNAME_FOR_SUCCESS_REQUEST, PASSWORD));
        Response getUserResponse = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, getUserResponse.getStatusCode());
        AccountInfoDTO accountInfoDTO = getUserResponse.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeSelfPasswordDTO changeSelfPasswordDTO = new ChangeSelfPasswordDTO(PASSWORD, NEW_PASSWORD, NEW_PASSWORD, accountInfoDTO.getVersion());
        Response response = sendRequestAndGetResponse(Method.PATCH, URL_PASSWORD, changeSelfPasswordDTO, ContentType.JSON);
        assertEquals(204, response.getStatusCode(), "Check if request responses no content.");
        Response responseLogin = sendRequestAndGetResponse(Method.POST, "/accounts/login", new LoginDTO(USERNAME_FOR_SUCCESS_REQUEST, NEW_PASSWORD), ContentType.JSON);
        assertEquals(200, responseLogin.getStatusCode(), "Check if request responses ok.");
        logger.info("PASSED!");
    }

    private void initialize() {
        auth(new LoginDTO(USERNAME, PASSWORD));
    }
}
