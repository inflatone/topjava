package ru.javaops.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class JpaMealRepository implements MealRepository {
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
        return Collections.emptyList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return Collections.emptyList();
    }
}
