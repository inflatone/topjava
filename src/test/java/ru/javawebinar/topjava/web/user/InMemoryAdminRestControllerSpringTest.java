package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@SpringJUnitConfig(locations = {"classpath:spring/spring-app.xml", "classpath:spring/inmemory.xml"})
class InMemoryAdminRestControllerSpringTest {
    @Autowired
    private AdminRestController controller;

    @Autowired
    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository.init();
    }

    @Test
    void testDelete() {
        controller.delete(USER_ID);
        List<User> users = controller.getAll();
        assertEquals(1, users.size());
        assertEquals(ADMIN, users.iterator().next());
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(0));
    }
}