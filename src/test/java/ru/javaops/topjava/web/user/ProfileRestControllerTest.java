package ru.javaops.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.service.UserService;
import ru.javaops.topjava.to.UserTo;
import ru.javaops.topjava.util.UserUtil;
import ru.javaops.topjava.util.exeption.ErrorType;
import ru.javaops.topjava.web.AbstractControllerTest;

import java.util.concurrent.ExecutionException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.TestUtil.contentTypeIsJson;
import static ru.javaops.topjava.TestUtil.readFromJson;
import static ru.javaops.topjava.UserTestData.*;
import static ru.javaops.topjava.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL;

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
    void register() throws Exception {
        var newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);
        var newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(doPost("/register").jsonBody(newTo))
                .andDo(print())
                .andExpect(status().isCreated());

        var createdUser = readFromJson(action, User.class);
        Integer newId = createdUser.getId();
        newUser.setId(newId);
        USER_MATCHERS.assertMatch(createdUser, newUser);
        USER_MATCHERS.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void updateProfile() throws Exception {
        var updatedTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);
        perform(doPut().basicAuth(USER).jsonBody(updatedTo))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHERS.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(USER), updatedTo));
    }

    @Test
    void updateInvalid()  throws Exception {
        var updatedTo = new UserTo(null, null, "password", null, 1500);
        perform(doPut().jsonBody(updatedTo).basicAuth(USER))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    void updateDuplicate() throws Exception {
        var updatedTo = new UserTo(null, "newName", ADMIN.getEmail(), "newPass", 1500);
        perform(doPut().jsonBody(updatedTo).basicAuth(USER))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL));
    }
}
