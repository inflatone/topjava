package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.util.UserUtil;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class SecurityUtil {
    private static int id = START_SEQ;

    private SecurityUtil() {
    }

    public static int authUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        SecurityUtil.id = id;
    }

    public static int authUserCaloriesPerDay() {
        return UserUtil.DEFAULT_CALORIES_PER_DAY;
    }
}
