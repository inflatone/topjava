package ru.javawebinar.topjava.service.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractJpaUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL1;
import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL2;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
class DataJpaUserServiceTest extends AbstractJpaUserServiceTest {
    @Test
    void testGetWithMeals() {
        User admin = service.getWithMeals(ADMIN_ID);
        assertMatch(admin, ADMIN);
        MealTestData.assertMatch(admin.getMeals(), ADMIN_MEAL2, ADMIN_MEAL1);
    }

    @Test
    void testGetWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(1));
    }
}