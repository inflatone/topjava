package ru.javawebinar.topjava.service;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static ru.javawebinar.topjava.TimingRules.STOPWATCH;
import static ru.javawebinar.topjava.TimingRules.SUMMARY;
import static ru.javawebinar.topjava.util.ValidationUtil.getRootCause;

@RunWith(SpringRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractServiceTest {
    @ClassRule
    public static ExternalResource summary = SUMMARY;

    @Rule
    public Stopwatch stopwatch = STOPWATCH;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // Check root cause in JUnit: https://github.com/junit-team/junit4/pull/778
    public <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        try {
            runnable.run();
            fail("Expected " + exceptionClass.getName());
        } catch (Exception e) {
            assertThat(getRootCause(e), instanceOf(exceptionClass));
        }
    }
}
