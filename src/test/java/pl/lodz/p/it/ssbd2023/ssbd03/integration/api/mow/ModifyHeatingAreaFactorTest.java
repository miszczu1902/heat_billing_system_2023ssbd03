package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.InsertAdvanceChangeFactorDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.factory.IntegrationTestObjectsFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ModifyHeatingAreaFactorTest extends BasicIntegrationConfigTest {
    private final LoginDTO manager = IntegrationTestObjectsFactory.credentialsToAuth(Account.MANAGER, Account.PASSWORD);

    @Before
    public void beforeTest() {
        auth(manager);
    }

    @Test
    public void modifyHeatingAreaFactorTest() {
        boolean condition = LocalDate.now().getDayOfMonth() == 1 && LocalDate.now().minusMonths(3).isBefore(LocalDate.now());
        Assume.assumeTrue("That test could be passed when is the first day of year quarter is present", condition);
        Response response = sendRequestAndGetResponse(Method.PATCH,
                "/heat-distribution-centre/parameters/advance-change-factor/" + 0,
                new InsertAdvanceChangeFactorDTO(new BigDecimal(RandomUtils.nextInt(1, 9))), ContentType.JSON);
        assertEquals(204, response.getStatusCode());
    }

    @Test
    public void modifyHeatingAreaFactorWithIncorrectValueTest() {
        InsertAdvanceChangeFactorDTO insertAdvanceChangeFactorDTO = new InsertAdvanceChangeFactorDTO(new BigDecimal(10));
        insertAdvanceChangeFactorDTO.setVersion(-1L);
        Response response = sendRequestAndGetResponse(Method.PATCH,
                "/heat-distribution-centre/parameters/advance-change-factor/" + 0, insertAdvanceChangeFactorDTO, ContentType.JSON);
        assertEquals(500, response.getStatusCode());
    }
}
