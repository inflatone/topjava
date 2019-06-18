package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.User;

import java.util.List;

public class AdminRestController extends AbstractUserController {
    @Override
    public User create(User user) {
        return super.create(user);
    }

    @Override
    public User get(int id) {
        return super.get(id);
    }

    @Override
    public User getByEmail(String email) {
        return super.getByEmail(email);
    }

    @Override
    public void update(User user, int id) {
        super.update(user, id);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }

    @Override
    public List<User> getAll() {
        return super.getAll();
    }
}
