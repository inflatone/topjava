package ru.javaops.topjava.repository;

import ru.javaops.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    Meal save(Meal meal);

    //    false if not found
    boolean delete(int id);

    //    null if not found
    Meal get(int id);

    Collection<Meal> getAll();
}
