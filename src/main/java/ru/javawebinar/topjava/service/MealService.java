package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {
    Meal get(int id);

    void delete(int id);

    List<Meal> getAll();

    void update(Meal meal);

    void create(Meal meal);
}
