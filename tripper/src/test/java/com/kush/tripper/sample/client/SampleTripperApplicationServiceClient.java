package com.kush.tripper.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.async.Response;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationServiceClient extends ServiceClient {

    public SampleTripperApplicationServiceClient() {
        super("Sample Tripper Application Service");
    }

    public Response<Identifier> saveTrip(Trip trip) {
        return invoke("saveTrip", trip);
    }

    public Response<Place> findPlace(String placeName) {
        return invoke("findPlace", placeName);
    }

    public Response<Itinerary> getItinerary(Identifier id) {
        return invoke("getItinerary", id);
    }

    public Response<Identifier[]> getAllTrips() {
        return invoke("getAllTrips");
    }
}
