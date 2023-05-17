package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.account;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicE2EConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class DisableEnableAccountTest extends BasicE2EConfigTest {
    @Before
    public void initialize() {
        setETAG("");
        setBEARER_TOKEN("");
        auth("johndoe", "Password$123");
    }

    @Test
    public void shouldNotAdminDisableSelfAccount() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/johndoe",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        setETAG(getUserResponse.header("ETag"));

        JsonPath jsonPath = new JsonPath(getUserResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        VersionDTO versionDTO = new VersionDTO(version);

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/johndoe/disable",
                versionDTO,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
        assertEquals("Action is not allowed with this privileges", enableUser.getBody().print());
    }

    @Test
    public void shouldNotManagerDisableSelfAccount() {
        auth("janekowalski", "Password$123");

        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        setETAG(getUserResponse.header("ETag"));

        JsonPath jsonPath = new JsonPath(getUserResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        VersionDTO versionDTO = new VersionDTO(version);

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/disable",
                versionDTO,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
        assertEquals("Action is not allowed with this privileges", enableUser.getBody().print());
    }

    @Test
    public void shouldNotManagerDisableAdminAccount() {
        auth("janekowalski", "Password$123");

        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/johndoe",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        setETAG(getUserResponse.header("ETag"));

        JsonPath jsonPath = new JsonPath(getUserResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        VersionDTO versionDTO = new VersionDTO(version);

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/johndoe/disable",
                versionDTO,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
        assertEquals("Action is not allowed with this privileges", enableUser.getBody().print());
    }


    @Test
    public void shouldNotDisableWhenEtagNotMatch() {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        setETAG(getUserResponse.header("ETag"));

        JsonPath jsonPath = new JsonPath(getUserResponse.getBody().asString());
        int version = jsonPath.getInt("version");
        VersionDTO versionDTO = new VersionDTO(version+1);

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/disable",
                versionDTO,
                ContentType.JSON);

        assertEquals(409, enableUser.getStatusCode());
    }
}