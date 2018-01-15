package com.kush.tripper.sample.client;

import com.kush.lib.service.remoting.api.ServiceApi;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

public interface SampleTripperApplicationServiceApi extends ServiceApi {

    Identifier saveTrip(Trip trip);

    Place findPlace(String placeName);

    Itinerary getItinerary(Identifier id);

    Identifier[] getAllTrips();
}
