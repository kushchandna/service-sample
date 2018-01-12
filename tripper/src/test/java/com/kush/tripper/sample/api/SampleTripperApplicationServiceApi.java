package com.kush.tripper.sample.api;

import com.kush.lib.service.remoting.ServiceApi;
import com.kush.tripper.sample.api.types.Itinerary;
import com.kush.tripper.sample.api.types.Place;
import com.kush.tripper.sample.api.types.Trip;
import com.kush.utils.id.Identifier;

public interface SampleTripperApplicationServiceApi extends ServiceApi {

    Identifier saveTrip(Trip trip);

    Place findPlace(String placeName);

    Itinerary getItinerary(Identifier id);

    Identifier[] getAllTrips();
}