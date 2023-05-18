package pl.lodz.p.it.ssbd2023.ssbd03.integration.config;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Setter;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;

import java.util.Optional;

import static io.restassured.RestAssured.*;

public class BasicIntegrationConfigTest extends DevelopEnvConfigTest {
    /* RestAssured config */
    protected static Logger logger = LoggerFactory.getLogger("e2e-tests");
    private static ObjectMapper mapper = new ObjectMapper();

    /* Test data */
    @Setter
    private static String BEARER_TOKEN = "";
    @Setter
    protected static String ETAG = "";

    protected static Response sendRequestAndGetResponse(Method method, String path, Object object, ContentType contentType) {
        contentType = contentType == null ? ContentType.ANY : contentType;
        RequestSpecification request = given().contentType(contentType);
        String jsonObject = objectToJson(object);

        if (object != null) request.body(jsonObject);
        if (!BEARER_TOKEN.equals("") && !path.equals("/accounts/login") && !path.equals("/accounts/register"))
            request.header(new Header("Authorization", "Bearer " + BEARER_TOKEN));
        if (!ETAG.equals("") && method.equals(Method.PATCH))
            request.header(new Header("If-Match", ETAG));

        logBeforeRequest(method, path, jsonObject, contentType);
        Response response = request.request(method, baseURI + path);
        ETAG = Optional.ofNullable(response.getHeader("ETag")).orElse("");

        logAfterRequest(response);
        return response;
    }

    private static String objectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void auth(LoginDTO loginData) {
        BEARER_TOKEN = sendRequestAndGetResponse(Method.POST, "/accounts/login", loginData, ContentType.JSON)
                .getHeader("Bearer");
    }

    private static void logMetaData(Method method, String path, ContentType contentType) {
        logger.info(new StringBuilder()
                .append("Method: ").append(method.toString()).append("\n")
                .append("Path: ").append(baseURI).append(path).append("\n")
                .append("Content-Type: ").append(Optional.ofNullable(contentType.toString()).orElse("")).append("\n")
                .append("Authorization: ").append(BEARER_TOKEN).append("\n").toString());
    }

    protected static void logBeforeRequest(Method method, String path, String jsonBody, ContentType contentType) {
        logMetaData(method, path, contentType);
        logger.info("Request body: \n" + jsonBody);
    }

    protected static void logAfterRequest(Response response) {
        logger.info("Status code: " + response.getStatusCode() + "\n" +
                "Response body: \n" + response.getBody().asPrettyString() + "\n"
                + "ETag: " + ETAG + "\n");
    }

    @BeforeClass
    public static void initRestAssured() {
        baseURI = "https://localhost/api";
        RestAssured.port = NGINX_PORT;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.useRelaxedHTTPSValidation();

        logger.info("URI: " + baseURI);
        logger.info("Default port: " + port);
        logger.info("Default parser: " + defaultParser);
    }
}