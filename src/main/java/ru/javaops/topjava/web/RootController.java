package ru.javaops.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.javaops.topjava.service.MealService;
import ru.javaops.topjava.util.MealsUtil;

@Controller
public class RootController {
    private final MealService mealService;

    @Autowired
    public RootController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:meals";
    }

    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/meals")
    public String getMeals(Model model) {
        model.addAttribute("meals", MealsUtil.getTOs(mealService.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }
}