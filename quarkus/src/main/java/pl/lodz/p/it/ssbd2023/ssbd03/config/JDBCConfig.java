package pl.lodz.p.it.ssbd2023.ssbd03.config;

import io.quarkus.hibernate.orm.PersistenceUnit;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.persistence.EntityManager;

import java.sql.Connection;

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03admin",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03admin",
        password = "9LUoYTSMH",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "ssbd03",
        initialPoolSize = 1,
        minPoolSize = 0,
        maxPoolSize = 1,
        maxIdleTime = 10,
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03auth",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03auth",
        password = "KHgXydJUv",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "ssbd03",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03mok",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mok",
        password = "CHqZxv5R1",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "ssbd03",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03mow",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mow",
        password = "obSjEBGaX",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "ssbd03",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@Boundary
public class JDBCConfig {
    @Inject
    @PersistenceUnit("ssbd03adminPU")
    EntityManager em;
}
