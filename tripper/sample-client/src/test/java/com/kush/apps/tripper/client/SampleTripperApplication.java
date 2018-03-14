package com.kush.apps.tripper.client;

import java.util.Iterator;

import com.google.common.collect.ImmutableMap;
import com.kush.apps.tripper.api.Trip;
import com.kush.apps.tripper.services.servicegen.generated.clients.TripPlannerServiceClient;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.services.servicegen.generated.clients.PlaceServiceClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.client.api.session.LoginServiceClient;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.userprofile.UserProfile;
import com.kush.lib.userprofile.servicegen.generated.clients.UserProfileServiceClient;
import com.kush.utils.async.Response;

public class SampleTripperApplication {

    private final ServiceClientProvider serviceClientProvider;

    public SampleTripperApplication(ServiceClientProvider serviceClientProvider) {
        this.serviceClientProvider = serviceClientProvider;
    }

    public void register(Credential credential) throws Exception {
        LoginServiceClient client = serviceClientProvider.getServiceClient(LoginServiceClient.class);
        client.register(credential).waitForResult();
    }

    public void login(Credential credential) throws Exception {
        LoginServiceClient client = serviceClientProvider.getServiceClient(LoginServiceClient.class);
        client.login(credential).waitForResult();
    }

    public void logout() throws Exception {
        LoginServiceClient client = serviceClientProvider.getServiceClient(LoginServiceClient.class);
        client.logout().waitForResult();
    }

    public Trip createTrip(String tripName) throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        return client.createTrip(tripName).getResult();
    }

    public Iterator<Trip> getCreatedTrips() throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        Response<Iterator<Trip>> response = client.getCreatedTrips();
        return response.getResult();
    }

    public UserProfile updateProfile(ImmutableMap<String, Object> profileFields) throws Exception {
        UserProfileServiceClient client = serviceClientProvider.getServiceClient(UserProfileServiceClient.class);
        return client.updateProfile(profileFields).getResult();
    }

    public UserProfile getProfile() throws Exception {
        UserProfileServiceClient client = serviceClientProvider.getServiceClient(UserProfileServiceClient.class);
        return client.getProfile().getResult();
    }

    public Place findPlace(String text) throws Exception {
        PlaceServiceClient client = serviceClientProvider.getServiceClient(PlaceServiceClient.class);
        return client.findPlace(text).getResult();
    }
}
