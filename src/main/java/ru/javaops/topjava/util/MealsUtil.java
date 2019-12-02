package ru.javaops.topjava.util;

import org.springframework.lang.Nullable;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    private MealsUtil() {
    }

    public static List<MealTo> getTOs(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredTOs(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTOs(Collection<Meal> meals, int caloriesPerDay, @Nullable LocalTime startTime, @Nullable LocalTime endTime) {
        return getFilteredTOs(meals, caloriesPerDay, m -> Util.isBetweenInclusive(m.getTime(), startTime, endTime));
    }

    private static List<MealTo> getFilteredTOs(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        final Map<LocalDate, Integer> caloriesSumPerDay = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
        return meals.stream().filter(filter)
                .map(m -> createTO(m, caloriesSumPerDay.get(m.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static MealTo createTO(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}