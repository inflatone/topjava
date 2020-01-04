package ru.javaops.topjava.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.util.exeption.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.topjava.UserTestData.*;
import static ru.javaops.topjava.model.Role.ROLE_USER;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {
    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void  setUp() {
        Optional.ofNullable(cacheManager.getCache("users")).ifPresent(Cache::clear);
    }

    @Test
    void  create() {
        var newUser = createNew();
        var created = service.create(new User(newUser));
        var newId = created.getId();
        newUser.setId(newId);
        assertMatch(created, newUser);
        assertMatch(service.get(newId), newUser);
    }

    @Test
    void  createDuplicateEmail() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", ROLE_USER)));
    }

    @Test
    void  delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    void  deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(0));
    }

    @Test
    void  get() {
        var user = service.get(ADMIN_ID);
        assertMatch(user, ADMIN);
    }

    @Test
    void  getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(0));
    }

    @Test
    void  getByEmail() {
        var admin = service.getByEmail(ADMIN.getEmail());
        assertMatch(admin, ADMIN);
    }

    @Test
    void  update() {
        var updated = createUpdated();
        service.update(new User(updated));
        assertMatch(service.get(USER_ID), updated);
    }

    @Test
    void  getAll() {
        assertMatch(service.getAll(), ADMIN, USER);
    }
}
