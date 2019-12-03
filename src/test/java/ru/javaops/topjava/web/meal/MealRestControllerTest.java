package ru.javaops.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.service.MealService;
import ru.javaops.topjava.util.exeption.NotFoundException;
import ru.javaops.topjava.web.AbstractControllerTest;
import ru.javaops.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.MealTestData.*;
import static ru.javaops.topjava.TestUtil.readFromJson;
import static ru.javaops.topjava.TestUtil.readFromJsonMvcResult;
import static ru.javaops.topjava.UserTestData.USER;
import static ru.javaops.topjava.UserTestData.USER_ID;
import static ru.javaops.topjava.util.MealsUtil.createTO;
import static ru.javaops.topjava.util.MealsUtil.getTOs;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(readFromJsonMvcResult(result, Meal.class), MEAL1));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void update() throws Exception {
        Meal updated = createUpdated();

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
        )
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = createNew();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal))
        );
        Meal created = readFromJson(action, Meal.class);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(getTOs(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void filter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2015-05-30")
                .param("startTime", "07:00")
                .param("endDate", "2015-05-31")
                .param("endTime", "11:00")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(contentJson(createTO(MEAL5, true), createTO(MEAL1, false)));

    }

    @Test
    void filterAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter?startDate=&endTime="))
                .andExpect(status().isOk())
                .andExpect(contentJson(getTOs(MEALS, USER.getCaloriesPerDay())));
    }
}
