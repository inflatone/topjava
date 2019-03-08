package ru.javawebinar.topjava.service.datajpa;

import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static org.junit.Assert.assertNull;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void getWithUser() {
        UserTestData.assertMatch(service.getWithUser(MEAL1_ID, USER_ID).getUser(), USER);
        thrown.expect(LazyInitializationException.class);
        thrown.expectMessage("could not initialize proxy [ru.javawebinar.topjava.model.User#100000] - no Session");
        assertNull(service.get(MEAL1_ID, USER_ID).getUser());
    }
}
