package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.EditPersonalDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class EditPersonalDataTest extends BasicIntegrationConfigTest {

    @Before
    public void initialize() {
        auth(new LoginDTO("johndoe", "Password$123"));
    }

    @Test
    public void shouldChangeUserPersonalData() {
        Response getUserPersonalDataResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse.getStatusCode());

        JsonPath jsonPath = new JsonPath(getUserPersonalDataResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        EditPersonalDataDTO personalDataDTO = new EditPersonalDataDTO(version, "Jane", "Kowalska");

        Response changePersonalDataResponse = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/personal-data",
                personalDataDTO,
                ContentType.JSON);

        assertEquals(204, changePersonalDataResponse.getStatusCode());
    }

    @Test
    public void shouldChangeSelfPersonalData() {
        Response getUserPersonalDataResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/johndoe/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse.getStatusCode());

        JsonPath jsonPath = new JsonPath(getUserPersonalDataResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        EditPersonalDataDTO personalDataDTO = new EditPersonalDataDTO(version, "Jan", "Doe");

        Response changePersonalDataResponse = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/johndoe/personal-data",
                personalDataDTO,
                ContentType.JSON);

        assertEquals(204, changePersonalDataResponse.getStatusCode());
    }

    @Test
    public void shouldNotChangeUserPersonalDataNoDTO() {
        Response getUserPersonalDataResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse.getStatusCode());

        Response changePersonalDataResponse = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(400, changePersonalDataResponse.getStatusCode());
    }

    @Test
    public void shouldNotChangeUserPersonalDataNoValidDTO() {
        Response getUserPersonalDataResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse.getStatusCode());

        JsonPath jsonPath = new JsonPath(getUserPersonalDataResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        EditPersonalDataDTO personalDataDTO = new EditPersonalDataDTO(version,
                "JaneJaneJaneJaneJaneJaneJaneJaneJaneJaneJaneJaneJaneJaneJaneJane", "Kowalska");

        Response changePersonalDataResponse = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/personal-data",
                personalDataDTO,
                ContentType.JSON);

        assertEquals(400, changePersonalDataResponse.getStatusCode());
    }
}