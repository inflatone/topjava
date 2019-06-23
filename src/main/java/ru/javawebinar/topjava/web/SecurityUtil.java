package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.util.MealsUtil;

public class SecurityUtil {
    private static int id = 1;

    public static int authUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        SecurityUtil.id = id;
    }

    public static int authUserCaloriesPerDay() {
        return MealsUtil.DEFAULT_CALORIES_PER_DAY;
    }
}
