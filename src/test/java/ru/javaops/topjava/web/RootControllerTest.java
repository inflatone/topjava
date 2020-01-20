package ru.javaops.topjava.web;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.topjava.MealTestData.MEALS;
import static ru.javaops.topjava.TestUtil.userAuth;
import static ru.javaops.topjava.UserTestData.ADMIN;
import static ru.javaops.topjava.UserTestData.USER;
import static ru.javaops.topjava.util.MealsUtil.getTOs;

class RootControllerTest extends AbstractControllerTest {
    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/users")
                .with(userAuth(ADMIN))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

    @Test
    void unauth() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getRoot() throws Exception {
        mockMvc.perform(get("/")
                .with(userAuth(USER))
        )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("meals"));
    }

    @Test
    void getMeals() throws Exception {
        mockMvc.perform(get("/meals")
                .with(userAuth(USER))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", getTOs(MEALS, USER.getCaloriesPerDay())));
    }
}