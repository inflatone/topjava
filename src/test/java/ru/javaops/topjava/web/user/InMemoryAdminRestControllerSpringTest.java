package ru.javaops.topjava.web.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javaops.topjava.util.exeption.NotFoundException;
import ru.javaops.topjava.web.user.AdminRestController;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.javaops.topjava.UserTestData.ADMIN;
import static ru.javaops.topjava.UserTestData.USER_ID;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
public class InMemoryAdminRestControllerSpringTest {
    @Autowired
    private AdminRestController controller;

    @Autowired
    private InMemoryUserRepository repository;

    @Before
    public void setUp() {
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
