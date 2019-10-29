package ru.javaops.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.web.user.AdminRestController;

public class SpringMain {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            printBeans(context);
            AdminRestController controller = context.getBean(AdminRestController.class);
            User user = new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN);
            controller.create(user);
        }
    }

    private static void printBeans(ConfigurableApplicationContext springContext) {
        System.out.println("Bean definition names: ");
        for (String beanName : springContext.getBeanDefinitionNames()) {
            System.out.println(' ' + beanName);
        }
        System.out.println();
    }
}