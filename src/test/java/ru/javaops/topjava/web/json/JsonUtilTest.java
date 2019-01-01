package ru.javaops.topjava.web.json;

import org.junit.jupiter.api.Test;
import ru.javaops.topjava.View;
import ru.javaops.topjava.model.Meal;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.javaops.topjava.MealTestData.*;

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
    public void testWriteWithView() throws Exception {
        var writer = JacksonObjectMapper.getMapper().writerWithView(View.JsonUI.class);
        var json = JsonUtil.writeValue(ADMIN_MEAL1, writer);
        System.out.println(json);
        assertThat(json, containsString("dateTimeUI"));
    }
}