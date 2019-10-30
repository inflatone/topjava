package ru.javaops.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.service.UserService;

import java.util.List;

@Controller
public class AdminRestController extends AbstractUserController {

    @Autowired
    public AdminRestController(UserService service) {
        super(service);
    }

    @Override
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    public User get(int id) {
        return super.get(id);
    }

    @Override
    public User create(User user) {
        return super.create(user);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }

    @Override
    public void update(User user, int id) {
        super.update(user, id);
    }

    @Override
    public User getByEmail(String email) {
        return super.getByEmail(email);
    }
}
