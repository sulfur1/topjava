package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealRepository;
import ru.javawebinar.topjava.model.MealRepositoryImpl;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * MealServlet
 * Обрабатывает GET и POST запросы на /meals
 *
 */
public class MealServlet extends HttpServlet {

    private static final MealRepository mealRepository = MealRepositoryImpl.getMealRepository();
    private static final int caloriesPerDay = 1000;

    private static final String ADD_MEAL = "/add_meal.jsp";
    private static final String EDIT_MEAL = "/edit_meal.jsp";
    private static final String DELETE_OR_LIST_MEALS = "/meals.jsp";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    private static final Logger log = getLogger(MealServlet.class);


    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     *
     * req параметры:
     * 1. edit - достает из параметра mealId, находит нужный в repository и перенаправляет на edit_meal.jsp
     * 2. delete - удаляет запись с последующим редиректом на /meals?action=listMeals
     * 3. listMeals - выводит все записи на страницу из хранилища
     * 4. default - перенаправление на add_meal.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String forward = "";
        String action = req.getParameter("action");

        switch (action) {
            case "edit": {
                log.debug("Редактирование записи еды");
                forward = EDIT_MEAL;
                Integer mealId = Integer.parseInt(req.getParameter("mealId"));
                Meal meal = mealRepository.readMeal(mealId);
                req.setAttribute("meal", meal);

                break;
            }
            case "delete": {
                log.debug("Запись еды удалена");
                forward = DELETE_OR_LIST_MEALS;
                int mealId = Integer.parseInt(req.getParameter("mealId"));
                mealRepository.deleteMeal(mealId);
                resp.sendRedirect(req.getContextPath() + "/meals?action=listMeals");

                return;
            }
            case "listMeals": {
                log.debug("Отобразить всю еду");
                forward = DELETE_OR_LIST_MEALS;
                if (!mealRepository.readAllMeal().isEmpty()) {
                    List<MealTo> mealTo = MealsUtil.filteredByStreams(mealRepository.readAllMeal(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
                    req.setAttribute("meals", mealTo);
                }
                break;
            }
            default: {
                forward = ADD_MEAL;

            }
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(forward);
        requestDispatcher.forward(req, resp);

    }

    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     *
     * doPost() - обрабатывает post-запросы с add_meal.jsp и edit_meal.jsp
     * edit_meal.jsp в параметрах запроса отправляет существующий mealId
     * Создается новый объект Meal
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String mealId = req.getParameter("mealId");
        int idMeal;

        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), FORMATTER);
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        if (mealId == null || mealId.isEmpty()) {
            log.debug("Добавление новой записи");
            idMeal = mealRepository.getCounterId();
            Meal newMeal = new Meal(idMeal, dateTime, description, calories);
            mealRepository.createMeal(newMeal);
        } else {
            idMeal = Integer.parseInt(req.getParameter("mealId"));
            Meal newMeal = new Meal(idMeal, dateTime, description, calories);
            mealRepository.updateMeal(newMeal);
        }


        List<MealTo> mealTo = MealsUtil.filteredByStreams(mealRepository.readAllMeal(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
        req.setAttribute("meals", mealTo);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(DELETE_OR_LIST_MEALS);
        requestDispatcher.forward(req, resp);
    }
}
