package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(), "description", 500);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), newMeal, SIXTH_MEAL, FIFTH_MEAL, FORTH_MEAL, THIRD_MEAL, SECOND_MEAL, FIRST_MEAL);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateCreate() {
        service.create(new Meal(FIRST_MEAL.getDateTime(), "lunch", 555), USER_ID);
    }

    @Test
    public void delete() {
        service.delete(FIRST_MEAL.getId(), USER_ID);
        assertMatch(service.getAll(USER_ID), SIXTH_MEAL, FIFTH_MEAL, FORTH_MEAL, THIRD_MEAL, SECOND_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnotherUserMeal() {
        service.delete(FIRST_ADMIN_MEAL.getId(), USER_ID);
    }

    @Test
    public void get() {
        Meal meal = service.get(SECOND_ADMIN_MEAL.getId(), ADMIN_ID);
        assertMatch(meal, SECOND_ADMIN_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUserMeal() {
        service.get(SECOND_ADMIN_MEAL.getId(), USER_ID);
    }

    @Test
    public void update() {
        Meal updated = new Meal(THIRD_MEAL);
        updated.setCalories(0);
        updated.setDescription("UpdatedDesc");
        service.update(updated, USER_ID);
        assertMatch(service.get(THIRD_MEAL.getId(), USER_ID), updated);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, SECOND_ADMIN_MEAL, FIRST_ADMIN_MEAL);
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime start = LocalDateTime.of(FIRST_MEAL.getDate(), LocalTime.MIN);
        LocalDateTime finish = LocalDateTime.of(FIRST_MEAL.getDate(), LocalTime.MAX);
        List<Meal> filteredMeals = service.getBetweenDateTimes(start, finish, USER_ID);
        assertMatch(filteredMeals, THIRD_MEAL, SECOND_MEAL, FIRST_MEAL);
    }


}