package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangeEmailDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AccountInfoDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeSelfEmailTest extends BasicIntegrationConfigTest {

    @Test
    public void ChangeSelfEmailTest() {
        initialize();
        String email = RandomStringUtils.randomAlphanumeric(10) + "@fakemailik.com";
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self", null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        AccountInfoDTO accountInfoDTO = response.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeEmailDTO changeEmailDTO = new ChangeEmailDTO(email);
        changeEmailDTO.setVersion(accountInfoDTO.getVersion());

        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/email", changeEmailDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if modification passed.");
    }

    @Test
    public void ChangeSelfEmailNotUniqueTest() {
        initialize();
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self", null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        AccountInfoDTO accountInfoDTO = response.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeEmailDTO changeEmailDTO = new ChangeEmailDTO("janekowalski@example.com");
        changeEmailDTO.setVersion(accountInfoDTO.getVersion());

        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/email", changeEmailDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(409, statusCode, "Conflict.");
    }

    @Test
    public void ChangeSelfEmailNoEtagTest() {
        initialize();
        String email = RandomStringUtils.randomAlphanumeric(10) + "@fakemailik.com";
        ChangeEmailDTO changeEmailDTO = new ChangeEmailDTO(email);

        Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/email", changeEmailDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(500, statusCode, " no etag.");
    }

    @Test
    public void ChangeSelfEmailWrongVersionTest() {
        initialize();
        String email = RandomStringUtils.randomAlphanumeric(10) + "@fakemailik.com";
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self", null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        AccountInfoDTO accountInfoDTO = response.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeEmailDTO changeEmailDTO = new ChangeEmailDTO(email);
        changeEmailDTO.setVersion(accountInfoDTO.getVersion() + 1);

        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/email", changeEmailDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(409, statusCode, "Check if request responses conflict.");
    }

    private void initialize() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
    }
}
