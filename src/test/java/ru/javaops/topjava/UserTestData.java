package ru.javaops.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;

public class UserTestData {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);

    public static void printBeans(ConfigurableApplicationContext springContext) {
        System.out.println("\nBean definition names: ");
        for (String beanName : springContext.getBeanDefinitionNames()) {
            System.out.println(' ' + beanName);
        }
        System.out.println();
    }
}
