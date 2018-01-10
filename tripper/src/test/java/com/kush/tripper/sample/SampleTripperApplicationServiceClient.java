package com.kush.tripper.sample;

import static com.kush.utils.id.Identifier.id;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceFailedException;
import com.kush.lib.service.client.api.ServiceTask;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationServiceClient extends ServiceClient {

    public SampleTripperApplicationServiceClient(Executor executor) {
        super(executor);
    }

    public Response<Identifier> saveTrip(Trip trip) {
        return invoke(new ServiceTask<Identifier>() {

            @Override
            public Identifier execute() throws ServiceFailedException {
                return id("A Random Id");
            }
        });
    }

    public Response<Place> findPlace(String placeName) {
        return invoke(new ServiceTask<Place>() {

            @Override
            public Place execute() throws ServiceFailedException {
                return new Place() {
                };
            }
        });
    }

    public Response<Itinerary> getItinerary(Identifier id) {
        return invoke(new ServiceTask<Itinerary>() {

            @Override
            public Itinerary execute() throws ServiceFailedException {
                return new Itinerary() {
                };
            }
        });
    }

    public Response<List<Identifier>> getAllTrips() {
        return invoke(new ServiceTask<List<Identifier>>() {

            @Override
            public List<Identifier> execute() throws ServiceFailedException {
                return Collections.singletonList(id("Fetched Id"));
            }
        });
    }
}
