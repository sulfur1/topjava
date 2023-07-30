package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "meals");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int GUEST_ID = START_SEQ + 2;
    public static final int NOT_FOUND = 10;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);
    public static final User guest = new User(GUEST_ID, "Guest", "guest@gmail.com", "guest");

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUserWithMeals() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User userWithMeals = new User(user);
        Class<? extends User> clazz = userWithMeals.getClass();
        Method methodSetMeals = clazz.getDeclaredMethod("setMeals", List.class);

        methodSetMeals.setAccessible(true);
        methodSetMeals.invoke(userWithMeals, meals);

        return userWithMeals;
    }
    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }
}
