package ru.javaops.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.web.AbstractControllerTest;
import ru.javaops.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.TestUtil.contentTypeIsJson;
import static ru.javaops.topjava.TestUtil.readFromJson;
import static ru.javaops.topjava.UserTestData.*;

class AdminRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Test
    void getById() throws Exception {
        mockMvc.perform(get(REST_URL + ADMIN_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(contentTypeIsJson())
                .andExpect(contentJson(ADMIN));
    }


    @Test
    void getByEmail() throws Exception {
        mockMvc.perform(get(REST_URL + "by")
                .param("email", USER.getEmail())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(contentTypeIsJson())
                .andExpect(contentJson(USER));
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(contentTypeIsJson())
                .andExpect(contentJson(ADMIN, USER));
    }

    @Test
    void remove() throws Exception {
        mockMvc.perform(delete(REST_URL + USER_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void create() throws Exception {
        var newUser = createNew();
        var resultActions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUser))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(contentTypeIsJson());
        var created = readFromJson(resultActions, User.class);
        newUser.setId(created.getId());

        assertMatch(created, newUser);
        assertMatch(userService.getAll(), ADMIN, newUser, USER);
    }

    @Test
    void update() throws Exception {
        var updated = createUpdated();
        mockMvc.perform(put(REST_URL + USER_ID)
        .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
        )
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN, updated);
    }
}
