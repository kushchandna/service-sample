package com.kush.apps.tripper.api;

import java.io.Serializable;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

@Exportable
public class TripPlan implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier tripId;
    private final Identifier createdBy;
    private final String tripPlanName;

    public TripPlan(Identifier createdBy, String tripPlanName) {
        this(Identifier.NULL, createdBy, tripPlanName);
    }

    public TripPlan(Identifier tripId, Identifier createdBy, String tripPlanName) {
        this.tripId = tripId;
        this.createdBy = createdBy;
        this.tripPlanName = tripPlanName;
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
}
