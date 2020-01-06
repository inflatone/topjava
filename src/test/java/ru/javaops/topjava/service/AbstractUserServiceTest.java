package ru.javaops.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.util.exeption.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaops.topjava.UserTestData.*;
import static ru.javaops.topjava.model.Role.ROLE_USER;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {
    @Autowired
    protected UserService service;

    @Test
    void create() {
        var newUser = createNew();
        var created = service.create(new User(newUser));
        var newId = created.getId();
        newUser.setId(newId);
        USER_MATCHERS.assertMatch(created, newUser);
        USER_MATCHERS.assertMatch(service.get(newId), newUser);
    }

    @Test
    void createDuplicateEmail() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", 2000, ROLE_USER)));
    }

    @Test
    void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(0));
    }

    @Test
    void get() {
        var user = service.get(ADMIN_ID);
        USER_MATCHERS.assertMatch(user, ADMIN);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(0));
    }

    @Test
    void getByEmail() {
        var admin = service.getByEmail(ADMIN.getEmail());
        USER_MATCHERS.assertMatch(admin, ADMIN);
    }

    @Test
    void update() {
        var updated = createUpdated();
        service.update(new User(updated));
        USER_MATCHERS.assertMatch(service.get(USER_ID), updated);
    }

    @Test
    void enable() {
        service.enable(USER_ID, false);
        assertFalse(service.get(USER_ID).isEnabled());
        service.enable(USER_ID, true);
        assertTrue(service.get(USER_ID).isEnabled());
    }

    @Test
    void getAll() {
        USER_MATCHERS.assertMatch(service.getAll(), ADMIN, USER);
    }

    @Test
    void createWithException() {
        validateException(new User(null, "  ", "mail@yandex.ru", "password", 2000, ROLE_USER));
        validateException(new User(null, "User", "  ", "password", 2000, ROLE_USER));
        validateException(new User(null, "User", "mail@yandex.ru", "  ", 2000, ROLE_USER));
        validateException(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of()));
        validateException(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of()));
    }

    private void validateException(User user) {
        validateRootCause(() -> service.create(user), ConstraintViolationException.class);
    }
}
