package ru.javaops.topjava.web.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.javaops.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javaops.topjava.util.exeption.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.topjava.UserTestData.ADMIN;
import static ru.javaops.topjava.UserTestData.USER_ID;

@SpringJUnitWebConfig(locations = {"classpath:spring/spring-app.xml", "classpath:spring/in-memory.xml"})
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
    void delete() {
        controller.delete(USER_ID);
        var users = controller.getAll();
        assertEquals(1, users.size());
        assertEquals(ADMIN, users.get(0));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(0));
    }
}
