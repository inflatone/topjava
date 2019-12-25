package ru.javaops.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.JpaUtil;
import ru.javaops.topjava.util.exeption.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static ru.javaops.topjava.UserTestData.*;
import static ru.javaops.topjava.model.Role.ROLE_USER;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {
    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    protected JpaUtil jpaUtil;

    @Before
    public void setUp() {
        Optional.ofNullable(cacheManager.getCache("users")).ifPresent(Cache::clear);
        jpaUtil.clear2ndLevelHibernateCache();
    }

    @Test
    public void create() {
        var newUser = createNew();
        var created = service.create(newUser);
        var newId = created.getId();
        newUser.setId(newId);
        assertMatch(created, newUser);
        assertMatch(service.get(newId), newUser);
    }

    @Test(expected = DataAccessException.class)
    public void createDuplicateEmail() {
        service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", ROLE_USER));
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(USER_ID);
        service.get(USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(0);
    }

    @Test
    public void get() {
        var user = service.get(USER_ID);
        assertMatch(user, USER);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(0);
    }

    @Test
    public void getByEmail() {
        var user = service.getByEmail(USER.getEmail());
        assertMatch(user, USER);
    }

    @Test
    public void update() {
        var updated = createUpdated();
        service.update(updated);
        assertMatch(service.get(USER_ID), updated);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(), ADMIN, USER);
    }

    @Test
    public void testValidation() {
        testCreateValidation(new User(null, "  ", "mail@yandex.ru", "password", ROLE_USER));
        testCreateValidation(new User(null, "User", "  ", "password", ROLE_USER));
        testCreateValidation(new User(null, "User", "mail@yandex.ru", "  ", ROLE_USER));
        testCreateValidation(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of()));
        testCreateValidation(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of()));
    }

    private void testCreateValidation(User user) {
        validateRootCause(() -> service.create(user), ConstraintViolationException.class);
    }
}
