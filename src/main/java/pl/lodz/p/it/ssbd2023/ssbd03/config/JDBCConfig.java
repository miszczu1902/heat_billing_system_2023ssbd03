package pl.lodz.p.it.ssbd2023.ssbd03.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.sql.Connection;

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03admin",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03admin",
        password = "9LUoYTSMH",
        serverName = "database",
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
        serverName = "database",
        portNumber = 5432,
        databaseName = "ssbd03",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03mok",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mok",
        password = "CHqZxv5R1",
        serverName = "database",
        portNumber = 5432,
        databaseName = "ssbd03",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd03mow",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd03mow",
        password = "obSjEBGaX",
        serverName = "database",
        portNumber = 5432,
        databaseName = "ssbd03",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@Stateless
public class JDBCConfig {
    @PersistenceContext(unitName = "ssbd03adminPU")
    private EntityManager em;
}
