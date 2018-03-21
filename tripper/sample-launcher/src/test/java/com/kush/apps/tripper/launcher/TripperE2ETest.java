package com.kush.apps.tripper.launcher;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.kush.apps.tripper.SampleLocalTripperServer;
import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.client.SampleTripperApplication;
import com.kush.apps.tripper.services.servicegen.generated.clients.TripPlannerServiceClient;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.services.servicegen.generated.clients.PlaceServiceClient;
import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.client.api.session.LoginServiceClient;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.local.LocalServiceConnectionFactory;
import com.kush.lib.userprofile.servicegen.generated.clients.UserProfileServiceClient;

public class TripperE2ETest {

    private SampleTripperApplication application;
    private ExecutorService executor;

    @Rule
    public FakeSessionManager sessionManager = new FakeSessionManager("test", 5);

    @Before
    public void setup() throws Exception {
        SampleLocalTripperServer server = new SampleLocalTripperServer();
        server.start();
        ApplicationClient client = new ApplicationClient();
        ServiceConnectionFactory connectionFactory = new LocalServiceConnectionFactory();
        client.start(connectionFactory);
        executor = Executors.newSingleThreadExecutor();
        client.activateLoginServiceClient(executor);
        client.activateServiceClient(TripPlannerServiceClient.class, executor);
        client.activateServiceClient(UserProfileServiceClient.class, executor);
        client.activateServiceClient(PlaceServiceClient.class, executor);

        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        application = new SampleTripperApplication(serviceClientProvider);
        sessionManager.initialize(serviceClientProvider.getServiceClient(LoginServiceClient.class));
    }

    @After
    public void teardown() throws Exception {
        executor.shutdownNow();
    }

    @Test
    public void createdTripPlan_CanBeRetrievedByCreator() throws Exception {
        String user1TripPlan1 = "First Trip Plan";
        String user2TripPlan2 = "Second Trip Plan";
        String user1TripPlan3 = "Third Trip Plan";

        User[] users = sessionManager.getUsers();
        User user1 = users[0];
        sessionManager.beginSession(user1);
        application.createTripPlan(user1TripPlan1);
        application.createTripPlan(user1TripPlan3);
        sessionManager.endSession();

        User user2 = users[1];
        sessionManager.beginSession(user2);
        application.createTripPlan(user2TripPlan2);
        sessionManager.endSession();

        sessionManager.beginSession(user1);
        Iterator<TripPlan> user1CreatedTripPlans = application.getCreatedTripPlans();
        sessionManager.endSession();

        sessionManager.beginSession(user2);
        Iterator<TripPlan> user2CreatedTripPlans = application.getCreatedTripPlans();
        sessionManager.endSession();

        TripPlan user1CreatedTripPlan1 = user1CreatedTripPlans.next();
        assertThat(user1CreatedTripPlan1.getTripPlanName(), is(equalTo(user1TripPlan1)));
        assertThat(user1CreatedTripPlan1.getCreatedBy(), is(equalTo(user1.getId())));
        TripPlan user1CreatedTripPlan2 = user1CreatedTripPlans.next();
        assertThat(user1CreatedTripPlan2.getTripPlanName(), is(equalTo(user1TripPlan3)));
        assertThat(user1CreatedTripPlan2.getCreatedBy(), is(equalTo(user1.getId())));
        assertThat(user1CreatedTripPlans.hasNext(), is(equalTo(false)));

        TripPlan user2CreatedTripPlan1 = user2CreatedTripPlans.next();
        assertThat(user2CreatedTripPlan1.getTripPlanName(), is(equalTo(user2TripPlan2)));
        assertThat(user2CreatedTripPlan1.getCreatedBy(), is(equalTo(user2.getId())));
        assertThat(user2CreatedTripPlans.hasNext(), is(equalTo(false)));
    }

    @Test
    public void addPlacesToCreatedTripPlan() throws Exception {
        sessionManager.beginTestSession();
        TripPlan tripPlan = application.createTripPlan("Test Trip Plan");
        assertThat(application.getPlacesInTripPlan(tripPlan), is(empty()));

        Place place1 = application.findPlace("Place1");
        Place place2 = application.findPlace("Place2");
        application.addPlaces(tripPlan.getId(), Arrays.asList(place1, place2));

        Iterator<TripPlan> tripPlans = application.getCreatedTripPlans();
        TripPlan createdTripPlan = tripPlans.next();
        List<Place> savedPlacesToVisit = application.getPlacesInTripPlan(createdTripPlan);
        assertThat(savedPlacesToVisit, hasSize(2));
        assertThat(savedPlacesToVisit.get(0).getName(), is(equalTo("Place1")));
        assertThat(savedPlacesToVisit.get(1).getName(), is(equalTo("Place2")));
    }
}
