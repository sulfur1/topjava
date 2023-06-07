package ru.javawebinar.topjava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepositoryImpl implements MealRepository {

    private static final MealRepository mealRepository = new MealRepositoryImpl();

    public static MealRepository getMealRepository() {
        return mealRepository;
    }
    private MealRepositoryImpl() {}

    private static final AtomicInteger COUNTER_ID = new AtomicInteger(1);
    private static final ConcurrentHashMap<Integer, Meal> DATA_OF_MEAL = new ConcurrentHashMap<>();

    @Override
    public void createMeal(Meal meal) {
        synchronized (DATA_OF_MEAL) {
            DATA_OF_MEAL.put(meal.getId(), meal);
        }
    }

    @Override
    public Meal readMeal(int id) {
        return DATA_OF_MEAL.get(id);
    }

    @Override
    public List<Meal> readAllMeal() {
        if (DATA_OF_MEAL.isEmpty()) {
            return Collections.emptyList();
        } else {
            return new ArrayList<Meal>(DATA_OF_MEAL.values());
        }
    }

    @Override
    public void updateMeal(Meal meal) {
        synchronized (DATA_OF_MEAL) {
            DATA_OF_MEAL.put(meal.getId(), meal);
        }
    }

    @Override
    public void deleteMeal(int id) {
        synchronized (DATA_OF_MEAL) {
            DATA_OF_MEAL.remove(id);
        }
    }

    public int getCounterId() {
        synchronized (this) {
            return COUNTER_ID.getAndIncrement();
        }
    }
}
