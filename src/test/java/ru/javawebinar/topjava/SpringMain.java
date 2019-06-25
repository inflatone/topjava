package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.AdminRestController;
import ru.javawebinar.topjava.web.MealRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("bean definition names:");
            for (String name : context.getBeanDefinitionNames()) {
                System.out.println(name);
            }
            System.out.println();
            AdminRestController controller = context.getBean(AdminRestController.class);
            controller.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            System.out.println();

            MealRestController mealController = context.getBean(MealRestController.class);
            List<MealTo> filteredMealsWithExcess = mealController.getBetween(
                    LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                    LocalDate.of(2015, Month.MAY, 31), LocalTime.of(11, 0)
            );
            filteredMealsWithExcess.forEach(System.out::println);
        }
    }


}
