package ru.javaops.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }

    public static void printBeans(ConfigurableApplicationContext springContext) {
        System.out.println("\nBean definition names: ");
        for (String beanName : springContext.getBeanDefinitionNames()) {
            System.out.println(' ' + beanName);
        }
        System.out.println();
    }
}
