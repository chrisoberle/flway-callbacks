package com.baeldung.flyway;

import java.util.Arrays;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = FlywayCallbackTestConfig.class)
public class FlywayApplicationTests {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private DataSource dataSource;

    @Test
    public void migrateWithNoCallbacks() {
        logTestBoundary("migrateWithNoCallbacks");
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("db/migration");
        flyway.migrate(); 
        //logFlywayInfo(flyway.info());
    }

    @Test
    public void migrateWithJavaCallbacks() {
        logTestBoundary("migrateWithJavaCallbacks");
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("db/migration");
        flyway.setCallbacks(new ExampleFlywayCallback());
        flyway.migrate();
        //logFlywayInfo(flyway.info());
    }

    @Test
    public void migrateWithSqlCallbacks() {
        logTestBoundary("migrateWithSqlCallbacks");
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("db/migration", "db/callbacks");
        flyway.migrate(); 
        //logFlywayInfo(flyway.info());
    }

    @Test
    public void migrateWithSqlAndJavaCallbacks() {
        logTestBoundary("migrateWithSqlAndJavaCallbacks");
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("db/migration", "db/callbacks");
        flyway.setCallbacks(new ExampleFlywayCallback());
        flyway.migrate(); 
        //logFlywayInfo(flyway.info());
    }

    private void logFlywayInfo(MigrationInfoService infoService) {
        log.info(String.format("%s\t%s\t%s\t%s", "Version", "Type", "State", "Script"));
        log.info(String.format("%s", "----------------------------------------"));
        Arrays.asList(infoService.all()).stream().forEach( mi -> {
            log.info(String.format("%s\t%s\t%s\t%s", mi.getVersion(), mi.getType(), mi.getState(), mi.getScript()));
        });
    }

    private void logTestBoundary(String testName) {
        System.out.println("\n");
        log.info("> " + testName);
    }
 }
