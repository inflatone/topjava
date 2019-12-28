package ru.javaops.topjava.service.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javaops.topjava.Profiles;
import ru.javaops.topjava.service.AbstractUserServiceTest;
import ru.javaops.topjava.util.exeption.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.topjava.MealTestData.*;
import static ru.javaops.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATA_JPA)
class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    void getWithMeals() {
        var user = service.getWithMeals(ADMIN_ID);
        USER_MATCHERS.assertMatch(user, ADMIN);
        MEAL_MATCHERS.assertMatch(user.getMeals(), ADMIN_MEAL2, ADMIN_MEAL1);
    }

    @Test
    void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(0));
    }
}