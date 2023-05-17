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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddRemoveAccessLevelFromAccountTest extends BasicIntegrationConfigTest {
    @Before
    public void initTest() {
        auth(new LoginDTO("johndoe", "Password$123"));
    }

    @Test
    public void addAndRemoveAdminAccessLevelToAccountTest() {
        AddAccessLevelAdminDTO accessLevelAdmin = new AddAccessLevelAdminDTO("mariasilva");
        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/add-access-level-admin", accessLevelAdmin, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was added.");

        RevokeAccessLevelDTO accessLevelToRevoke = new RevokeAccessLevelDTO("mariasilva", Roles.ADMIN);
        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/revoke-access-level", accessLevelToRevoke, ContentType.JSON);
        statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was revoked.");
    }

    @Test
    public void addAndRemoveManagerAccessLevelToAccountTest() {
        AddAccessLevelManagerDTO accessLevelAdmin = new AddAccessLevelManagerDTO("mariasilva", RandomStringUtils.randomNumeric(20));
        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/add-access-level-manager", accessLevelAdmin, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was added.");

        RevokeAccessLevelDTO accessLevelToRevoke = new RevokeAccessLevelDTO("mariasilva", Roles.MANAGER);
        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/revoke-access-level", accessLevelToRevoke, ContentType.JSON);
        statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was revoked.");
    }

    @Test
    public void addAndRemoveOwnerAccessLevelToAccountTest() {
        AddAccessLevelOwnerDTO accessLevelAdmin = new AddAccessLevelOwnerDTO("janekowalski", RandomStringUtils.randomNumeric(9));
        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/add-access-level-owner", accessLevelAdmin, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was added.");

        RevokeAccessLevelDTO accessLevelToRevoke = new RevokeAccessLevelDTO("janekowalski", Roles.OWNER);
        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/revoke-access-level", accessLevelToRevoke, ContentType.JSON);
        statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if access level was revoked.");
    }

    @Test
    public void addAndRemoveAdminAccessLevelToAccountConcurrent() {
        List<Integer> responseList = new ArrayList<>();
        int concurrentRequests = 2;

        ExecutorService addAccessLevelExecutorService = Executors.newFixedThreadPool(concurrentRequests);
        AddAccessLevelAdminDTO accessLevelAdmin = new AddAccessLevelAdminDTO("mariasilva");
        for (int i = 0; i < concurrentRequests; i++) {
            int index = i + 1;
            addAccessLevelExecutorService.execute(() -> {
                logger.info("Request no. " + index);
                Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/add-access-level-admin", accessLevelAdmin, ContentType.JSON);
                responseList.add(response.getStatusCode());
            });
        }
        try {
            addAccessLevelExecutorService.shutdown();
            addAccessLevelExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            assertTrue(responseList.contains(204), "Check if the first of request was passed.");
            assertTrue(responseList.contains(500), "Check if the rest of requests was failed.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ExecutorService revokeAccessLevelExecutorService = Executors.newFixedThreadPool(concurrentRequests);
        RevokeAccessLevelDTO accessLevelToRevoke = new RevokeAccessLevelDTO("mariasilva", Roles.ADMIN);
        for (int i = 0; i < concurrentRequests; i++) {
            int index = i + 1;
            revokeAccessLevelExecutorService.execute(() -> {
                logger.info("Request no. " + index);
                Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/revoke-access-level", accessLevelToRevoke, ContentType.JSON);
                responseList.add(response.getStatusCode());
            });
        }
        try {
            revokeAccessLevelExecutorService.shutdown();
            revokeAccessLevelExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            assertTrue(responseList.contains(204), "Check if the first of request was passed.");
            assertTrue(responseList.contains(500), "Check if the rest of requests was failed.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
