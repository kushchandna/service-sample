package com.kush.apps.tripper.api;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class TripPlanMember implements Identifiable {

    private final Identifier id;
    private final TripPlan tripPlan;
    private final Identifier userId;

    public TripPlanMember(TripPlan tripPlan, Identifier userId) {
        this(Identifier.NULL, tripPlan, userId);
    }

    public TripPlanMember(Identifier id, TripPlanMember tripPlanMember) {
        this(id, tripPlanMember.getTripPlan(), tripPlanMember.getUserId());
    }

    public TripPlanMember(Identifier id, TripPlan tripPlan, Identifier userId) {
        this.id = id;
        this.tripPlan = tripPlan;
        this.userId = userId;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public TripPlan getTripPlan() {
        return tripPlan;
    }

    public Identifier getUserId() {
        return userId;
    }
}
