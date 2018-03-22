package com.kush.apps.tripper.client;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.services.servicegen.generated.clients.TripPlannerServiceClient;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.services.servicegen.generated.clients.PlaceServiceClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.userprofile.UserProfile;
import com.kush.lib.userprofile.servicegen.generated.clients.UserProfileServiceClient;
import com.kush.utils.id.Identifier;

public class SampleTripperApplication {

    private final ServiceClientProvider serviceClientProvider;

    public SampleTripperApplication(ServiceClientProvider serviceClientProvider) {
        this.serviceClientProvider = serviceClientProvider;
    }

    public TripPlan createTripPlan(String tripPlanName) throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        return client.createTripPlan(tripPlanName).getResult();
    }

    public List<TripPlan> getTripPlans() throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        return newArrayList(client.getTripPlans().getResult());
    }

    public void addPlacesToTripPlan(Identifier id, List<Place> placesToVisit) throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        client.addPlacesToTripPlan(id, placesToVisit).waitForResult();
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

    public List<Place> getPlacesInTripPlan(TripPlan tripPlan) throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        return newArrayList(client.getPlacesInTripPlan(tripPlan.getId()).getResult());
    }
}
