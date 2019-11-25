package ru.javaops.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javaops.topjava.service.MealService;

@Controller
public class MealRestController extends AbstractMealController {
    @Autowired
    public MealRestController(MealService service) {
        super(service);
    }
}
