package ru.javaops.topjava.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.topjava.MealTestData.MEALS;
import static ru.javaops.topjava.UserTestData.USER;
import static ru.javaops.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javaops.topjava.util.MealsUtil.getTOs;

class RootControllerTest extends AbstractControllerTest {
    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

    @Test
    void getRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"));
    }

    @Test
    void setUser() throws Exception {
        mockMvc.perform(post("/users")
                .param("userId", String.valueOf(START_SEQ + 500))
        )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:meals"))
                .andExpect(redirectedUrl("meals"));
        assertEquals(SecurityUtil.authUserId(), START_SEQ + 500);
        SecurityUtil.setAuthUserId(START_SEQ);
    }

    @Test
    void getMeals() throws Exception {
        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", getTOs(MEALS, USER.getCaloriesPerDay())));
    }
}