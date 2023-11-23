package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.BuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlaceDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetBuildingTest extends BasicIntegrationConfigTest {
    private static final String URL_GET = "/buildings/building/0";

    @Test
    public void getBuildingAsManager() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(200, response.getStatusCode(), "check if request responses ok.");
        BuildingDTO building = response.body().jsonPath().getObject("", BuildingDTO.class);
        assertNotNull(building, "check if list is not empty");
    }

    @Test
    public void getBuildingAsOwner() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(200, response.getStatusCode(), "check if request responses ok.");
        BuildingDTO building = response.body().jsonPath().getObject("", BuildingDTO.class);
        assertNotNull(building, "check if list is not empty");
    }

    @Test
    public void getBuildingForbiddenAsAdmin() {
        auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(403, response.getStatusCode(), "check if request responses forbidden.");
    }
}
