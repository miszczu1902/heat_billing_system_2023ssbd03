package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class EnableAccountTest extends BasicIntegrationConfigTest {

    @Before
    public void initialize() {
        auth(new LoginDTO("johndoe", "Password$123"));
    }

    @Test
    public void shouldAdminEnableGivenUserAccount() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        JsonPath jsonPath = new JsonPath(getUserResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        VersionDTO versionDTO = new VersionDTO(version);

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/enable",
                versionDTO,
                ContentType.JSON);

        assertEquals(204, enableUser.getStatusCode());
    }

    @Test
    public void shouldNotAdminEnableSelfAccount() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/johndoe",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        JsonPath jsonPath = new JsonPath(getUserResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        VersionDTO versionDTO = new VersionDTO(version);

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/johndoe/enable",
                versionDTO,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
        assertEquals("Action is not allowed with this privileges", enableUser.getBody().print());
    }

    @Test
    public void shouldNotManagerEnableSelfAccount() {
        auth(new LoginDTO("janekowalski", "Password$123"));

        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        JsonPath jsonPath = new JsonPath(getUserResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        VersionDTO versionDTO = new VersionDTO(version);

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/enable",
                versionDTO,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
        assertEquals("Action is not allowed with this privileges", enableUser.getBody().print());
    }

    @Test
    public void shouldNotManagerEnableAdminAccount() {
        auth(new LoginDTO("janekowalski", "Password$123"));

        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/johndoe",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        JsonPath jsonPath = new JsonPath(getUserResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        VersionDTO versionDTO = new VersionDTO(version);

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/johndoe/enable",
                versionDTO,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
        assertEquals("Action is not allowed with this privileges", enableUser.getBody().print());
    }


    @Test
    public void shouldNotEnableWhenEtagNotMatch() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        JsonPath jsonPath = new JsonPath(getUserResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        VersionDTO versionDTO = new VersionDTO(version + 1);

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/enable",
                versionDTO,
                ContentType.JSON);

        assertEquals(409, enableUser.getStatusCode());
    }

}