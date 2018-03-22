package com.kush.apps.tripper.client;

import java.time.LocalDateTime;

public class Duration {

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private Duration(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Duration during(LocalDateTime startTime, LocalDateTime endTime) {
        return new Duration(startTime, endTime);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
