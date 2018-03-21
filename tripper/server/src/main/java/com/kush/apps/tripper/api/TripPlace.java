package com.kush.apps.tripper.api;

import com.kush.lib.location.api.Place;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class TripPlace implements Identifiable {

    private final Identifier id;
    private final Trip trip;
    private final Place place;

    public TripPlace(Trip trip, Place place) {
        this(Identifier.NULL, trip, place);
    }

    public TripPlace(Identifier id, Trip trip, Place place) {
        this.id = id;
        this.trip = trip;
        this.place = place;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public Place getPlace() {
        return place;
    }
}
