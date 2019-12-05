package ru.javaops.topjava.service;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.util.exeption.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertMatch(created, newUser);
        assertMatch(service.get(newId), newUser);
    }

    @Test
    void createDuplicateEmail() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", ROLE_USER)));
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
        assertMatch(user, ADMIN);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(0));
    }

    @Test
    void getByEmail() {
        var admin = service.getByEmail(ADMIN.getEmail());
        assertMatch(admin, ADMIN);
    }

    @Test
    void update() {
        var updated = createUpdated();
        service.update(new User(updated));
        assertMatch(service.get(USER_ID), updated);
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(), ADMIN, USER);
    }

    @Test
    void createWithException() {
        Assumptions.assumeTrue(isJpaBased(), "Validation not supported (JPA only)");
        validateException(new User(null, "  ", "mail@yandex.ru", "password", ROLE_USER));
        validateException(new User(null, "User", "  ", "password", ROLE_USER));
        validateException(new User(null, "User", "mail@yandex.ru", "  ", ROLE_USER));
        validateException(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of()));
        validateException(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of()));
    }

    private void validateException(User user) {
        validateRootCause(() -> service.create(user), ConstraintViolationException.class);
    }
}
