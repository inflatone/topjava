package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static LocalDateTime parse(String date, String time) {
        return LocalDateTime.of(LocalDate.parse(date, DATE_FORMATTER), LocalTime.parse(time, TIME_FORMATTER));
    }

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, DATE_FORMATTER);
    }

    public static String format(LocalDateTime date) {
        return String.format("%s %s", format(date.toLocalDate()), format(date.toLocalTime()));
    }

    public static String format(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String format(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }
}
