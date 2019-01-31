package com.kush.apps.tripper.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Duration {

    private final LocalDateTime start;
    private final LocalDateTime end;

    private Duration(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        this.start = start;
        this.end = end;
    }

    public static Builder duration() {
        return new Builder();
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

    public static class Builder {

        private LocalDateTime start;
        private LocalDateTime end;

        private Builder() {
        }

        public Builder from(LocalDateTime start) {
            this.start = start;
            return this;
        }

        public Builder to(LocalDateTime end) {
            this.end = end;
            return this;
        }

        public Duration build() {
            return new Duration(start, end);
        }
    }
}
