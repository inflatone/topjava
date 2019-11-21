package ru.javaops.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javaops.topjava.MealTestData;
import ru.javaops.topjava.Profiles;
import ru.javaops.topjava.service.AbstractJpaUserServiceTest;
import ru.javaops.topjava.util.exeption.NotFoundException;

import static ru.javaops.topjava.MealTestData.MEALS;
import static ru.javaops.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATA_JPA)
public class DataJpaUserServiceTest extends AbstractJpaUserServiceTest {
    @Test
    public void getWithMeals() {
        var user = service.getWithMeals(USER_ID);
        assertMatch(user, USER);
        MealTestData.assertMatch(user.getMeals(), MEALS);
    }

    @Test
    public void getWithMealsNotFound() {
        thrown.expect(NotFoundException.class);
        service.getWithMeals(0);
    }
}
