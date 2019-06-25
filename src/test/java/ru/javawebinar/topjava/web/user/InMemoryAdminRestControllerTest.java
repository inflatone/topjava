package ru.javawebinar.topjava.web.user;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.web.AdminRestController;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class InMemoryAdminRestControllerTest {
    private static ConfigurableApplicationContext context;
    private static AdminRestController controller;

    @BeforeClass
    public static void beforeClass() {
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        System.out.println('\n' + Arrays.toString(context.getBeanDefinitionNames()) + '\n');
        controller = context.getBean(AdminRestController.class);
    }

    @AfterClass
    public static void afterClass() {
        context.close();
    }

    @Before
    public void setUp() {
        // re-initialize
        InMemoryUserRepository repository = context.getBean(InMemoryUserRepository.class);
        repository.init();
    }

    @Test
    public void delete() {
        controller.delete(USER_ID);
        Collection<User> users = controller.getAll();
        assertEquals(1, users.size());
        assertEquals(ADMIN, users.iterator().next());
    }
}
