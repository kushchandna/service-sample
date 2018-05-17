package com.kush.apps.tripper.api;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.kush.service.annotations.Exportable;

@Exportable
public class Duration implements Serializable {

    private static final long serialVersionUID = 1L;

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
