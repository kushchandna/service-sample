package com.kush.tripper.sample.server;

import static com.kush.utils.id.Identifier.id;

import com.kush.lib.service.server.api.BaseService;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationService extends BaseService {

    public Identifier saveTrip(Trip trip) {
        return id("Random ID");
    }

    public Place findPlace(String placeName) {
        return new Place() {
        };
    }

    public Itinerary getItinerary(Identifier id) {
        return new Itinerary() {
        };
    }

    public Identifier[] getAllTrips() {
        Identifier[] ids = new Identifier[1];
        ids[0] = id("Random ID");
        return ids;
    }
}
