package com.kush.tripper.sample.api.remoting;

import com.kush.lib.service.server.api.BaseService;
import com.kush.tripper.sample.api.SampleTripperApplicationServiceApi;
import com.kush.tripper.sample.api.types.Itinerary;
import com.kush.tripper.sample.api.types.Place;
import com.kush.tripper.sample.api.types.Trip;
import com.kush.utils.id.Identifier;

public class DelegatingSampleTripperApplicationService extends BaseService implements SampleTripperApplicationServiceApi {

    private final SampleTripperApplicationServiceApi delegate;

    public DelegatingSampleTripperApplicationService(SampleTripperApplicationServiceApi delegate) {
        this.delegate = delegate;
    }

    @Override
    public Identifier saveTrip(Trip trip) {
        return delegate.saveTrip(trip);
    }

    @Override
    public Place findPlace(String placeName) {
        return delegate.findPlace(placeName);
    }

    @Override
    public Itinerary getItinerary(Identifier id) {
        return delegate.getItinerary(id);
    }

    @Override
    public Identifier[] getAllTrips() {
        return delegate.getAllTrips();
    }
}
