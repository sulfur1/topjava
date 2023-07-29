package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static org.springframework.format.annotation.DateTimeFormat.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@RestController
@RequestMapping(value = MealRestController.REST_URL_MEALS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {
    public static final String REST_URL_MEALS = "/rest/meals/";

    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal mealCreated = super.create(meal);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_MEALS + "/{id}")
                .buildAndExpand(mealCreated.id()).toUri();

        return ResponseEntity.created(uri).body(mealCreated);
    }

    @GetMapping("/filter")
    public List<MealTo> getBetween
            (
                    @RequestParam LocalDate startDate,
                    @RequestParam LocalDate endDate,
                    @RequestParam LocalTime startTime,
                    @RequestParam LocalTime endTime
            )
    {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}