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

public class ChangeUserEmailTest extends BasicIntegrationConfigTest {

    @Test
    public void ChangeUserEmailTest() {
        initialize();
        String email = RandomStringUtils.randomAlphanumeric(10) + "@fakemailik.com";
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.OWNER, null, ContentType.JSON);
        AccountInfoDTO accountInfoDTO = response.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeEmailDTO changeEmailDTO = new ChangeEmailDTO(email);
        changeEmailDTO.setVersion(accountInfoDTO.getVersion());

        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/" + Account.OWNER + "/email", changeEmailDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if modification passed.");
    }

    @Test
    public void ChangeUserEmailNotUniqueTest() {
        initialize();
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.OWNER, null, ContentType.JSON);
        AccountInfoDTO accountInfoDTO = response.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeEmailDTO changeEmailDTO = new ChangeEmailDTO("janekowalski@example.com");
        changeEmailDTO.setVersion(accountInfoDTO.getVersion());

        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/" + Account.OWNER + "/email", changeEmailDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(409, statusCode, "Conflict.");
    }

    @Test
    public void ChangeUserEmailWrongEtagTest() {
        initialize();
        String email = RandomStringUtils.randomAlphanumeric(10) + "@fakemailik.com";
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.MANAGER, null, ContentType.JSON);
        AccountInfoDTO accountInfoDTO = response.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeEmailDTO changeEmailDTO = new ChangeEmailDTO(email);
        changeEmailDTO.setVersion(accountInfoDTO.getVersion());


        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/" + Account.OWNER + "/email", changeEmailDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode, " etag error.");
    }

    @Test
    public void ChangeUserEmailWrongVersionTest() {
        initialize();
        String email = RandomStringUtils.randomAlphanumeric(10) + "@fakemailik.com";
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/" + Account.OWNER, null, ContentType.JSON);
        AccountInfoDTO accountInfoDTO = response.body().jsonPath().getObject("", AccountInfoDTO.class);
        ChangeEmailDTO changeEmailDTO = new ChangeEmailDTO(email);
        changeEmailDTO.setVersion(accountInfoDTO.getVersion() + 1);

        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/" + Account.OWNER + "/email", changeEmailDTO, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(409, statusCode, "Check if request responses conflict.");
    }

    private void initialize() {
        auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
    }
}
