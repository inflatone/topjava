package ru.javaops.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.service.MealService;
import ru.javaops.topjava.to.MealTo;
import ru.javaops.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaops.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava.util.ValidationUtil.checkNew;
import static ru.javaops.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javaops.topjava.web.SecurityUtil.authUserId;

@Controller
public abstract class AbstractMealController {
    private final Logger log = getLogger(getClass());

    private final MealService service;

    @Autowired
    public AbstractMealController(MealService service) {
        this.service = service;
    }

    public Meal get(int id) {
        int userId = authUserId();
        log.info("get meal {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public void delete(int id) {
        int userId = authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public List<MealTo> getAll() {
        int userId = authUserId();
        log.info("getAll for user {}", userId);
        return MealsUtil.getTOs(service.getAll(userId), authUserCaloriesPerDay());
    }

    public Meal create(Meal meal) {
        int userId = authUserId();
        checkNew(meal);
        log.info("create {} for user {}", meal, userId);
        return service.create(meal, userId);
    }

    public void update(Meal meal, int id) {
        int userId = authUserId();
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> dateFilteredMeals = service.getBetweenDates(startDate, endDate, userId);
        return MealsUtil.getFilteredTOs(dateFilteredMeals, authUserCaloriesPerDay(), startTime, endTime);
    }
}
