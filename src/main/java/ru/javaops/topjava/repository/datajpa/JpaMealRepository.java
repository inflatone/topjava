package ru.javaops.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JpaMealRepository implements MealRepository {
    @Autowired
    private CrudMealRepository crudMealRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return null;
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDate startDate, LocalDate endDate, int userId) {
        return null;
    }
}
