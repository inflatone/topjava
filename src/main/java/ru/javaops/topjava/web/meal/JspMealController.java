package ru.javaops.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javaops.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javaops.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @Autowired
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("/create")
    public String create(Model model) {
        var meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request, Model model) {
        var meal = super.get(getId(request));
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = getId(request);
        super.delete(id);
        return "redirect:/meals";
    }

    @PostMapping("/filter")
    public String filter(HttpServletRequest request, Model model) {
        var startDate = parseLocalDate(request.getParameter("startDate"));
        var endDate = parseLocalDate(request.getParameter("endDate"));
        var startTime = parseLocalTime(request.getParameter("startTime"));
        var endTime = parseLocalTime(request.getParameter("endTime"));

        var filteredMeals = super.getBetween(startDate, startTime, endDate, endTime);
        model.addAttribute("meals", filteredMeals);
        return "meals";
    }

    @PostMapping
    public String save(HttpServletRequest request) {
        final var meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        String idLine = request.getParameter("id");
        if (StringUtils.isEmpty(idLine)) {
            super.create(meal);
        } else {
            super.update(meal, Integer.parseInt(idLine));
        }
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
