package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    /**
     * Доделать см. пункт 6 Типичных ошибок
     */
    public List<MealTo> getFilteredAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getFilteredAll");

        if (startDate == null && startTime == null && endDate == null && endTime == null) return getAll();

        if (startDate == null) {
            startDate = LocalDate.MIN;
        }
        if (endDate == null) {
            endDate = LocalDate.MAX;
        }
        if (startTime == null) {
            startTime = LocalTime.MIN;
        }
        if (endTime == null) {
            endTime = LocalTime.MAX;
        }

        List<Meal> mealList = service.getAllByFilteredDate(SecurityUtil.authUserId(), startDate, endDate);
        return MealsUtil.getFilteredTos(mealList, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);

    }

    public Meal getById(int id) {
        log.info("get Meal By {}", id);
        return service.get(SecurityUtil.authUserId(), id);
    }

    public void deleteById(int id) {
        log.info("delete Meal By {}", id);
        service.delete(SecurityUtil.authUserId(), id);
    }

    public Meal create(Meal meal) {
        log.info("save {}", meal);
        return service.create(SecurityUtil.authUserId(), meal);
    }

    public void update(int id, Meal meal) {
        log.info("update {}", meal);
        service.update(SecurityUtil.authUserId(), meal);
    }


}