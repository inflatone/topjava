package ru.javaops.topjava.repository;

import ru.javaops.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MealRepository {
    /**
     * @return null if updated meal do not belong to userId
     */
    Meal save(Meal meal, int userId);

    /**
     * @return false if meal do not belong to userId
     */
    boolean delete(int id, int userId);

    /**
     * @return null if meal do not belong to userId
     */
    Meal get(int id, int userId);

    /**
     * @return ORDERED dateTime desc
     */
    List<Meal> getAll(int userId);

    /**
     * @return ORDERED dateTime desc
     */
    List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId);
}
