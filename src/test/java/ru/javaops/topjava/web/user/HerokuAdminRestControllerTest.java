package ru.javaops.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.test.context.ActiveProfiles;
import ru.javaops.topjava.util.exeption.ErrorType;
import ru.javaops.topjava.web.AbstractControllerTest;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.Profiles.HEROKU;
import static ru.javaops.topjava.UserTestData.*;
import static ru.javaops.topjava.util.exeption.ModificationRestrictionException.EXCEPTION_MODIFICATION_RESTRICTION;

@ActiveProfiles({HEROKU})
public class HerokuAdminRestControllerTest extends AbstractControllerTest {

    static {
        Resource resource = new ClassPathResource("db/postgres.properties");
        try {
            var propertySource = new ResourcePropertySource(resource);
            var herokuDbUrl = String.format("postgres://%s:%s@%s",
                    propertySource.getProperty("database.username"),
                    propertySource.getProperty("database.password"),
                    ((String) requireNonNull(propertySource.getProperty("database.url"), "url must be set")).substring(18));
            System.out.println(herokuDbUrl);
            System.setProperty("DATABASE_URL", herokuDbUrl);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public HerokuAdminRestControllerTest() {
        super(AdminRestController.REST_URL);
    }

    @Test
    void deleteRestricted() throws Exception {
        perform(doDelete(USER_ID).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_MODIFICATION_RESTRICTION));
    }

    @Test
    void updateRestricted() throws Exception {
        perform(doPut(USER_ID).jsonUserWithPassword(USER).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_MODIFICATION_RESTRICTION));

    }
}
