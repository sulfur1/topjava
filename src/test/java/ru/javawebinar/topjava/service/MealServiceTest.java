package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(MEAL_ID_OF_30_01_2020_10_00, USER_ID);
        assertMatch(MealTestData.MEAL_OF_30_01_2020_10_00, meal);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }
    @Test
    public void delete() {
        mealService.delete(MEAL_ID_OF_30_01_2020_10_00, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID_OF_30_01_2020_10_00, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        Predicate<Meal> predicate = meal -> meal.getId() == 200000 || meal.getId() == 200003 || meal.getId() == 200004;
        List<Meal> meals = mealService.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 31),
                USER_ID).stream().filter(predicate).collect(Collectors.toList());
        assertMatch(meals, MEAL_OF_31_01_2020_10_00, MEAL_OF_31_01_2020_00_00, MEAL_OF_30_01_2020_10_00);
    }

    @Test
    public void getAll() {
        Predicate<Meal> predicate = meal -> meal.getId() == 200000 || meal.getId() == 200003 || meal.getId() == 200004;
        List<Meal> meals = mealService.getAll(USER_ID).stream().filter(predicate).collect(Collectors.toList());
        assertMatch(meals, MEAL_OF_31_01_2020_10_00, MEAL_OF_31_01_2020_00_00, MEAL_OF_30_01_2020_10_00);
    }

    @Test
    public void update() {
        Meal meal = getUpdated();
        mealService.update(meal, USER_ID);
        assertMatch(mealService.get(meal.getId(), USER_ID), getUpdated());
    }

    @Test
    public void duplicateMealCreated() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500), USER_ID));
    }
    @Test
    public void create() {
        Meal createdMeal = mealService.create(getNew(), USER_ID);
        Meal newMeal = getNew();
        newMeal.setId(200010);
        assertMatch(createdMeal, newMeal);
        assertMatch(mealService.get(createdMeal.getId(), USER_ID), newMeal);
    }
}