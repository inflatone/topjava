package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.JpaUtil;

import javax.validation.ConstraintViolationException;
import java.util.Date;

import static java.util.Collections.emptySet;
import static ru.javawebinar.topjava.model.Role.ROLE_USER;

public abstract class AbstractJpaUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    private JpaUtil jpaUtil;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        jpaUtil.clear2ndLevelHibernateCache();
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