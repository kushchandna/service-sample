package com.kush.tripper.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.async.Response;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.Request;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationServiceClient extends ServiceClient<SampleTripperApplicationServiceApi> {

    public Response<Identifier> saveTrip(Trip trip) {
        return invoke(new Request<Identifier>() {

            @Override
            public Identifier process() throws RequestFailedException {
                return getService().saveTrip(trip);
            }
        });
    }

    public Response<Place> findPlace(String placeName) {
        return invoke(new Request<Place>() {

            @Override
            public Place process() throws RequestFailedException {
                return getService().findPlace(placeName);
            }
        });
    }

    public Response<Itinerary> getItinerary(Identifier id) {
        return invoke(new Request<Itinerary>() {

            @Override
            public Itinerary process() throws RequestFailedException {
                return getService().getItinerary(id);
            }
        });
    }

    public Response<Identifier[]> getAllTrips() {
        return invoke(new Request<Identifier[]>() {

            @Override
            public Identifier[] process() throws RequestFailedException {
                return getService().getAllTrips();
            }
        });
    }

    @Override
    protected Class<SampleTripperApplicationServiceApi> getServiceApiClass() {
        return SampleTripperApplicationServiceApi.class;
    }
}
