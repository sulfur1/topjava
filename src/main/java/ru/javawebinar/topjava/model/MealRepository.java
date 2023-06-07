package ru.javawebinar.topjava.model;

import java.util.List;

public interface MealRepository {
    void createMeal(Meal meal);

    Meal readMeal(int id);

    List<Meal> readAllMeal();

    void updateMeal(Meal meal);

    void deleteMeal(int id);

    int getCounterId();
}
