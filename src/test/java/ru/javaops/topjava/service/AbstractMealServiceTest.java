package ru.javaops.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.util.exeption.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.topjava.MealTestData.*;
import static ru.javaops.topjava.UserTestData.ADMIN_ID;
import static ru.javaops.topjava.UserTestData.USER_ID;

public abstract class AbstractMealServiceTest extends AbstractServiceTest {
    @Autowired
    protected MealService service;

    @Test
    void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(0, USER_ID));
    }

    @Test
    void create() {
        var newMeal = createNew();
        var created = service.create(newMeal, USER_ID);
        int newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHERS.assertMatch(created, newMeal);
        MEAL_MATCHERS.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    void get() {
        var actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHERS.assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(0, USER_ID));
    }

    @Test
    void update() {
        var updated = createUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHERS.assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> service.update(MEAL1, ADMIN_ID));
    }

    @Test
    void getAll() {
        var meals = service.getAll(USER_ID);
        MEAL_MATCHERS.assertMatch(meals, MEALS);
    }

    @Test
    void getBetween() {
        var meals = service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30),
                USER_ID
        );
        MEAL_MATCHERS.assertMatch(meals, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void getBetweenWithNullDates() {
        MEAL_MATCHERS.assertMatch(service.getBetweenDates(null, null, USER_ID), MEALS);
    }

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