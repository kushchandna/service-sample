package com.kush.apps.tripper.api;

import com.kush.lib.location.api.Place;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class TripPlanPlace implements Identifiable {

    private final Identifier id;
    private final TripPlan tripPlan;
    private final Place place;

    public TripPlanPlace(TripPlan tripPlan, Place place) {
        this(Identifier.NULL, tripPlan, place);
    }

    public TripPlanPlace(Identifier id, TripPlanPlace tripPlanPlace) {
        this(id, tripPlanPlace.getTripPlan(), tripPlanPlace.getPlace());
    }

    public TripPlanPlace(Identifier id, TripPlan tripPlan, Place place) {
        this.id = id;
        this.tripPlan = tripPlan;
        this.place = place;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public TripPlan getTripPlan() {
        return tripPlan;
    }

    public Place getPlace() {
        return place;
    }
}
