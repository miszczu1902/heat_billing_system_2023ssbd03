package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ModifyPlaceOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlaceInfoDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModifyPlaceOwnerTest extends BasicIntegrationConfigTest {
    private static final String URL_GET = "/places/place/0";
    private static final String URL_PATCH = "/places/owner/0";

    @Before
    public void initTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
    }

    public int modifyPlaceOwner(ModifyPlaceOwnerDTO modifyPlaceOwnerDTO) {
        return sendRequestAndGetResponse(Method.PATCH, URL_PATCH, modifyPlaceOwnerDTO, ContentType.JSON)
                .getStatusCode();
    }

    @Test
    public void modifyPlaceOwnerToTheSameOwnerTest() {
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        PlaceInfoDTO placeInfoDTO = response.body().jsonPath().getObject("", PlaceInfoDTO.class);
        ModifyPlaceOwnerDTO modifyPlaceOwnerDTO = new ModifyPlaceOwnerDTO(placeInfoDTO.getUsername());
        modifyPlaceOwnerDTO.setVersion(placeInfoDTO.getVersion());
        int statusCode = modifyPlaceOwner(modifyPlaceOwnerDTO);
        assertEquals(409, statusCode, "check conflict appeared");
    }

    @Test
    public void modifyPlaceOwnerToTheNewOwnerTest() {
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        PlaceInfoDTO placeInfoDTO = response.body().jsonPath().getObject("", PlaceInfoDTO.class);
        ModifyPlaceOwnerDTO modifyPlaceOwnerDTO = new ModifyPlaceOwnerDTO(Account.OWNER2);
        modifyPlaceOwnerDTO.setVersion(placeInfoDTO.getVersion());
        int statusCode = modifyPlaceOwner(modifyPlaceOwnerDTO);
        assertEquals(204, statusCode, "check if owner changed");
    }

    @Test
    public void modifyPlaceOwnerWrongVersionTest() {
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        PlaceInfoDTO placeInfoDTO = response.body().jsonPath().getObject("", PlaceInfoDTO.class);
        ModifyPlaceOwnerDTO modifyPlaceOwnerDTO = new ModifyPlaceOwnerDTO(Account.OWNER);
        modifyPlaceOwnerDTO.setVersion(placeInfoDTO.getVersion() + 1);
        int statusCode = modifyPlaceOwner(modifyPlaceOwnerDTO);
        assertEquals(409, statusCode, "check if conflict appeared");
    }

    @Test
    public void modifyPlaceOwnerToYourselfTest() {
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        PlaceInfoDTO placeInfoDTO = response.body().jsonPath().getObject("", PlaceInfoDTO.class);
        ModifyPlaceOwnerDTO modifyPlaceOwnerDTO = new ModifyPlaceOwnerDTO(Account.MANAGER);
        modifyPlaceOwnerDTO.setVersion(placeInfoDTO.getVersion());
        int statusCode = modifyPlaceOwner(modifyPlaceOwnerDTO);
        assertEquals(403, statusCode, "check if forbidden appeared");
    }

    @Test
    public void modifyPlaceOwnerToAdminTest() {
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        PlaceInfoDTO placeInfoDTO = response.body().jsonPath().getObject("", PlaceInfoDTO.class);
        ModifyPlaceOwnerDTO modifyPlaceOwnerDTO = new ModifyPlaceOwnerDTO(Account.ADMIN);
        modifyPlaceOwnerDTO.setVersion(placeInfoDTO.getVersion());
        int statusCode = modifyPlaceOwner(modifyPlaceOwnerDTO);
        assertEquals(403, statusCode, "check if forbidden appeared");
    }
}
