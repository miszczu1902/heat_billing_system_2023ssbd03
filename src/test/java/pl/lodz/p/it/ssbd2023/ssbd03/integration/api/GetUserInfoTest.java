package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AccountInfoDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetUserInfoTest extends BasicIntegrationConfigTest {

    @Test
    public void getUserAsAdminTest() {
        auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.OWNER, null, null);
        int statusCode = response.getStatusCode();
        AccountInfoDTO accountInfoDTO = response.body().as(AccountInfoDTO.class);

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertEquals(accountInfoDTO.getUsername(), Account.OWNER, "Check if response contains username from request.");
        logger.info("PASSED!");
    }

    @Test
    public void getUserThatNotExistsAsAdminTest() {
        auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.OWNER + "a", null, null);
        int statusCode = response.getStatusCode();

        assertEquals(404, statusCode, "Check if request responses not found.");
    }

    @Test
    public void getUserAsManagerTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.OWNER, null, null);
        int statusCode = response.getStatusCode();
        AccountInfoDTO accountInfoDTO = response.body().as(AccountInfoDTO.class);

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertEquals(accountInfoDTO.getUsername(), Account.OWNER, "Check if response contains username from request.");
        logger.info("PASSED!");
    }

    @Test
    public void getUserThatNotExistsAsManagerTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.OWNER + "a", null, null);
        int statusCode = response.getStatusCode();

        assertEquals(404, statusCode, "Check if request responses not found.");
    }

    @Test
    public void getUserAsOwnerTest() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.OWNER, null, null);
        int statusCode = response.getStatusCode();

        assertEquals(403, statusCode, "Check if request responses forbidden.");
    }

    @Test
    public void getUserThatNotExistsAsUserTest() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.OWNER + "a", null, null);
        int statusCode = response.getStatusCode();

        assertEquals(403, statusCode, "Check if request responses forbidden.");
    }
}
