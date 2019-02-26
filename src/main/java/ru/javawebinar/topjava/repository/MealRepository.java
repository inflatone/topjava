package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal, int userId);

    boolean delete(int id, int userId);

    Meal get(int id, int userId);

    // ORDERED dateTime desc
    List<Meal> getAll(int userId);

    // ORDERED dateTime desc
    List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId);
}
