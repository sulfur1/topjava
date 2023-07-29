package ru.javawebinar.topjava.web.formatters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {

    private static final Logger log = LoggerFactory.getLogger(CustomTimeFormatter.class);

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        log.debug("format {} to LocalTime", text);

        return StringUtils.hasLength(text) ? LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")) : null;
    }

    @Override
    public String print(LocalTime localtime, Locale locale) {
        return localtime.toString();
    }
}
