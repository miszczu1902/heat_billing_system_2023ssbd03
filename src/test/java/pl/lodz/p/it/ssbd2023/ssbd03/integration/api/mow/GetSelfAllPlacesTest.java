package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AnnualBalanceToListDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlacesListDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetSelfAllPlacesTest extends BasicIntegrationConfigTest {
    private static final String URL_GET = "/places/self";

    @Test
    public void getSelfAllPlacesTestAsOwner() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(200, response.getStatusCode(), "check if request responses ok.");
        List<PlacesListDTO> listOfSelfPlaces = response.body().jsonPath().getList("", PlacesListDTO.class).stream().toList();
        assertFalse(listOfSelfPlaces.isEmpty(), "check if list is not empty");
    }

    @Test
    public void getSelfAllPlacesTestForbiddenAsManager() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(403, response.getStatusCode(), "check if request responses forbidden.");
    }

    @Test
    public void getSelfAllPlacesTestForbiddenAsAdmin() {
        auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(403, response.getStatusCode(), "check if request responses forbidden.");
    }
}
