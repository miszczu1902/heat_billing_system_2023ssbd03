package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mok;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisableAccountTest extends BasicIntegrationConfigTest {
    @Before
    public void initialize() {
        auth(new LoginDTO("johndoe", "Password$123"));
    }

    @Test
    public void shouldNotAdminDisableSelfAccount() {
        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/johndoe/disable",
                null,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
    }

    @Test
    public void shouldAdminDisableManagerAccount() {
        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/disable",
                null,
                ContentType.JSON);

        assertEquals(204, enableUser.getStatusCode());
    }

    @Test
    public void shouldAdminDisableOwnerAccount() {
        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/mariasilva/disable",
                null,
                ContentType.JSON);

        assertEquals(204, enableUser.getStatusCode());
    }
}