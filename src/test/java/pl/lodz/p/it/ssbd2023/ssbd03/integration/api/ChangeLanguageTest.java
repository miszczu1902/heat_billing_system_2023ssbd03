package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangeLanguageDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ManagerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeLanguageTest extends BasicIntegrationConfigTest {

    @Test
    public void ChangeLanguageToPlTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/language", new ChangeLanguageDTO("PL"), ContentType.JSON);
        int statusCode = response.getStatusCode();

        assertEquals(204, statusCode, "Check if request responses no content.");

        Response responseGet = sendRequestAndGetResponse(Method.GET, "/accounts/self/manager", null, null);
        statusCode = responseGet.getStatusCode();
        ManagerDTO managerDTO = responseGet.body().as(ManagerDTO.class);

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertEquals(managerDTO.getUsername(), Account.MANAGER, "Check if response contains self username.");
        assertEquals(managerDTO.getLanguage(), "PL", "check if manager contains proper language settings");

        logger.info("PASSED!");
    }

    @Test
    public void ChangeLanguageToEnTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/language", new ChangeLanguageDTO("EN"), ContentType.JSON);
        int statusCode = response.getStatusCode();

        assertEquals(204, statusCode, "Check if request responses no content.");

        Response responseGet = sendRequestAndGetResponse(Method.GET, "/accounts/self/manager", null, null);
        statusCode = responseGet.getStatusCode();
        ManagerDTO managerDTO = responseGet.body().as(ManagerDTO.class);

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertEquals(managerDTO.getUsername(), Account.MANAGER, "Check if response contains self username.");
        assertEquals(managerDTO.getLanguage(), "EN", "check if manager contains proper language settings");

        logger.info("PASSED!");
    }

    @Test
    public void ChangeLanguageToInvalidDataTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/language", new ChangeLanguageDTO("FRENCH"), ContentType.JSON);
        int statusCode = response.getStatusCode();

        assertEquals(400, statusCode, "Check if request responses bad request.");

        logger.info("PASSED!");
    }
}
