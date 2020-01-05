package ru.javaops.topjava.service;

import org.junit.jupiter.api.Test;
import ru.javaops.topjava.model.Meal;

import javax.validation.ConstraintViolationException;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static ru.javaops.topjava.UserTestData.USER_ID;

public abstract class AbstractJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    void createWithException() {
        var dateTime = of(2015, Month.JUNE, 1, 18, 0);
        validateException(new Meal(null, dateTime, "  ", 300));
        validateException(new Meal(null, null, "description", 300));
        validateException(new Meal(null, dateTime, "description", 9));
        validateException(new Meal(null, dateTime, "description", 5001));
    }

    private void validateException(Meal meal) {
        validateRootCause(() -> service.create(meal, USER_ID), ConstraintViolationException.class);
    }
}
