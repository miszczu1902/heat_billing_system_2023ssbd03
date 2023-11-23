package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mok;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangePhoneNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.OwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ChangePhoneNumberTest extends BasicIntegrationConfigTest {
    @Before
    public void initTest() {
        auth(new LoginDTO("mariasilva", "Password$123"));
    }

    @Test
    public void changePhoneNumberTest() {
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self/owner", null, null);
        OwnerDTO ownerDTO = response.body().jsonPath().getObject("", OwnerDTO.class);
        ChangePhoneNumberDTO newPhoneNumber = new ChangePhoneNumberDTO(RandomStringUtils.randomNumeric(9));
        newPhoneNumber.setVersion(ownerDTO.getVersion());

        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/phone-number", newPhoneNumber, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(204, statusCode, "Check if modification passed.");

        String actualPhoneNumber = sendRequestAndGetResponse(Method.GET, "/accounts/self/owner", null, null)
                .body().jsonPath().getObject("", OwnerDTO.class).getPhoneNumber();
        assertNotEquals(ownerDTO.getPhoneNumber(), actualPhoneNumber, "Compare phone numbers.");
    }

    @Test
    public void tryToChangeOnNotUniquePhoneNumberTest() {
        Response response = sendRequestAndGetResponse(Method.GET, "/accounts/self/owner", null, null);
        OwnerDTO ownerDTO = response.body().jsonPath().getObject("", OwnerDTO.class);
        ChangePhoneNumberDTO newPhoneNumber = new ChangePhoneNumberDTO(ownerDTO.getPhoneNumber());
        newPhoneNumber.setVersion(ownerDTO.getVersion());

        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/phone-number", newPhoneNumber, ContentType.JSON);
        int statusCode = response.getStatusCode();
        assertEquals(409, statusCode, "Check if modification failed.");

        newPhoneNumber.setPhoneNumber("123456789");

        sendRequestAndGetResponse(Method.GET, "/accounts/self/owner", null, null);
        response = sendRequestAndGetResponse(Method.PATCH, "/accounts/self/phone-number", newPhoneNumber, ContentType.JSON);
        statusCode = response.getStatusCode();
        assertEquals(409, statusCode, "Check if modification failed.");
    }
}
