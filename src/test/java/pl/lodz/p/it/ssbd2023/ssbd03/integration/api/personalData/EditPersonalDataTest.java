package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.personalData;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.EditPersonalDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicE2EConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class EditPersonalDataTest extends BasicE2EConfigTest {

    @Before
    public void initialize() {
        setETAG("");
        setBEARER_TOKEN("");
        auth("johndoe", "Password$123");
    }

    @Test
    public void shouldChangeUserPersonalData() {
        Response getUserPersonalDataResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse.getStatusCode());

        setETAG(getUserPersonalDataResponse.header("ETag"));

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

        setETAG(getUserPersonalDataResponse.header("ETag"));

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
    public void shouldNotChangeSelfPersonalDataNotMatchingETag() {
        Response getUserPersonalDataResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse.getStatusCode());

        setETAG("");

        JsonPath jsonPath = new JsonPath(getUserPersonalDataResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        EditPersonalDataDTO personalDataDTO = new EditPersonalDataDTO(version, "Jane", "Kowalska");

        Response changePersonalDataResponse = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/personal-data",
                personalDataDTO,
                ContentType.JSON);

        assertEquals(500, changePersonalDataResponse.getStatusCode());
    }

    @Test
    public void shouldNotManagerChangeAdminPersonalData() {
        Response getUserPersonalDataResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse.getStatusCode());

        setETAG("");

        JsonPath jsonPath = new JsonPath(getUserPersonalDataResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        EditPersonalDataDTO personalDataDTO = new EditPersonalDataDTO(version, "Jane", "Kowalska");

        Response changePersonalDataResponse = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/personal-data",
                personalDataDTO,
                ContentType.JSON);

        assertEquals(500, changePersonalDataResponse.getStatusCode());
    }

    @Test
    public void shouldNotChangeUserPersonalDataNoDTO() {
        Response getUserPersonalDataResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse.getStatusCode());

        setETAG(getUserPersonalDataResponse.header("ETag"));


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

        setETAG(getUserPersonalDataResponse.header("ETag"));

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