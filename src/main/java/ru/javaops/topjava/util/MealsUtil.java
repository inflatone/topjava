package ru.javaops.topjava.util;

import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
    );

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealTo> getTOs(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredTOs(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTOs(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return getFilteredTOs(meals, caloriesPerDay, m -> Util.isBetweenInclusive(m.getTime(), startTime, endTime));
    }

    private static List<MealTo> getFilteredTOs(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        final Map<LocalDate, Integer> caloriesSumPerDay = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
        return meals.stream().filter(filter)
                .map(m -> createTO(m, caloriesSumPerDay.get(m.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTO(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}