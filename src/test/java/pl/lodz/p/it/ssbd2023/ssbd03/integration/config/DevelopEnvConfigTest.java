package pl.lodz.p.it.ssbd2023.ssbd03.integration.config;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.nio.file.Paths;

@Testcontainers
public class DevelopEnvConfigTest {
    /* Images */
    private static final DockerImageName NGINX_IMAGE = DockerImageName.parse("nginx:latest");
    private static final DockerImageName PAYARA_IMAGE = DockerImageName.parse("payara/server-full:6.2023.3-jdk17");
    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres");

    /* Network and ports */
    protected static int POSTGRES_PORT;
    protected static int PAYARA_PORT;
    protected static int NGINX_PORT;

    private static Logger logger = LoggerFactory.getLogger("testcontainers-config");

    /* Containers */
    @Container
    private static PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withLogConsumer(new Slf4jLogConsumer(logger))
            .withCopyFileToContainer(
                    MountableFile.forHostPath(new File("src/test/resources/docker/create_db_users.sh").getAbsolutePath()),
                    "/docker-entrypoint-initdb.d/create_db_users.sh")
            .withDatabaseName("ssbd03")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432);

    @Container
    private static GenericContainer<?> PAYARA = new GenericContainer<>(PAYARA_IMAGE)
            .withLogConsumer(new Slf4jLogConsumer(logger))
            .withExposedPorts(8080)
            .withCopyFileToContainer(
                    MountableFile.forHostPath(Paths.get("target/ssbd03-0.1.0.war").toAbsolutePath()),
                    "/opt/payara/deployments/ssbd03-0.1.0.war")
            .dependsOn(POSTGRES)
            .waitingFor(Wait.forLogMessage(".*was successfully deployed in.*", 1));

    @Container
    private static GenericContainer<?> NGINX = new GenericContainer<>(NGINX_IMAGE)
            .withLogConsumer(new Slf4jLogConsumer(logger))
            .withCopyFileToContainer(
                    MountableFile.forHostPath(new File("src/test/resources/docker/nginx.conf").getAbsolutePath()),
                    "/etc/nginx/nginx.conf")
            .withCopyFileToContainer(
                    MountableFile.forHostPath(new File("src/test/resources/docker/secrets/certs").getAbsolutePath()),
                    "/etc/ssl/certs")
            .withCopyFileToContainer(
                    MountableFile.forHostPath(new File("src/test/resources/docker/secrets/private").getAbsolutePath()),
                    "/etc/ssl/private")
            .withCopyFileToContainer(
                    MountableFile.forHostPath(new File("src/test/resources/docker/secrets/snippets").getAbsolutePath()),
                    "/etc/nginx/snippets")
            .withExposedPorts(80, 443)
            .dependsOn(PAYARA);

    @BeforeClass
    public static void initTestContainers() {
        try (Network network = Network.newNetwork()) {
            POSTGRES.withNetwork(network).withNetworkAliases("database");
            PAYARA.withNetwork(network).withNetworkAliases("appserver");
            NGINX.withNetwork(network).withNetworkAliases("nginx");

            POSTGRES.start();
            POSTGRES_PORT = POSTGRES.getMappedPort(5432);
            PAYARA.start();
            PAYARA_PORT = PAYARA.getMappedPort(8080);
            NGINX.start();
            NGINX_PORT = NGINX.getMappedPort(443);

            logger.info("Postgres port: " + POSTGRES_PORT);
            logger.info("Payara port: " + PAYARA_PORT);
            logger.info("Nginx port: " + NGINX_PORT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void endTestAndStopContainers() {
        NGINX.stop();
        logger.info("Nginx container stopped.");
        PAYARA.stop();
        logger.info("Payara container stopped.");
        POSTGRES.stop();
        logger.info("Postgres container stopped.");
    }
}