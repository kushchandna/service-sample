package com.kush.tripper.sample.server;

import static com.kush.utils.id.Identifier.id;

import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.annotations.Service;
import com.kush.lib.service.server.api.annotations.ServiceMethod;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

@Service(name = "Sample Tripper Application Service")
public class SampleTripperApplicationService extends BaseService {

    @ServiceMethod(name = "saveTrip")
    public Identifier saveTrip(Trip trip) {
        return id("Random ID");
    }

    @ServiceMethod(name = "findPlace")
    public Place findPlace(String placeName) {
        return new Place() {
        };
    }

    @ServiceMethod(name = "getItinerary")
    public Itinerary getItinerary(Identifier id) {
        return new Itinerary() {
        };
    }

    @ServiceMethod(name = "getAllTrips")
    public Identifier[] getAllTrips() {
        Identifier[] ids = new Identifier[1];
        ids[0] = id("Random ID");
        return ids;
    }
}
