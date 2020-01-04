package ru.javaops.topjava.service.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javaops.topjava.Profiles;
import ru.javaops.topjava.UserTestData;
import ru.javaops.topjava.service.AbstractJpaMealServiceTest;
import ru.javaops.topjava.util.exeption.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.topjava.MealTestData.assertMatch;
import static ru.javaops.topjava.MealTestData.*;
import static ru.javaops.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATA_JPA)
class DataJpaMealServiceTest extends AbstractJpaMealServiceTest {
    @Test
    void getWithUser() {
        var meal = service.getWithUser(MEAL1_ID, USER_ID);
        assertMatch(meal, MEAL1);
        UserTestData.assertMatch(meal.getUser(), USER);
    }

    @Test
    void getWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(0, 0));
    }

    @Test
    void getWithUserNotOwn() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(MEAL1_ID, ADMIN_ID));
    }
}
