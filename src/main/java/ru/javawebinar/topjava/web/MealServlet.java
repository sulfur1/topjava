package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() throws ServletException {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (id.equals("filter")) {
            String stDate = request.getParameter("startDate");
            LocalDate startDate = stDate.isEmpty() ? null : LocalDate.parse(stDate, formatterDate);

            String eDate = request.getParameter("endDate");
            LocalDate endDate = eDate.isEmpty() ? null : LocalDate.parse(eDate, formatterDate);

            String stTime = request.getParameter("startTime");
            LocalTime startTime = stTime.isEmpty() ? null : LocalTime.parse(stTime, formatterTime);

            String eTime = request.getParameter("endTime");
            LocalTime endTime = eTime.isEmpty() ? null : LocalTime.parse(eTime, formatterTime);

            request.setAttribute("meals", mealRestController.getFilteredAll(startDate, startTime, endDate, endTime));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            //repository.save(meal);
            if (meal.isNew()) {
                mealRestController.create(meal);
            } else {
                mealRestController.update(Integer.parseInt(id), meal);
            }
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete": {
                String id = request.getParameter("id");
                mealRestController.deleteById(Integer.parseInt(id));
                response.sendRedirect("meals");
                break;
            }
            case "create": {
                log.info("create meal");
                Meal meal = new Meal(LocalDateTime.MIN, "", 0);
                request.setAttribute("meal",
                        meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            }
            case "update": {
                log.info("update meal");
                Meal meal = mealRestController.getById(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            }
            case "all":
            default: {
                log.info("getAll");
                request.setAttribute("userId", SecurityUtil.authUserId());
                request.setAttribute("meals",
                        mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            }
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        appCtx.close();
    }
}
