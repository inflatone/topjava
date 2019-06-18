package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.User;

public class ProfileRestController extends AbstractUserController {
    public User get() {
        return super.get(SecurityUtil.authUserId());
    }

    public void update(User user) {
        super.update(user, SecurityUtil.authUserId());
    }

    public void delete() {
        super.delete(SecurityUtil.authUserId());
    }
}
