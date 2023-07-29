package ru.javawebinar.topjava.web.formatters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {

    private static final Logger log = LoggerFactory.getLogger(CustomDateFormatter.class);

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        log.debug("format {} to LocalDate", text);
        return StringUtils.hasLength(text) ? LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.toString();
    }
}
