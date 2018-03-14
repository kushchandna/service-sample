package com.kush.apps.tripper.client;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.common.collect.ImmutableMap;
import com.kush.apps.tripper.api.Trip;
import com.kush.apps.tripper.services.servicegen.generated.clients.TripPlannerServiceClient;
import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.local.LocalServiceConnectionFactory;
import com.kush.lib.userprofile.UserProfile;
import com.kush.lib.userprofile.servicegen.generated.clients.UserProfileServiceClient;

public class SampleTripperClient {

    public void start() {
        try {
            performClientOperations();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void performClientOperations() throws Exception {
        ApplicationClient client = new ApplicationClient();

        ServiceConnectionFactory connectionFactory = new LocalServiceConnectionFactory();
        client.start(connectionFactory);

        Executor executor = Executors.newSingleThreadExecutor();
        client.activateLoginServiceClient(executor);
        client.activateServiceClient(TripPlannerServiceClient.class, executor);
        client.activateServiceClient(UserProfileServiceClient.class, executor);

        SampleTripperApplication application = new SampleTripperApplication(client.getServiceClientProvider());

        Credential credential1 = new PasswordBasedCredential("testusr1", "testpwd1".toCharArray());
        application.register(credential1);
        application.login(credential1);
        Trip trip = application.createTrip("Trip to Jaipur");
        System.out.println(trip.getTripName());
        application.logout();

        Credential credential2 = new PasswordBasedCredential("testusr2", "testpwd2".toCharArray());
        application.register(credential2);
        application.login(credential2);
        Iterator<Trip> createdTrips = application.getCreatedTrips();
        System.out.println(createdTrips.hasNext());
        application.logout();

        Credential credential3 = new PasswordBasedCredential("testusr1", "testpwd1".toCharArray());
        application.login(credential3);
        Iterator<Trip> createdTrips3 = application.getCreatedTrips();
        System.out.println(createdTrips3.next().getTripName());
        Map<String, Boolean> emailIdVsIsPrimary = ImmutableMap.of(
                "testuser1@domain.com", true);
        application.updateProfile(ImmutableMap.of(
                "NAME", "Test User1",
                "EMAILS", emailIdVsIsPrimary));
        UserProfile profile = application.getProfile();
        System.out.println(profile.getAllFields());
        application.logout();
    }
}
