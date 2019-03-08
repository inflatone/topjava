package ru.javawebinar.topjava.service.datajpa;

import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static org.junit.Assert.assertNull;
import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeals() {
        MealTestData.assertMatch(
                service.getWithMeals(USER_ID).getMeals(),
                MEALS
        );
        thrown.expect(LazyInitializationException.class);
        thrown.expectMessage("failed to lazily initialize a collection of role: ru.javawebinar.topjava.model.User.meals, could not initialize proxy - no Session");
        assertNull(service.get(USER_ID).getMeals());

    }
}
