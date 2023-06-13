package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.mow;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.HeatDistributionCentrePayoffDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetHeatDistributionCentreParametersTest extends BasicIntegrationConfigTest {
    private static final String URL_GET = "/heat-distribution-centre/parameters";

        @Test
        public void getHeatDistributionCentrePayoffsAsManager() {
            auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
            Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
            assertEquals(200, response.getStatusCode(), "check if request responses ok.");
            List<HeatDistributionCentrePayoffDTO> listHeatDistributionCentrePayoffs = response.body().jsonPath().getList("", HeatDistributionCentrePayoffDTO.class).stream().toList();
            assertFalse(listHeatDistributionCentrePayoffs.isEmpty(), "check if list is not empty");
        }

        @Test
        public void getHeatDistributionCentrePayoffsForbiddenAsOwner() {
            auth(new LoginDTO(Account.OWNER, Account.PASSWORD));
            Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
            assertEquals(403, response.getStatusCode(), "check if request responses forbidden.");
        }

        @Test
        public void getHeatDistributionCentrePayoffsForbiddenAsAdmin() {
            auth(new LoginDTO(Account.ADMIN, Account.PASSWORD));
            Response response = sendRequestAndGetResponse(Method.GET, URL_GET, null, null);
            assertEquals(403, response.getStatusCode(), "check if request responses forbidden.");
        }
}
