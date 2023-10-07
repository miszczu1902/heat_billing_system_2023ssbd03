package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.YearReportDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.factory.IntegrationTestObjectsFactory;

import static org.junit.jupiter.api.Assertions.*;

public class GetAnnualBalanceTest extends BasicIntegrationConfigTest {
    private final LoginDTO manager = IntegrationTestObjectsFactory.credentialsToAuth(Account.MANAGER, Account.PASSWORD);
    private final LoginDTO owner = IntegrationTestObjectsFactory.credentialsToAuth(Account.OWNER, Account.PASSWORD);

    @Before
    public void beforeTest() {
        auth(owner);
    }

    @Test
    public void getAnnualBalanceForOwnerTest() {
        Response response = sendRequestAndGetResponse(Method.GET, "/balances/report/" + 0, null, null);
        YearReportDTO annualBalanceForYear = response.body().jsonPath().getObject("$", YearReportDTO.class);

        assertEquals(200, response.getStatusCode());
        assertEquals(2022, Integer.valueOf(annualBalanceForYear.getYear()));
    }

    @Test
    public void getAnnualBalanceForManagerTest() {
        auth(manager);
        Response response = sendRequestAndGetResponse(Method.GET, "/balances/report/" + 0, null, null);
        YearReportDTO annualBalanceForYear = response.body().jsonPath().getObject("$", YearReportDTO.class);

        assertEquals(200, response.getStatusCode());
        assertEquals(2022, Integer.valueOf(annualBalanceForYear.getYear()));
    }

    @Test
    public void tryToGetNotExistingAnnualBalance() {
        Response response = sendRequestAndGetResponse(Method.GET, "/balances/report/" + RandomUtils.nextInt(), null, null);
        assertEquals(404, response.getStatusCode());
    }
}
