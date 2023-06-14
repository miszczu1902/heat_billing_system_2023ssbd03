package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ModifyPlaceDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModifyPlaceTest extends BasicIntegrationConfigTest {

    @Before
    public void initTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
    }


    @Test
    public void shouldNotModifyPlaceWithNegativeArea() {
        Response response = sendRequestAndGetResponse(Method.GET, "/places/place/0",
                null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        ModifyPlaceDTO modifyPlaceDTO = new ModifyPlaceDTO(new BigDecimal("-20.00"));
        modifyPlaceDTO.setVersion(Long.valueOf(response.body().jsonPath().get("version").toString()));
        Response modifyResponse = sendRequestAndGetResponse(Method.PATCH, "/places/place/0",
                modifyPlaceDTO, ContentType.JSON);

        assertEquals(400, modifyResponse.getStatusCode());
    }

    @Test
    public void shouldNotModifyPlaceWithWrongVersion() {
        Response response = sendRequestAndGetResponse(Method.GET, "/places/place/0",
                null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        ModifyPlaceDTO modifyPlaceDTO = new ModifyPlaceDTO(new BigDecimal("20.00"));
        modifyPlaceDTO.setVersion((long) (Long.valueOf(response.body().jsonPath().get("version").toString()) + 1.0));
        Response modifyResponse = sendRequestAndGetResponse(Method.PATCH, "/places/place/0",
                modifyPlaceDTO, ContentType.JSON);

        assertEquals(409, modifyResponse.getStatusCode());
    }
}
