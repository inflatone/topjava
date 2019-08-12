package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractJpaUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractJpaUserServiceTest {
    @Test
    public void testGetWithMeals() {
        User withMeals = service.getWithMeals(USER_ID);
        assertMatch(withMeals, USER);
        MealTestData.assertMatch(withMeals.getMeals(), MEALS);
    }

    @Test
    public void testGetWithMealsNotFound() {
        thrown.expect(NotFoundException.class);
        service.getWithMeals(1);
    }
}