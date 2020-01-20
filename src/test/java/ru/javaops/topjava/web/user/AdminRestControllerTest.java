package ru.javaops.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.service.UserService;
import ru.javaops.topjava.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.TestUtil.contentTypeIsJson;
import static ru.javaops.topjava.TestUtil.readFromJson;
import static ru.javaops.topjava.UserTestData.*;

class AdminRestControllerTest extends AbstractControllerTest {
    @Autowired
    private UserService userService;

    AdminRestControllerTest() {
        super(AdminRestController.REST_URL);
    }

    @Test
    void getById() throws Exception {
        perform(doGet(ADMIN_ID).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(contentTypeIsJson())
                .andExpect(USER_MATCHERS.contentJson(ADMIN));
    }


    @Test
    void getByEmail() throws Exception {
        perform(doGet("by")
                .basicAuth(ADMIN)
                .unwrap()
                .param("email", USER.getEmail())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(contentTypeIsJson())
                .andExpect(USER_MATCHERS.contentJson(USER));
    }

    @Test
    void getUnauth() throws Exception {
        perform(doGet())
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(doGet().basicAuth(USER))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getAll() throws Exception {
        perform(doGet().basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(contentTypeIsJson())
                .andExpect(USER_MATCHERS.contentJson(ADMIN, USER));
    }

    @Test
    void remove() throws Exception {
        perform(doDelete(USER_ID).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHERS.assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void create() throws Exception {
        var newUser = createNew();
        var resultActions = perform(doPost().jsonBody(newUser).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(contentTypeIsJson());
        var created = readFromJson(resultActions, User.class);
        newUser.setId(created.getId());

        USER_MATCHERS.assertMatch(created, newUser);
        USER_MATCHERS.assertMatch(userService.getAll(), ADMIN, newUser, USER);
    }

    @Test
    void update() throws Exception {
        var updated = createUpdated();
        perform(doPut(USER_ID).jsonBody(updated).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHERS.assertMatch(userService.getAll(), ADMIN, updated);
    }

    @Test
    void enable() throws Exception {
        perform(doPatch(USER_ID)
                .basicAuth(ADMIN)
                .unwrap()
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(userService.get(USER_ID).isEnabled());
    }
}
