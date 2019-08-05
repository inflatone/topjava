package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Collections.emptySet;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.model.Role.ROLE_USER;


public abstract class AbstractUserServiceTest extends AbstractServiceTest {
    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() {
        cacheManager.getCache("users").clear();
    }

    @Test
    public void create() {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(ROLE_USER));
        User created = service.create(newUser);
        newUser.setId(created.getId());
        assertMatch(created, newUser);
        assertMatch(service.getAll(), ADMIN, newUser, USER);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateMailCreate() {
        service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", ROLE_USER));
    }

    @Test
    public void delete() {
        service.delete(USER_ID);
        assertMatch(service.getAll(), ADMIN);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(1);
    }

    @Test
    public void get() {
        User user = service.get(USER_ID);
        assertMatch(user, USER);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(1);
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail("user@yandex.ru");
        assertMatch(user, USER);
    }

    @Test
    public void update() {
        User updated = new User(USER);
        updated.setName("UpdatedNamed");
        updated.setCaloriesPerDay(1000);
        service.update(updated);
        assertMatch(service.get(USER_ID), updated);
    }

    @Test
    public void getAll() {
        List<User> users = service.getAll();
        assertMatch(users, ADMIN, USER);
    }

    @Test
    public void testValidation() throws Exception {
        validateRootCause(() -> service.create(new User(null, " ", "mail@yandex.ru", "password", ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", " ", "password", ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", " ", ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), emptySet())), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), emptySet())), ConstraintViolationException.class);
    }
}