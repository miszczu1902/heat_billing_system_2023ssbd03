package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.AddConsumptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddConsumptionFromInvoiceTest extends BasicIntegrationConfigTest {

    @Before
    public void initTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
    }

    public int addConsumption(AddConsumptionDTO addConsumptionDTO) {
        return sendRequestAndGetResponse(Method.POST, "/heat-distribution-centre/parameters/consumption-cost", addConsumptionDTO, ContentType.JSON)
                .getStatusCode();
    }

    @Test
    public void addConsumptionWithTooHighConsumptionTest() {
        AddConsumptionDTO addConsumptionDTO = new AddConsumptionDTO(new BigDecimal("123123123123"), new BigDecimal("300"), new BigDecimal("1"));
        int statusCode = addConsumption(addConsumptionDTO);
        assertEquals(400, statusCode, "check if error appears when consumption is too high");
    }

    @Test
    public void addConsumptionWithTooHighConsumptionCostTest() {
        AddConsumptionDTO addConsumptionDTO = new AddConsumptionDTO(new BigDecimal("12"), new BigDecimal("123123123123123123123123"), new BigDecimal("1"));
        int statusCode = addConsumption(addConsumptionDTO);
        assertEquals(400, statusCode, "check if error appears when consumption cost is too high");
    }

    @Test
    public void addConsumptionWithTooHighHeatingAreaFactorTest() {
        AddConsumptionDTO addConsumptionDTO = new AddConsumptionDTO(new BigDecimal("12"), new BigDecimal("123"), new BigDecimal("2"));
        int statusCode = addConsumption(addConsumptionDTO);
        assertEquals(400, statusCode, "check if error appears when heating area factor is too high");
    }

    @Test
    public void addConsumptionSuccessfullAndTryToOverwritteTest() {
        AddConsumptionDTO addConsumptionDTO = new AddConsumptionDTO(new BigDecimal("15"), new BigDecimal("300"), new BigDecimal("1"));
        int statusCode = addConsumption(addConsumptionDTO);
        assertEquals(201, statusCode, "check if value was added");

        statusCode = addConsumption(addConsumptionDTO);
        assertEquals(403, statusCode, "check if error appeared");
    }

    @Test
    public void addConsumptionAsOwnerTest() {
        auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
        AddConsumptionDTO addConsumptionDTO = new AddConsumptionDTO(new BigDecimal("15"), new BigDecimal("300"), new BigDecimal("1"));
        int statusCode = addConsumption(addConsumptionDTO);
        assertEquals(403, statusCode, "check if error appeared");
    }


}
