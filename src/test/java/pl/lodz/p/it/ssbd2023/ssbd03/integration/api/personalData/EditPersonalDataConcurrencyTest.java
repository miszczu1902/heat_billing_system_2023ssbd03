package pl.lodz.p.it.ssbd2023.ssbd03.integration.api.personalData;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.EditPersonalDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.integration.config.BasicE2EConfigTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class EditPersonalDataConcurrencyTest extends BasicE2EConfigTest {

    @Before
    public void initialize() {
        setETAG("");
        setBEARER_TOKEN("");
        auth("johndoe", "Password$123");
    }

    @Test
    public void shouldOnlyOneFromThreeEditSelfPersonalDataConcurrent() {
        List<Integer> responseList = new ArrayList<>();
        int concurrentRequests = 2;

        Response getUserPersonalDataResponse = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse.getStatusCode());

        setETAG(getUserPersonalDataResponse.header("ETag"));

        JsonPath jsonPath = new JsonPath(getUserPersonalDataResponse.getBody().asString());
        int version = jsonPath.getInt("version");

        ExecutorService editPersonalDataExecutorService = Executors.newFixedThreadPool(concurrentRequests);
        EditPersonalDataDTO personalDataDTO = new EditPersonalDataDTO(version,
                "Jane", "Kowalska");
        for (int i = 0; i < concurrentRequests; i++) {
            int index = i + 1;
            editPersonalDataExecutorService.execute(() -> {
                logger.info("Request no. " + index);
                Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/janekowalski/personal-data",
                        personalDataDTO, ContentType.JSON);
                responseList.add(response.getStatusCode());
            });
        }
        try {
            editPersonalDataExecutorService.shutdown();
            editPersonalDataExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            assertTrue(responseList.contains(204), "Check if the first of request was passed.");
            assertTrue(responseList.contains(409), "Check if the rest of requests was failed.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Response getUserPersonalDataResponse2 = sendRequestAndGetResponse(Method.GET,
                "/accounts/janekowalski/personal-data",
                null,
                ContentType.JSON);

        assertEquals(200, getUserPersonalDataResponse2.getStatusCode());

        setETAG(getUserPersonalDataResponse2.header("ETag"));

        JsonPath jsonPath2 = new JsonPath(getUserPersonalDataResponse2.getBody().asString());
        int version2 = jsonPath2.getInt("version");

        ExecutorService editPersonalDataExecutorService2 = Executors.newFixedThreadPool(concurrentRequests);
        EditPersonalDataDTO editPersonalDataDTO = new EditPersonalDataDTO(version2, "Ja", "Kowalska");
        for (int i = 0; i < concurrentRequests; i++) {
            int index = i + 1;
            editPersonalDataExecutorService2.execute(() -> {
                logger.info("Request no. " + index);
                Response response = sendRequestAndGetResponse(Method.PATCH, "/accounts/janekowalski/personal-data",
                        editPersonalDataDTO, ContentType.JSON);
                responseList.add(response.getStatusCode());
            });
        }
        try {
            editPersonalDataExecutorService2.shutdown();
            editPersonalDataExecutorService2.awaitTermination(10, TimeUnit.SECONDS);
            assertTrue(responseList.contains(204), "Check if the first of request was passed.");
            assertTrue(responseList.contains(409), "Check if the rest of requests was failed.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
