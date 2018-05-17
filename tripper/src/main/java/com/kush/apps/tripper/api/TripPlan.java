package com.kush.apps.tripper.api;

import java.io.Serializable;

import com.kush.service.annotations.Exportable;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

@Exportable
public class TripPlan implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier tripId;
    private final Identifier createdBy;
    private final String tripPlanName;
    private final Duration duration;

    public TripPlan(Identifier createdBy, String tripPlanName, Duration duration) {
        this(Identifier.NULL, createdBy, tripPlanName, duration);
    }

    public TripPlan(Identifier tripId, TripPlan tripPlan) {
        this(tripId, tripPlan.getCreatedBy(), tripPlan.getTripPlanName(), tripPlan.getDuration());
    }

    public TripPlan(Identifier tripId, Identifier createdBy, String tripPlanName, Duration duration) {
        this.tripId = tripId;
        this.createdBy = createdBy;
        this.tripPlanName = tripPlanName;
        this.duration = duration;
    }

    @Override
    public Identifier getId() {
        return tripId;
    }

    public Identifier getCreatedBy() {
        return createdBy;
    }

    public String getTripPlanName() {
        return tripPlanName;
    }

    public Duration getDuration() {
        return duration;
    }
}
