package ru.javaops.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.repository.MealRepository;
import ru.javaops.topjava.util.Util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javaops.topjava.MealTestData.MEALS;
import static ru.javaops.topjava.UserTestData.USER_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    // userId -> (mealId-> meal)
    private Map<Integer, InMemoryBaseRepository<Meal>> repositories = new ConcurrentHashMap<>();

    {
        var userMeals = new InMemoryBaseRepository<Meal>();
        repositories.put(USER_ID, userMeals);
        MEALS.forEach(meal -> userMeals.storage.put(meal.getId(), meal));
    }

    @PostConstruct
    public void postConstruct() {
        log.info("+++ PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("+++ PreDestroy");
    }


    @Override
    public Meal save(Meal meal, int userId) {
        Objects.requireNonNull(meal, "meal must not be null");
        var meals = repositories.computeIfAbsent(userId, uId -> new InMemoryBaseRepository<>());
        return meals.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        var meals = repositories.get(userId);
        return meals != null && meals.delete(id);
    }

    @Override
    public Meal get(int id, int userId) {
        var meals = repositories.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDate start, LocalDate end, int userId) {
        return getAllFiltered(userId, meal -> Util.isBetweenInclusive(meal.getDate(), start, end));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        var meals = repositories.get(userId);
        return meals == null ? Collections.emptyList()
                : meals.getCollection()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}