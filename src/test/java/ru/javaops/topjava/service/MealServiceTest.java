package ru.javaops.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javaops.topjava.util.exeption.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaops.topjava.MealTestData.*;
import static ru.javaops.topjava.UserTestData.ADMIN_ID;
import static ru.javaops.topjava.UserTestData.USER_ID;

@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"})
@ActiveProfiles({"hsqldb", "jpa"})
public class MealServiceTest {
    private static final Logger log = getLogger(MealServiceTest.class);

    private static StringBuilder results = new StringBuilder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            var result = format("\n%-25s %7d", description.getMethodName(), NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @AfterClass
    public static void printResults() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    @Autowired
    private MealService service;

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        thrown.expect(NotFoundException.class);
        service.get(MEAL1_ID, USER_ID);
    }

    @Test
    public void deleteNotOwn() {
        thrown.expect(NotFoundException.class);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void deleteNotFound() {
        thrown.expect(NotFoundException.class);
        service.delete(0, USER_ID);
    }

    @Test
    public void create() {
        var newMeal = getCreated();
        var created = service.create(newMeal, USER_ID);
        var newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void get() {
        var actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotOwn() {
        thrown.expect(NotFoundException.class);
        service.get(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void getNotFound() {
        thrown.expect(NotFoundException.class);
        service.get(0, USER_ID);
    }

    @Test
    public void update() {
        var updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotOwn() {
        thrown.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        var meals = service.getAll(USER_ID);
        assertMatch(meals, MEALS);
    }

    @Test
    public void getBetween() {
        var meals = service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30),
                USER_ID
        );
        assertMatch(meals, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() {
        assertMatch(service.getBetweenDates(null, null, USER_ID), MEALS);
    }
}
