package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.AddPlaceToBuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.BuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddPlaceTest extends BasicIntegrationConfigTest {
    private static final String URL_GET = "/buildings/building/0";
    private static final String URL_POST = "/buildings/place";

    @Before
    public void initTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
    }

    @Test
    public void shouldSuccessfullyAddPlaaceTest() {
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        BuildingDTO buildingDTO = response.body().jsonPath().getObject("", BuildingDTO.class);
        AddPlaceToBuildingDTO addPlaceToBuildingDTO = new AddPlaceToBuildingDTO(
                new BigDecimal("80.00"),
                true,
                new BigDecimal("50.00"),
                (long) 0,
                (long) -2);
        addPlaceToBuildingDTO.setVersion(buildingDTO.getVersion());
        response = sendRequestAndGetResponse(Method.POST, URL_POST, addPlaceToBuildingDTO,
                ContentType.JSON);

        assertEquals(204, response.getStatusCode());
    }

    @Test
    public void buildingLackOfSpaceTest() {
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        BuildingDTO buildingDTO = response.body().jsonPath().getObject("", BuildingDTO.class);
        AddPlaceToBuildingDTO addPlaceToBuildingDTO = new AddPlaceToBuildingDTO(
                new BigDecimal("1000.00"),
                true,
                new BigDecimal("50.00"),
                (long) 0,
                (long) -2);
        addPlaceToBuildingDTO.setVersion(buildingDTO.getVersion());
        response = sendRequestAndGetResponse(Method.POST, URL_POST, addPlaceToBuildingDTO,
                ContentType.JSON);

        assertEquals(400, response.getStatusCode());
    }
}
