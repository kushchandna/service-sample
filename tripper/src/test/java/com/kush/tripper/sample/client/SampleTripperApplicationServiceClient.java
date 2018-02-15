package com.kush.tripper.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.async.Response;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationServiceClient extends ServiceClient {

    public Response<Identifier> saveTrip(Trip trip) {
        return invoke("saveTrip", Identifier.class, trip);
    }

    public Response<Place> findPlace(String placeName) {
        return invoke("findPlace", Place.class, placeName);
    }

    public Response<Itinerary> getItinerary(Identifier id) {
        return invoke("getItinerary", Itinerary.class, id);
    }

    public Response<Identifier[]> getAllTrips() {
        return invoke("getAllTrips", Identifier[].class);
    }
}
