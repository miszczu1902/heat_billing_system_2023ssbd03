package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ModifyPlaceOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicIntegrationConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModifyPlaceOwnerTest extends BasicIntegrationConfigTest {

    @Before
    public void initTest() {
        auth(new LoginDTO(Account.MANAGER, Account.PASSWORD));
    }

    public int modifyPlaceOwner(ModifyPlaceOwnerDTO modifyPlaceOwnerDTO) {

        return sendRequestAndGetResponse(Method.PATCH, "/places/owner/0", modifyPlaceOwnerDTO, ContentType.JSON)
                .getStatusCode();
    }

    @Test
    public void modifyPlaceOwnerToTheSameOwnerTest() {
        ModifyPlaceOwnerDTO modifyPlaceOwnerDTO = new ModifyPlaceOwnerDTO(Account.OWNER);
        modifyPlaceOwner(modifyPlaceOwnerDTO);
        int statusCode = modifyPlaceOwner(modifyPlaceOwnerDTO);
        assertEquals(409, statusCode, "check conflict appeared");
    }

    @Test
    public void modifyPlaceOwnerToTheNewOwnerTest() {
        ModifyPlaceOwnerDTO modifyPlaceOwnerDTO = new ModifyPlaceOwnerDTO(Account.OWNER2);
        int statusCode = modifyPlaceOwner(modifyPlaceOwnerDTO);
        assertEquals(204, statusCode, "check if owner changed");
    }

    @Test
    public void modifyPlaceOwnerToYourselfTest() {
        ModifyPlaceOwnerDTO modifyPlaceOwnerDTO = new ModifyPlaceOwnerDTO(Account.MANAGER);
        int statusCode = modifyPlaceOwner(modifyPlaceOwnerDTO);
        assertEquals(403, statusCode, "check if forbidden appeared");
    }

    @Test
    public void modifyPlaceOwnerToAdminTest() {
        ModifyPlaceOwnerDTO modifyPlaceOwnerDTO = new ModifyPlaceOwnerDTO(Account.ADMIN);
        int statusCode = modifyPlaceOwner(modifyPlaceOwnerDTO);
        assertEquals(403, statusCode, "check if forbidden appeared");
    }
}
