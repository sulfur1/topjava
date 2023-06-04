package ru.javawebinar.topjava.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ParseDate {
    private ParseDate() {}

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
