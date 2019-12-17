package ru.javaops.topjava.repository.datajpa;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.topjava.model.Meal;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
}
