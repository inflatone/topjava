package ru.javaops.topjava.repository;

import org.springframework.lang.Nullable;
import ru.javaops.topjava.model.Meal;

import java.time.LocalDate;
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
    List<Meal> getBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId);
}
