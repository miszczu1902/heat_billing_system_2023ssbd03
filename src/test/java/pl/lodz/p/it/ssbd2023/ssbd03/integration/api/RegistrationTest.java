package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicE2EConfigTest;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationTest extends BasicE2EConfigTest {

    @Test
    public void registerOwner() {
        CreateOwnerDTO createOwnerDTO = new CreateOwnerDTO(
                "Bartosz",
                "Miszczak",
                "miszczu2137",
                "stary2111@gmail.com",
                "Password$123",
                "Password$123",
                "PL",
                "997654321"
        );

        int statusCode = sendRequestAndGetResponse(Method.POST, "/accounts/register", createOwnerDTO, ContentType.JSON)
                .getStatusCode();
        assertEquals(201, statusCode);

        logger.info("PASSED!");
    }

}
