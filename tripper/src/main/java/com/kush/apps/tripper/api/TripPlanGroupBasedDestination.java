package com.kush.apps.tripper.api;

import com.kush.messaging.destination.GroupIdBasedDestination;

public class TripPlanGroupBasedDestination extends GroupIdBasedDestination {

    private static final long serialVersionUID = 1L;

    private final TripPlan tripPlan;

    public TripPlanGroupBasedDestination(TripPlan tripPlan) {
        super(tripPlan.getTripGroup().getId());
        this.tripPlan = tripPlan;
    }

    public TripPlan getTripPlan() {
        return tripPlan;
    }
}
