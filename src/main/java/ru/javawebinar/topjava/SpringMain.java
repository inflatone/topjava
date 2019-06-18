package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("bean definition names: " + Arrays.toString(context.getBeanDefinitionNames()));

//        UserRepository repository = (UserRepository) context.getBean("inmemoryUserRepository");
            UserRepository repository = context.getBean(UserRepository.class);
            List<User> users = repository.getAll();
            System.out.println(users.size());

            MealRepository mealRepository = context.getBean(MealRepository.class);
            mealRepository.getAll().forEach(System.out::println);
        }
    }


}
