package com.kush.apps.tripper.client;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.kush.apps.tripper.api.Duration;
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
        return client.getTripPlans().getResult();
    }

    public void addPlacesToTripPlan(Identifier tripPlanId, List<Place> placesToVisit) throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        client.addPlacesToTripPlan(tripPlanId, placesToVisit).waitForResult();
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
        return client.getPlacesInTripPlan(tripPlan.getId()).getResult();
    }

    public void setTripPlanDuration(Identifier tripPlanId, Duration duration) throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        client.setTripPlanDuration(tripPlanId, duration).waitForResult();
    }

    public void addMembersToTripPlan(Identifier tripPlanId, Set<Identifier> memberUserIds) throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        client.addMembersToTripPlan(tripPlanId, memberUserIds).waitForResult();
    }
}
