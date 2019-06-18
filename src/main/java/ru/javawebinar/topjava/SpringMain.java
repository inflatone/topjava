package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.AdminRestController;

import java.util.Arrays;

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
        }
    }


}
