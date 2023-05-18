package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.*;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddRemoveAccessLevelFromAccountTest extends BasicIntegrationConfigTest {
    @Before
    public void initTest() {
        auth(new LoginDTO("johndoe", "Password$123"));
    }

    @Test
    public void addAndRemoveAdminAccessLevelToAccountTest() {
        AddAccessLevelAdminDTO accessLevelAdmin = new AddAccessLevelAdminDTO("mariasilva");
        sendRequestAndGetResponse(Method.GET, "/accounts/mariasilva", null, null);
        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/add-access-level-admin", accessLevelAdmin, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was added.");

        RevokeAccessLevelDTO accessLevelToRevoke = new RevokeAccessLevelDTO("mariasilva", Roles.ADMIN);
        sendRequestAndGetResponse(Method.GET, "/accounts/mariasilva", null, null);
        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/revoke-access-level", accessLevelToRevoke, ContentType.JSON);
        statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was revoked.");
    }

    @Test
    public void addAndRemoveManagerAccessLevelToAccountTest() {
        AddAccessLevelManagerDTO accessLevelAdmin = new AddAccessLevelManagerDTO("mariasilva", RandomStringUtils.randomNumeric(20));
        sendRequestAndGetResponse(Method.GET, "/accounts/mariasilva", null, null);
        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/add-access-level-manager", accessLevelAdmin, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was added.");

        RevokeAccessLevelDTO accessLevelToRevoke = new RevokeAccessLevelDTO("mariasilva", Roles.MANAGER);
        sendRequestAndGetResponse(Method.GET, "/accounts/mariasilva", null, null);
        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/revoke-access-level", accessLevelToRevoke, ContentType.JSON);
        statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was revoked.");
    }

    @Test
    public void addAndRemoveOwnerAccessLevelToAccountTest() {
        AddAccessLevelOwnerDTO accessLevelAdmin = new AddAccessLevelOwnerDTO("janekowalski", RandomStringUtils.randomNumeric(9));
        sendRequestAndGetResponse(Method.GET, "/accounts/janekowalski", null, null);
        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/add-access-level-owner", accessLevelAdmin, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was added.");

        RevokeAccessLevelDTO accessLevelToRevoke = new RevokeAccessLevelDTO("janekowalski", Roles.OWNER);
        sendRequestAndGetResponse(Method.GET, "/accounts/janekowalski", null, null);
        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/revoke-access-level", accessLevelToRevoke, ContentType.JSON);
        statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was revoked.");
    }
}
