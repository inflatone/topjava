package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class InMemoryAdminRestControllerTest {
    private static ConfigurableApplicationContext context;
    private static AdminRestController controller;

    @BeforeAll
    static void beforeClass() {
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/inmemory.xml");
        System.out.println('\n' + Arrays.toString(context.getBeanDefinitionNames()) + '\n');
        controller = context.getBean(AdminRestController.class);
    }

    @AfterAll
    static void afterClass() {
//        May cause during JUnit "Cache is not alive (STATUS_SHUTDOWN)" as JUnit share Spring context for speed
//        http://stackoverflow.com/questions/16281802/ehcache-shutdown-causing-an-exception-while-running-test-suite
//        context.close();
    }

    @BeforeEach
    void setUp() {
        // re-initialize
        InMemoryUserRepository repository = context.getBean(InMemoryUserRepository.class);
        repository.init();
    }

    @Test
    void delete() {
        controller.delete(USER_ID);
        Collection<User> users = controller.getAll();
        assertEquals(1, users.size());
        assertEquals(ADMIN, users.iterator().next());
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(10));
    }
}