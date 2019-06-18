package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private UserService service;

    public User create(User user) {
        log.info("create {}", user);
        ValidationUtil.checkNew(user);
        return service.create(user);
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        ValidationUtil.assureIdConsistent(user, id);
        service.update(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();

    }
}
