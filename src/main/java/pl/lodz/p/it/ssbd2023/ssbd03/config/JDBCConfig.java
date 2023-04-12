package pl.lodz.p.it.ssbd2023.ssbd03.config;

import jakarta.annotation.sql.DataSourceDefinition;
import java.sql.Connection;

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03admin",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03admin",
        password = "9LUoYTSMH",
        portNumber = 5352,
        databaseName = "ssbd03",
        initialPoolSize = 1,
        minPoolSize = 0,
        maxPoolSize = 1,
        maxIdleTime = 10)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03auth",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03auth",
        password = "KHgXydJUv",
        portNumber = 5352,
        databaseName = "ssbd03")

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03mok",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mok",
        password = "CHqZxv5R1",
        portNumber = 5352,
        databaseName = "ssbd03",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03mow",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mow",
        password = "obSjEBGaX",
        portNumber = 5352,
        databaseName = "ssbd03",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

public class JDBCConfig {
}
