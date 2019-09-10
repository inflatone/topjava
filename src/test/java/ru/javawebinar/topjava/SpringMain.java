package ru.javawebinar.topjava;

import org.springframework.context.support.GenericXmlApplicationContext;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.TestUtil.mockAuthorize;
import static ru.javawebinar.topjava.UserTestData.USER;

public class SpringMain {
    public static void main(String[] args) {
        try (GenericXmlApplicationContext context = new GenericXmlApplicationContext ()) {
            context.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile(), Profiles.REPOSITORY_IMPLEMENTATION);
            context.load("spring/spring-app.xml", "spring/inmemory.xml");
            context.refresh();

            mockAuthorize(USER);

            System.out.println("bean definition names:");
            for (String name : context.getBeanDefinitionNames()) {
                System.out.println(name);
            }
            System.out.println();
            AdminRestController controller = context.getBean(AdminRestController.class);
            controller.getAll();
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
