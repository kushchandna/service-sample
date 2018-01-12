package com.kush.tripper.sample.client;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceFailedException;
import com.kush.lib.service.client.api.ServiceTask;
import com.kush.tripper.sample.api.SampleTripperApplicationServiceApi;
import com.kush.tripper.sample.api.types.Itinerary;
import com.kush.tripper.sample.api.types.Place;
import com.kush.tripper.sample.api.types.Trip;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationServiceClient extends ServiceClient<SampleTripperApplicationServiceApi> {

    public SampleTripperApplicationServiceClient(Executor executor, SampleTripperApplicationServiceApi serviceApi) {
        super(executor, serviceApi);
    }

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
}
