package ru.javaops.topjava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.JpaUtil;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;

import static ru.javaops.topjava.model.Role.ROLE_USER;

public abstract class AbstractJpaUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    protected JpaUtil jpaUtil;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        jpaUtil.clear2ndLevelHibernateCache();
    }

    @Test
    void createWithException() {
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
