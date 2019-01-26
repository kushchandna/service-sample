package com.kush.apps.tripper.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.concurrent.TimeUnit;

public class Duration {

    private final LocalDateTime start;
    private final LocalDateTime end;

    public Duration(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public long getDays() {
        java.time.Duration duration = java.time.Duration.between(start, end);
        long seconds = duration.getSeconds();
        return TimeUnit.SECONDS.toDays(seconds);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy/MM/dd")
            .toFormatter();
        return new StringBuilder()
            .append(start.format(formatter))
            .append(" - ")
            .append(end.format(formatter))
            .append(" (").append(getDays()).append(" days)")
            .toString();
    }
}
