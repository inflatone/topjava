package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.util.MealsUtil;

public class SecurityUtil {

    public static int authUserId() {
        return 1;
    }

    public static int authUserCaloriesPerDay() {
        return MealsUtil.DEFAULT_CALORIES_PER_DAY;
    }
}
