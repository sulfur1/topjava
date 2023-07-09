package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public class DataJpaUserServiceTest extends AbstractServiceTest{
    @Autowired
    private UserService service;

    @Test
    public void get() {
        User actual = service.get(ADMIN_ID);
        USER_MATCHER.assertMatch(actual, admin);
    }
}
