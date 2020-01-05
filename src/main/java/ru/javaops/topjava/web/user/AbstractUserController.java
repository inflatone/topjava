package ru.javaops.topjava.web.user;

import org.slf4j.Logger;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.service.UserService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaops.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava.util.ValidationUtil.checkNew;

public class AbstractUserController {
    protected final Logger log = getLogger(getClass());

    protected final UserService service;

    public AbstractUserController(UserService service) {
        this.service = service;
    }

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("Update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }
}
