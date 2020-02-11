package ru.javaops.topjava.web.user;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaops.topjava.model.AbstractBaseEntity;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.service.UserService;
import ru.javaops.topjava.to.UserTo;
import ru.javaops.topjava.util.UserUtil;
import ru.javaops.topjava.util.exeption.ModificationRestrictionException;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaops.topjava.Profiles.HEROKU;
import static ru.javaops.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava.util.ValidationUtil.checkNew;

public class AbstractUserController {
    protected final Logger log = getLogger(getClass());

    protected final UserService service;

    private UniqueMailValidator mailValidator;

    private boolean modificationRestriction;

    @Autowired
    //@SuppressWarnings("deprecation")
    public void setEnvironment(Environment environment) {
        modificationRestriction = environment.acceptsProfiles(Profiles.of(HEROKU));
    }

    public AbstractUserController(UserService service, UniqueMailValidator mailValidator) {
        this.service = service;
        this.mailValidator = mailValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(mailValidator);
    }

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(UserTo userTo) {
        log.info("create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkModificationAllowed(id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("Update {} with id={}", user, id);
        assureIdConsistent(user, id);
        checkModificationAllowed(id);
        service.update(user);
    }

    public void update(UserTo userTo, int id) {
        log.info("Update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        checkModificationAllowed(id);
        service.update(userTo);
    }

    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        checkModificationAllowed(id);
        service.enable(id, enabled);
    }

    private void checkModificationAllowed(int id) {
        if (modificationRestriction && id < AbstractBaseEntity.START_SEQ + 2) {
            throw new ModificationRestrictionException();
        }
    }
}
