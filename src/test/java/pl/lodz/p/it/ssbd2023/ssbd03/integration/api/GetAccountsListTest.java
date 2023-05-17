package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AccountForListDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.factory.IntegrationTestObjectsFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetAccountsListTest extends BasicIntegrationConfigTest {
    @Before
    public void initTest() {
        auth(new LoginDTO("johndoe", "Password$123"));
    }

    @Test
    public void getListOfAccountsTest() {
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts", null, null);
        int statusCode = response.getStatusCode();
        List<String> listOfAccounts = response.body().jsonPath().getList("", AccountForListDTO.class).stream()
                .map(AccountForListDTO::getUsername).toList();

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertTrue(listOfAccounts.contains("johndoe"), "Check if list size equals default amount of accounts.");
        logger.info("PASSED!");
    }

    @Test
    public void getListOfAccountsByOwnerTest() {
        auth(new LoginDTO("janekowalski", "Password$123"));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts", null, null);
        int statusCode = response.getStatusCode();
        List<String> listOfAccounts = response.body().jsonPath().getList("", AccountForListDTO.class).stream()
                .map(AccountForListDTO::getUsername).toList();

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertTrue(listOfAccounts.contains("janekowalski"), "Check if list size equals default amount of accounts.");
        logger.info("PASSED!");
    }

    @Test
    public void registerNewAccountAndGetListOfAccountsTest() {
        CreateOwnerDTO owner = IntegrationTestObjectsFactory.createAccountToRegister();
        int statusCode = sendRequestAndGetResponse(Method.POST, "/accounts/register", owner, ContentType.JSON)
                .getStatusCode();
        assertEquals(201, statusCode, "Check if account was registered");
        logger.info("Owner registered.");

        Response response = sendRequestAndGetResponse(Method.GET, "/accounts", null, null);
        statusCode = response.getStatusCode();
        List<String> listOfAccounts = response.body().jsonPath().getList("", AccountForListDTO.class).stream()
                .map(AccountForListDTO::getUsername).toList();

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertTrue(listOfAccounts.contains(owner.getUsername()), "Check if list contains new registered account");

        logger.info("PASSED!");
    }
}
