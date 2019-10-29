package ru.javaops.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.UserRepository;
import ru.javaops.topjava.service.UserService;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        System.out.println("Bean definition names: " + Arrays.toString(context.getBeanDefinitionNames()));

        //UserRepository repository = (UserRepository) context.getBean("inmemoryUserRepository");

        UserRepository repository = context.getBean(UserRepository.class);
        System.out.println(repository.getAll());

        UserService userService = context.getBean(UserService.class);
        userService.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));

        context.close();
    }
}
