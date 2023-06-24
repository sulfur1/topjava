package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


public class MealTestData {
    public static final int START_SEQ_MEAL = 200000;
    public static final int MEAL_ID_OF_30_01_2020_10_00 = START_SEQ_MEAL;
    public static final int MEAL_ID_OF_31_01_2020_00_00 = START_SEQ_MEAL + 3;
    public static final int MEAL_ID_OF_31_01_2020_10_00 = START_SEQ_MEAL + 4;
    public static final int NOT_FOUND = START_SEQ_MEAL + 12;

    public static final Meal MEAL_OF_30_01_2020_10_00 = new Meal(200000, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_OF_31_01_2020_00_00 = new Meal(200003, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal MEAL_OF_31_01_2020_10_00 = new Meal(200004, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);


    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2020, Month.JANUARY, 28, 10, 0), "Завтрак", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL_OF_31_01_2020_10_00);
        updated.setDateTime(LocalDateTime.of(2020, Month.JANUARY, 27, 0, 0));
        updated.setDescription("Полдник");
        updated.setCalories(700);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
