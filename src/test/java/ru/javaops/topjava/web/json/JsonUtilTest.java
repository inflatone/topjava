package ru.javaops.topjava.web.json;

import org.junit.jupiter.api.Test;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.model.User;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.javaops.topjava.MealTestData.*;
import static ru.javaops.topjava.UserTestData.USER;

class JsonUtilTest {
    @Test
    void readWriteValue() {
        var json = JsonUtil.writeValue(ADMIN_MEAL1);
        System.out.println(json);
        var meal = JsonUtil.readValue(json, Meal.class);
        MEAL_MATCHERS.assertMatch(meal, ADMIN_MEAL1);
    }

    @Test
    void readWriteValues() {
        var json = JsonUtil.writeValue(MEALS);
        System.out.println(json);
        var meals = JsonUtil.readValues(json, Meal.class);
        MEAL_MATCHERS.assertMatch(meals, MEALS);
    }

    @Test
    void writeOnlyAccess() {
        String json = JsonUtil.writeValue(USER);
        System.out.println(json);
        assertThat(json, not(containsString("password")));

        String jsonWithPassword = JsonUtil.writeWithAdditionalProperty(USER, "password", "newPass");
        System.out.println(jsonWithPassword);
        User user = JsonUtil.readValue(jsonWithPassword, User.class);
        assertEquals(user.getPassword(), "newPass");
    }
}