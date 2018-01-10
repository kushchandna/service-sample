package com.kush.tripper.sample;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceInvoker;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationServiceClient extends ServiceClient {

    public SampleTripperApplicationServiceClient(Executor executor, ServiceInvoker serviceInvoker) {
        super(executor, serviceInvoker, "Sample Tripper Application Service");
    }

    public Response<Identifier> saveTrip(Trip trip) {
        return invoke(Identifier.class, "saveTrip", trip);
    }

    public Response<Place> findPlace(String placeName) {
        return invoke(Place.class, "findPlace", placeName);
    }

    public Response<Itinerary> getItinerary(Identifier id) {
        return invoke(Itinerary.class, "getItinerary", id);
    }

    public Response<Identifier[]> getAllTrips() {
        return invoke(Identifier[].class, "getAllTrips");
    }
}
