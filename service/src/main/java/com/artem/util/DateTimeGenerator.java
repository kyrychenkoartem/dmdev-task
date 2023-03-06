package com.artem.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeGenerator {

    public static LocalDateTime getRandomDateTime() {
        LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0);
        long days = ChronoUnit.DAYS.between(start, LocalDateTime.now());
        return start.plusDays(new Random().nextInt((int) days + 1));
    }
}
