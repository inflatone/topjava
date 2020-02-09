package ru.javaops.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.service.UserService;
import ru.javaops.topjava.util.exeption.ErrorType;
import ru.javaops.topjava.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.TestUtil.contentTypeIsJson;
import static ru.javaops.topjava.TestUtil.readFromJson;
import static ru.javaops.topjava.UserTestData.*;
import static ru.javaops.topjava.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL;

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
    void getNotFound() throws Exception {
        perform(doGet(1).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
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
    void delete() throws Exception {
        perform(doDelete(USER_ID).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHERS.assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(doDelete(1).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void create() throws Exception {
        var newUser = createNew();
        var resultActions = perform(doPost().jsonUserWithPassword(newUser).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(contentTypeIsJson());
        var created = readFromJson(resultActions, User.class);
        newUser.setId(created.getId());

        USER_MATCHERS.assertMatch(created, newUser);
        USER_MATCHERS.assertMatch(userService.getAll(), ADMIN, newUser, USER);
    }

    @Test
    void createInvalid() throws Exception {
        var invalid = new User(null, null, "", "newPass", 7300, Role.ROLE_USER, Role.ROLE_ADMIN);
        perform(doPost().jsonBody(invalid).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR));
    }

    @Test
    void createDuplicate() throws Exception {
        var invalid = createNew();
        invalid.setEmail(USER.getEmail());
        perform(doPost().jsonUserWithPassword(invalid).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL));
    }

    @Test
    void update() throws Exception {
        var updated = createUpdated();
        perform(doPut(USER_ID).jsonUserWithPassword(updated).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHERS.assertMatch(userService.getAll(), ADMIN, updated);
    }

    @Test
    void updateInvalid() throws Exception {
        var invalid = new User(USER);
        invalid.setName(null);
        perform(doPut(USER_ID).jsonBody(invalid).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR));
    }

    @Test
    void updateDuplicate() throws Exception {
        var invalid = createUpdated();
        invalid.setEmail(ADMIN.getEmail());
        perform(doPut(USER.getId()).jsonUserWithPassword(invalid).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL));
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
