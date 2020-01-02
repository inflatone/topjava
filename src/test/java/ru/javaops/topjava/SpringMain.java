package ru.javaops.topjava;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.web.meal.AbstractMealController;
import ru.javaops.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static ru.javaops.topjava.UserTestData.printBeans;

public class SpringMain {
    public static void main(String[] args) {
        try (var context = new ClassPathXmlApplicationContext()) {
            context.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile(), Profiles.REPOSITORY_IMPLEMENTATION);
            context.setConfigLocations("spring/spring-app.xml", "spring/in-memory.xml");
            context.refresh();
            printBeans(context);
            var userController = context.getBean(AdminRestController.class);
            var user = new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN);
            userController.create(user);

            var mealController = context.getBean(AbstractMealController.class);
            var meals = mealController.getBetween(
                    LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                    LocalDate.of(2015, Month.MAY, 31), LocalTime.of(11, 0)
            );
            meals.forEach(System.out::println);
        }
    }
}