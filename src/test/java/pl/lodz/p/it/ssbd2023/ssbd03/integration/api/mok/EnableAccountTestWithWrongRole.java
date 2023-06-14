package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mok;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnableAccountTestWithWrongRole extends BasicIntegrationConfigTest {

    @Before
    public void initialize() {
        auth(new LoginDTO("janekowalski", "Password$123"));
    }

    @Test
    public void shouldNotManagerEnableSelfAccount() {
        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/enable",
                null,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
    }

    @Test
    public void shouldNotManagerEnableAdminAccount() {
        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/johndoe/enable",
                null,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
    }
}