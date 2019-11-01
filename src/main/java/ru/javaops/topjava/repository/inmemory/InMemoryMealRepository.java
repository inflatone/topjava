package ru.javaops.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.repository.MealRepository;
import ru.javaops.topjava.util.MealsUtil;
import ru.javaops.topjava.util.Util;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javaops.topjava.repository.inmemory.InMemoryUserRepository.ADMIN_ID;
import static ru.javaops.topjava.repository.inmemory.InMemoryUserRepository.USER_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {

    // userId -> (mealId-> meal)
    private Map<Integer, InMemoryBaseRepository<Meal>> repositories = new ConcurrentHashMap<>();

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, USER_ID));

        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500), ADMIN_ID);
    }


    @Override
    public Meal save(Meal meal, int userId) {
        InMemoryBaseRepository<Meal> meals = repositories.computeIfAbsent(userId, uId -> new InMemoryBaseRepository<>());
        return meals.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        InMemoryBaseRepository<Meal> meals = repositories.get(userId);
        return meals != null && meals.delete(id);
    }

    @Override
    public Meal get(int id, int userId) {
        InMemoryBaseRepository<Meal> meals = repositories.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime start, LocalDateTime end, int userId) {
        return getAllFiltered(userId, meal -> Util.isBetweenInclusive(meal.getDateTime(), start, end));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        InMemoryBaseRepository<Meal> meals = repositories.get(userId);
        return meals == null ? Collections.emptyList()
                : meals.getCollection()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}