package com.kush.apps.tripper.api;

import com.kush.lib.location.api.Place;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class TripPlanPlace implements Identifiable {

    private final Identifier id;
    private final TripPlan trip;
    private final Place place;

    public TripPlanPlace(TripPlan trip, Place place) {
        this(Identifier.NULL, trip, place);
    }

    public TripPlanPlace(Identifier id, TripPlan trip, Place place) {
        this.id = id;
        this.trip = trip;
        this.place = place;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public TripPlan getTrip() {
        return trip;
    }

    public Place getPlace() {
        return place;
    }
}
