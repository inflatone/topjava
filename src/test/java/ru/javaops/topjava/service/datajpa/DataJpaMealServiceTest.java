package ru.javaops.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javaops.topjava.Profiles;
import ru.javaops.topjava.UserTestData;
import ru.javaops.topjava.service.AbstractMealServiceTest;
import ru.javaops.topjava.util.exeption.NotFoundException;

import static ru.javaops.topjava.MealTestData.assertMatch;
import static ru.javaops.topjava.MealTestData.*;
import static ru.javaops.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATA_JPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void testGetWithUser() {
        var meal = service.getWithUser(MEAL1_ID, USER_ID);
        assertMatch(meal, MEAL1);
        UserTestData.assertMatch(meal.getUser(), USER);
    }

    @Test
    public void testGetWithUserNotFound() {
        thrown.expect(NotFoundException.class);
        service.getWithUser(0, 0);
    }

    @Test
    public void testGetWithUserNotOwn() {
        thrown.expect(NotFoundException.class);
        service.getWithUser(MEAL1_ID, ADMIN_ID);
    }
}
