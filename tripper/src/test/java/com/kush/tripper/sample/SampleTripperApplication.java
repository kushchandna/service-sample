package com.kush.tripper.sample;

import java.util.List;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.location.Location;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

public class SampleTripperApplication {

    private final ServiceClientProvider serviceClientProvider;
    private final ApplicationLocator locator;
    private final TripperItineraryView itineraryView;

    public SampleTripperApplication(ApplicationClient applicationClient, ApplicationLocator locator,
            TripperItineraryView itineraryView) {
        this.locator = locator;
        this.itineraryView = itineraryView;
        serviceClientProvider = applicationClient.getServiceClientProvider();
    }

    public void registerUser(String username, String password) throws Exception {
        SampleTripperUserServiceClient userServiceClient = getSampleTripperUserServiceClient();
        Response<Void> response = userServiceClient.registerUser(username, password);
        response.waitForResult();
    }

    public void login(String username, String password) throws Exception {
        SampleTripperUserServiceClient userServiceClient = getSampleTripperUserServiceClient();
        Response<Void> response = userServiceClient.login(username, password);
        response.waitForResult();
    }

    public void logout() throws Exception {
        SampleTripperUserServiceClient userServiceClient = getSampleTripperUserServiceClient();
        Response<Void> response = userServiceClient.logout();
        response.waitForResult();
    }

    public Identifier saveTrip(Trip trip) throws Exception {
        SampleTripperApplicationServiceClient appServiceClient = getSampleTripperApplicationServiceClient();
        Response<Identifier> response = appServiceClient.saveTrip(trip);
        return response.getResult();
    }

    public Place findPlace(String placeName) throws Exception {
        SampleTripperApplicationServiceClient appServiceClient = getSampleTripperApplicationServiceClient();
        Response<Place> response = appServiceClient.findPlace(placeName);
        return response.getResult();
    }

    public Location getCurrentLocation() {
        return locator.getCurrentLocation();
    }

    public Itinerary getItinerary(Identifier id) throws Exception {
        SampleTripperApplicationServiceClient appServiceClient = getSampleTripperApplicationServiceClient();
        Response<Itinerary> response = appServiceClient.getItinerary(id);
        return response.getResult();
    }

    public void displayItinerary(Itinerary itinerary) {
        itineraryView.show(itinerary);
    }

    public List<Identifier> getAllTrips() throws Exception {
        SampleTripperApplicationServiceClient appServiceClient = getSampleTripperApplicationServiceClient();
        Response<List<Identifier>> response = appServiceClient.getAllTrips();
        return response.getResult();
    }

    private SampleTripperApplicationServiceClient getSampleTripperApplicationServiceClient() {
        return serviceClientProvider.getServiceClient(SampleTripperApplicationServiceClient.class);
    }

    private SampleTripperUserServiceClient getSampleTripperUserServiceClient() {
        return serviceClientProvider.getServiceClient(SampleTripperUserServiceClient.class);
    }
}
