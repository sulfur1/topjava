package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(SecurityUtil.authUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        repository.putIfAbsent(userId, new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        Meal meal = repository.get(userId).get(id);
        if (meal != null && meal.getUserId() == userId) {
            repository.get(userId).remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        Meal meal = repository.get(userId).get(id);
        if (meal != null && meal.getUserId() == userId) {
            return repository.get(userId).get(id);
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        List<Meal> listMeals = new ArrayList<>(repository.get(userId).values());

        if (listMeals.isEmpty()) return Collections.emptyList();

        return listMeals.stream().sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime())).collect(Collectors.toList());
    }

    public List<Meal> getAllByFilteredDate(int userId, LocalDate startDate, LocalDate endDate) {
        Map<Integer, Meal> mealList = repository.get(userId);
        return mealList == null
                ? Collections.emptyList()
                : mealList.values().stream()
                .filter(meal -> meal.getDate().compareTo(startDate) >= 0 && meal.getDate().compareTo(endDate) <= 0)
                .collect(Collectors.toList());
    }
}

