package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.InsertHotWaterEntryDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ModifyHotWaterEntryDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.HotWaterEntryDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.factory.IntegrationTestObjectsFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InsertAndModifyHotWaterConsumptionTest extends BasicIntegrationConfigTest {
    private final LoginDTO owner = IntegrationTestObjectsFactory.credentialsToAuth(Account.OWNER, Account.PASSWORD);

    @Before
    public void beforeTest() {
        auth(owner);
    }

    @Test
    public void tryToInputIncorrectHotWaterEntryValueTest() {
        Response response = sendRequestAndGetResponse(
                Method.POST,
                "/heat-distribution-centre/parameters/insert-consumption",
                new InsertHotWaterEntryDTO(new BigDecimal(-4), 0L),
                ContentType.JSON);
        assertEquals(400, response.getStatusCode());

        response = sendRequestAndGetResponse(
                Method.POST,
                "/heat-distribution-centre/parameters/insert-consumption",
                new InsertHotWaterEntryDTO(new BigDecimal(0), 0L),
                ContentType.JSON);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void tryToInputIncorrectVersionFieldValueTest() {
        Response response = sendRequestAndGetResponse(
                Method.GET,
                "/heat-distribution-centre/hot-water-consumption/owner/0",
                null,
                null);
        HotWaterEntryDTO hotWaterEntry = response.body().jsonPath().getObject("$", HotWaterEntryDTO.class);

        response = sendRequestAndGetResponse(
                Method.PATCH,
                "/heat-distribution-centre/parameters/insert-consumption",
                new ModifyHotWaterEntryDTO(hotWaterEntry.getVersion() + 1, hotWaterEntry.getEntryValue(), 0L),
                ContentType.JSON);
        assertEquals(400, response.getStatusCode());
    }
}
