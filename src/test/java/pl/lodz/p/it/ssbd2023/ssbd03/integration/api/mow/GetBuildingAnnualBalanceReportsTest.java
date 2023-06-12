package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AnnualBalanceToListDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetBuildingAnnualBalanceReportsTest extends BasicIntegrationConfigTest {
    private static final String URL_GET = "/balances/all-reports/0";

    @Test
    public void getBuildingAnnualBalanceReportsAsManager() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(200, response.getStatusCode(), "check if request responses ok.");
        List<AnnualBalanceToListDTO> listOfAnnualBalances = response.body().jsonPath().getList("", AnnualBalanceToListDTO.class).stream().toList();
        assertFalse(listOfAnnualBalances.isEmpty(), "check if list is not empty");
    }

    @Test
    public void getBuildingAnnualBalanceReportsForbiddenAsOwner() {
        auth(new LoginDTO(Account.OWNER2, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(403, response.getStatusCode(), "check if request responses forbidden.");
    }

    @Test
    public void getBuildingAnnualBalanceReportsForbiddenAsAdmin() {
        auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
        Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
        assertEquals(403, response.getStatusCode(), "check if request responses forbidden.");
    }
}
