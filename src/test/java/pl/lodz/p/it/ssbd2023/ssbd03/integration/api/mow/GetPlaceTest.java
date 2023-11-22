package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.BuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlaceInfoDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetPlaceTest extends BasicIntegrationConfigTest {
    private static final String URL_GET = "/places/place/0";

    @Test
    public void getPlaceAsManager() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(200, response.getStatusCode(), "check if request responses ok.");
        PlaceInfoDTO place = response.body().jsonPath().getObject("", PlaceInfoDTO.class);
        assertNotNull(place, "check if list is not empty");
    }

    @Test
    public void getPlaceAsOwner() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(200, response.getStatusCode(), "check if request responses ok.");
        PlaceInfoDTO place = response.body().jsonPath().getObject("", PlaceInfoDTO.class);
        assertNotNull(place, "check if list is not empty");
    }

    @Test
    public void getPlaceForbiddenAsAdmin() {
        auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(403, response.getStatusCode(), "check if request responses forbidden.");
    }
}
