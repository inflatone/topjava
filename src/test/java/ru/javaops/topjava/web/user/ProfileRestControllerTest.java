package ru.javaops.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.service.UserService;
import ru.javaops.topjava.to.UserTo;
import ru.javaops.topjava.util.UserUtil;
import ru.javaops.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.TestUtil.contentTypeIsJson;
import static ru.javaops.topjava.UserTestData.*;

class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private UserService userService;

    ProfileRestControllerTest() {
        super(ProfileRestController.REST_URL);
    }

    @Test
    void getProfile() throws Exception {
        perform(doGet().basicAuth(USER))
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
    void deleteProfile() throws Exception {
        perform(doDelete().basicAuth(USER))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHERS.assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void updateProfile() throws Exception {
        var updatedTo = new UserTo(null, "newName", "newEmail@ya.ru", "newPassword", 1500);
        perform(doPut().basicAuth(USER).jsonBody(updatedTo))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHERS.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(USER), updatedTo));
    }
}
