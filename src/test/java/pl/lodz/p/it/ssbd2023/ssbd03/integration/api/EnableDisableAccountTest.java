package pl.lodz.p.it.ssbd2023.ssbd03.integration.api;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicE2EConfigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class EnableDisableAccountTest extends BasicE2EConfigTest {

    @Before
    public void initialize() {
        setETAG("");
        setBEARER_TOKEN("");
        login("johndoe", "Password$123");
    }

    @ParameterizedTest
    @ValueSource(strings = {"enable", "disable"})
    public void shouldAdminEnableDisableGivenUserAccount(String action) {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        setETAG(getUserResponse.header("ETag"));

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/" + action,
                null,
                ContentType.JSON);

        assertEquals(204, enableUser.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"enable", "disable"})
    public void shouldNotAdminEnableSelfAccount(String action) {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/johndoe",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        setETAG(getUserResponse.header("ETag"));

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/johndoe/" + action,
                null,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
        assertEquals("Action is not allowed with this privileges", enableUser.getBody().print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"enable", "disable"})
    public void shouldNotManagerEnableSelfAccount(String action) {
        login("janekowalski", "Password$123");

        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        setETAG(getUserResponse.header("ETag"));

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/" + action,
                null,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
        assertEquals("Action is not allowed with this privileges", enableUser.getBody().print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"enable", "disable"})
    public void shouldNotManagerEnableAdminAccount(String action) {
        login("janekowalski", "Password$123");

        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/johndoe",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        setETAG(getUserResponse.header("ETag"));

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/johndoe/" + action,
                null,
                ContentType.JSON);

        assertEquals(405, enableUser.getStatusCode());
        assertEquals("Action is not allowed with this privileges", enableUser.getBody().print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"enable", "disable"})
    public void shouldNotEnableNotExistingAccount(String action) {
        setETAG("etag");

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/kamiltumulec/" + action,
                null,
                ContentType.JSON);

        assertEquals(404, enableUser.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"enable", "disable"})
    public void shouldNotEnableWhenEtagNotMatch(String action) {
        Response getUserResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski",
                null,
                ContentType.JSON);

        assertEquals(200, getUserResponse.getStatusCode());

        setETAG("not matching ETag");

        Response enableUser = sendRequestAndGetResponse(Method.PATCH,
                "/accounts/janekowalski/" + action,
                null,
                ContentType.JSON);

        assertEquals(400, enableUser.getStatusCode());
    }

    private void login(String username, String passsword) {
        LoginDTO loginDTO =  new LoginDTO(
                username,
                passsword
        );

        Response logIn = sendRequestAndGetResponse(Method.POST,
                "/accounts/login",
                loginDTO,
                ContentType.JSON);

        setBEARER_TOKEN(logIn.header("Bearer"));
    }

}
