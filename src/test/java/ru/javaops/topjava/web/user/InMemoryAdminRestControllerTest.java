package ru.javaops.topjava.web.user;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javaops.topjava.util.exeption.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.topjava.UserTestData.*;

class InMemoryAdminRestControllerTest {
    private static ConfigurableApplicationContext appContext;
    private static AdminRestController controller;

    @BeforeAll
    static void beforeClass() {
        appContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/in-memory.xml");
        printBeans(appContext);
        controller = appContext.getBean(AdminRestController.class);
    }

    @AfterAll
    static void afterClass() {
//        May cause during JUnit "Cache is not alive (STATUS_SHUTDOWN)" as JUnit share Spring context for speed
//        http://stackoverflow.com/questions/16281802/ehcache-shutdown-causing-an-exception-while-running-test-suite
//        appCtx.close();
    }

    @BeforeEach
    void setUp() {
        // re-initialize
        InMemoryUserRepository repository = appContext.getBean(InMemoryUserRepository.class);
        repository.init();
    }

    @Test
    void delete() {
        controller.delete(USER_ID);
        List<User> users = controller.getAll();
        assertEquals(1, users.size());
        assertEquals(ADMIN, users.get(0));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(0));
    }
}
