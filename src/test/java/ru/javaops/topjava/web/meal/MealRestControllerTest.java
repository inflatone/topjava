package ru.javaops.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javaops.topjava.MealTestData;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.service.MealService;
import ru.javaops.topjava.util.exeption.NotFoundException;
import ru.javaops.topjava.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.MealTestData.*;
import static ru.javaops.topjava.TestUtil.readFromJson;
import static ru.javaops.topjava.TestUtil.readFromJsonMvcResult;
import static ru.javaops.topjava.UserTestData.*;
import static ru.javaops.topjava.util.MealsUtil.createTO;
import static ru.javaops.topjava.util.MealsUtil.getTOs;

class MealRestControllerTest extends AbstractControllerTest {
    @Autowired
    private MealService mealService;

    MealRestControllerTest() {
        super(MealRestController.REST_URL);
    }

    @Test
    void get() throws Exception {
        perform(doGet(ADMIN_MEAL_ID).basicAuth(ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> MEAL_MATCHERS.assertMatch(readFromJsonMvcResult(result, Meal.class), ADMIN_MEAL1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(doGet(MEAL1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(doDelete(MEAL1_ID).basicAuth(USER))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.createUpdated();

        perform(doPut(MEAL1_ID).jsonBody(updated).basicAuth(USER))
                .andDo(print())
                .andExpect(status().isNoContent());
        MEAL_MATCHERS.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.createNew();
        ResultActions action = perform(doPost().jsonBody(newMeal).basicAuth(USER));
        Meal created = readFromJson(action, Meal.class);
        Integer newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHERS.assertMatch(created, newMeal);
        MEAL_MATCHERS.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void getAll() throws Exception {
        perform(doGet().basicAuth(USER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHERS.contentJson(getTOs(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void filter() throws Exception {
        perform(doGet("filter")
                .basicAuth(USER)
                .unwrap()
                .param("startDate", "2015-05-30")
                .param("startTime", "07:00")
                .param("endDate", "2015-05-31")
                .param("endTime", "11:00")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHERS.contentJson(createTO(MEAL5, true), createTO(MEAL1, false)));
    }

    @Test
    void filterAll() throws Exception {
        perform(doGet("filter?startDate=&endTime=").basicAuth(USER))
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHERS.contentJson(getTOs(MEALS, USER.getCaloriesPerDay())));
    }
}
