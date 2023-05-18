package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AdminDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ManagerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.OwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GetSelfInfoTest extends BasicIntegrationConfigTest {

    @Test
    public void getManagerTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self/manager", null, null);
        int statusCode = response.getStatusCode();
        ManagerDTO managerDTO = response.body().as(ManagerDTO.class);

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertEquals(managerDTO.getUsername(), Account.MANAGER, "Check if response contains self username.");
        assertNotEquals(managerDTO.getLicense(), null, "check if manager contains license");
        logger.info("PASSED!");
    }

    @Test
    public void getOwnerTest() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self/owner", null, null);
        int statusCode = response.getStatusCode();
        OwnerDTO ownerDTO = response.body().as(OwnerDTO.class);

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertEquals(ownerDTO.getUsername(), Account.OWNER, "Check if response contains self username.");
        assertNotEquals(ownerDTO.getPhoneNumber(), null, "check if owner contains phone number");
        logger.info("PASSED!");
    }

    @Test
    public void getAdminTest() {
        auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self/admin", null, null);
        int statusCode = response.getStatusCode();
        AdminDTO adminDTO = response.body().as(AdminDTO.class);

        assertEquals(200, statusCode, "Check if request responses ok.");
        assertEquals(adminDTO.getUsername(), Account.ADMIN, "Check if response contains self username.");
        logger.info("PASSED!");
    }

    @Test
    public void getManagerWhenUserIsntManagerTest() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self/manager", null, null);
        int statusCode = response.getStatusCode();

        assertEquals(403, statusCode, "Check if request responses forbidden.");
        logger.info("PASSED!");
    }

    @Test
    public void getOwnerWhenUserIsntOwnerTest() {
        auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self/owner", null, null);
        int statusCode = response.getStatusCode();

        assertEquals(403, statusCode, "Check if request responses forbidden.");
        logger.info("PASSED!");
    }

    @Test
    public void getAdminTestWhenUserIsntAdmin() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self/admin", null, null);
        int statusCode = response.getStatusCode();

        assertEquals(403, statusCode, "Check if request responses forbidden.");
        logger.info("PASSED!");
    }
}
