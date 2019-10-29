package ru.javaops.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javaops.topjava.repository.UserRepository;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        System.out.println("Bean definition names: " + Arrays.toString(context.getBeanDefinitionNames()));

        //UserRepository repository = (UserRepository) context.getBean("inmemoryUserRepository");

        UserRepository repository = context.getBean(UserRepository.class);
        System.out.println(repository.getAll());
        context.close();
    }
}
