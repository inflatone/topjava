package ru.javaops.topjava;

import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static ru.javaops.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final TestMatchers<User> USER_MATCHERS = TestMatchers.useFieldsComparator(User.class, "registered", "meals", "password");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", 2005, Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", 1900, Role.ROLE_USER, Role.ROLE_ADMIN);

    public static User createNew() {
        return new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.ROLE_USER));
    }

    public static User createUpdated() {
        var updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setRoles(Set.of(Role.ROLE_ADMIN));
        return updated;
    }
}