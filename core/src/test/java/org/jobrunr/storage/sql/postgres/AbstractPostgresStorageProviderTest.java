package org.jobrunr.storage.sql.postgres;

import com.zaxxer.hikari.HikariDataSource;
import org.jobrunr.storage.sql.SqlStorageProviderTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.extension.AfterAllSubclasses;
import org.junit.jupiter.extension.BeforeAllSubclasses;
import org.junit.jupiter.extension.ForAllSubclassesExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.Instant;

import static java.time.Instant.now;
import static org.jobrunr.storage.sql.SqlTestUtils.toHikariDataSource;

@ExtendWith(ForAllSubclassesExtension.class)
public abstract class AbstractPostgresStorageProviderTest extends SqlStorageProviderTest {

    protected static PostgreSQLContainer sqlContainer = new PostgreSQLContainer<>("postgres:12");

    protected static HikariDataSource dataSource;

    @Override
    public DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = toHikariDataSource(sqlContainer);
        }
        return dataSource;
    }

    @AfterAll
    public static void destroyDatasource() {
        dataSource.close();
        dataSource = null;
    }

    @BeforeAllSubclasses
    public static void startSqlContainer() {
        Instant before = now();
        sqlContainer.start();
        printSqlContainerDetails(sqlContainer, Duration.between(before, now()));
    }

    @AfterAllSubclasses
    public static void stopSqlContainer() {
        sqlContainer.stop();
    }
}
