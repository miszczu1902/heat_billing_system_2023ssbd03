package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.EnterPredictedHotWaterConsumptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnterPredictedHotWaterConsumptionTest extends BasicIntegrationConfigTest {

    @Before
    public void initTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
    }

    @Test
    public void shouldNotEnterPredictedHotWaterConsumptionWithNegativeConsumption() {
        Response response = sendRequestAndGetResponse(Method.GET, "/places/place/0",
                null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        EnterPredictedHotWaterConsumptionDTO enterPredictedHotWaterConsumptionDTO =
                new EnterPredictedHotWaterConsumptionDTO(
                        response.body().jsonPath().get("version"),
                        new BigDecimal("-20.00")
                );
        Response predictedHotWaterResponse = sendRequestAndGetResponse(Method.PATCH,
                "/places/place/0/predicted-hot-water-consumption",
                enterPredictedHotWaterConsumptionDTO,
                ContentType.JSON);

        assertEquals(400, predictedHotWaterResponse.getStatusCode());
    }

    @Test
    public void shouldNotEnterPredictedHotWaterConsumptionWithWrongVersion() {
        Response response = sendRequestAndGetResponse(Method.GET, "/places/place/0",
                null, ContentType.JSON);
        assertEquals(200, response.getStatusCode());
        EnterPredictedHotWaterConsumptionDTO enterPredictedHotWaterConsumptionDTO =
                new EnterPredictedHotWaterConsumptionDTO(
                        -1,
                        new BigDecimal("-20.00")
                );
        Response predictedHotWaterResponse = sendRequestAndGetResponse(Method.PATCH,
                "/places/place/0/predicted-hot-water-consumption",
                enterPredictedHotWaterConsumptionDTO,
                ContentType.JSON);

        assertEquals(400, predictedHotWaterResponse.getStatusCode());
    }
}
