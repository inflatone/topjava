package ru.javaops.topjava.web.user;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javaops.topjava.util.exeption.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.javaops.topjava.UserTestData.*;

public class InMemoryAdminRestControllerTest {
    private static ConfigurableApplicationContext appContext;
    private static AdminRestController controller;

    @BeforeClass
    public static void beforeClass() {
        appContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/in-memory.xml");
        printBeans(appContext);
        controller = appContext.getBean(AdminRestController.class);
    }

    @AfterClass
    public static void afterClass() {
        appContext.close();
    }

    @Before
    public void setUp() {
        // re-initialize
        InMemoryUserRepository repository = appContext.getBean(InMemoryUserRepository.class);
        repository.init();
    }

    @Test
    public void delete() {
        controller.delete(USER_ID);
        List<User> users = controller.getAll();
        assertEquals(1, users.size());
        assertEquals(ADMIN, users.get(0));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        controller.delete(0);
    }
}
