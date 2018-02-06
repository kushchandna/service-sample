package com.kush.tripper.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.async.Response;
import com.kush.utils.async.ServiceFailedException;
import com.kush.utils.async.ServiceTask;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationServiceClient extends ServiceClient<SampleTripperApplicationServiceApi> {

    public Response<Identifier> saveTrip(Trip trip) {
        return invoke(new ServiceTask<Identifier>() {

            @Override
            public Identifier execute() throws ServiceFailedException {
                return getService().saveTrip(trip);
            }
        });
    }

    public Response<Place> findPlace(String placeName) {
        return invoke(new ServiceTask<Place>() {

            @Override
            public Place execute() throws ServiceFailedException {
                return getService().findPlace(placeName);
            }
        });
    }

    public Response<Itinerary> getItinerary(Identifier id) {
        return invoke(new ServiceTask<Itinerary>() {

            @Override
            public Itinerary execute() throws ServiceFailedException {
                return getService().getItinerary(id);
            }
        });
    }

    public Response<Identifier[]> getAllTrips() {
        return invoke(new ServiceTask<Identifier[]>() {

            @Override
            public Identifier[] execute() throws ServiceFailedException {
                return getService().getAllTrips();
            }
        });
    }

    @Override
    protected Class<SampleTripperApplicationServiceApi> getServiceApiClass() {
        return SampleTripperApplicationServiceApi.class;
    }
}
