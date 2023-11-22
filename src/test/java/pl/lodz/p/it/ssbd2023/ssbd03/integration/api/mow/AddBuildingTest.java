package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateBuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddBuildingTest extends BasicIntegrationConfigTest {

    @Before
    public void initTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
    }

    @Test
    public void shouldSuccessfullyAddBuildingTest() {
        CreateBuildingDTO createBuildingDTO = new CreateBuildingDTO(
                new BigDecimal("30.00"),
                "Ulica",
                "13A",
                "Miasto",
                "00-000");

        Response response = sendRequestAndGetResponse(Method.POST, "/buildings/building", createBuildingDTO,
                ContentType.JSON);

        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void shouldNotAddBuildingWithNegativeArea() {
        CreateBuildingDTO createBuildingDTO = new CreateBuildingDTO(
                new BigDecimal("-30.00"),
                "Ulica",
                "13A",
                "Miasto",
                "00-000");

        Response response = sendRequestAndGetResponse(Method.POST, "/buildings/building", createBuildingDTO,
                ContentType.JSON);

        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void shouldNotAddBuildingWithWrongPostalCodeFormat() {
        CreateBuildingDTO createBuildingDTO = new CreateBuildingDTO(
                new BigDecimal("-30.00"),
                "Ulica",
                "13A",
                "Miasto",
                "00-00A");

        Response response = sendRequestAndGetResponse(Method.POST, "/buildings/building", createBuildingDTO,
                ContentType.JSON);

        assertEquals(400, response.getStatusCode());
    }
}
