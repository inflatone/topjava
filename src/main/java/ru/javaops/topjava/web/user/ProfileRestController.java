package ru.javaops.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.service.UserService;

import static ru.javaops.topjava.web.SecurityUtil.authUserId;

@Controller
public class ProfileRestController extends AbstractUserController {
    @Autowired
    public ProfileRestController(UserService service) {
        super(service);
    }

    public User get() {
        return super.get(authUserId());
    }

    public void delete() {
        super.delete(authUserId());
    }

    public void update(User user) {
        super.update(user, authUserId());
    }
}
