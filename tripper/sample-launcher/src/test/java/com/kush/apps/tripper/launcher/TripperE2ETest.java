package com.kush.apps.tripper.launcher;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kush.apps.tripper.SampleLocalTripperServer;
import com.kush.apps.tripper.api.Trip;
import com.kush.apps.tripper.client.SampleTripperApplication;
import com.kush.apps.tripper.services.servicegen.generated.clients.TripPlannerServiceClient;
import com.kush.lib.location.api.Place;
import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.local.LocalServiceConnectionFactory;
import com.kush.lib.userprofile.servicegen.generated.clients.UserProfileServiceClient;

public class TripperE2ETest {

    private SampleTripperApplication application;
    private ExecutorService executor;

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

        application = new SampleTripperApplication(client.getServiceClientProvider());
    }

    @After
    public void teardown() throws Exception {
        executor.shutdownNow();
        logoutSilently();
    }

    @Test
    public void createdTrip_CanBeRetrievedByCreator() throws Exception {
        String user1Trip1 = "First Trip";
        String user2Trip2 = "Second Trip";
        String user1Trip3 = "Third Trip";

        Credential user1Cred1 = new PasswordBasedCredential("testusr1", "testpwd1".toCharArray());
        User user1 = application.register(user1Cred1);
        application.login(user1Cred1);
        application.createTrip(user1Trip1);
        application.createTrip(user1Trip3);
        application.logout();

        Credential user2Cred1 = new PasswordBasedCredential("testusr2", "testpwd2".toCharArray());
        User user2 = application.register(user2Cred1);
        application.login(user2Cred1);
        application.createTrip(user2Trip2);
        application.logout();

        Credential user1Cred2 = new PasswordBasedCredential("testusr1", "testpwd1".toCharArray());
        application.login(user1Cred2);
        Iterator<Trip> user1CreatedTrips = application.getCreatedTrips();
        application.logout();

        Credential user2Cred2 = new PasswordBasedCredential("testusr2", "testpwd2".toCharArray());
        application.login(user2Cred2);
        Iterator<Trip> user2CreatedTrips = application.getCreatedTrips();
        application.logout();

        Trip user1CreatedTrip1 = user1CreatedTrips.next();
        assertThat(user1CreatedTrip1.getTripName(), is(equalTo(user1Trip1)));
        assertThat(user1CreatedTrip1.getCreatedBy(), is(equalTo(user1.getId())));
        Trip user1CreatedTrip2 = user1CreatedTrips.next();
        assertThat(user1CreatedTrip2.getTripName(), is(equalTo(user1Trip3)));
        assertThat(user1CreatedTrip2.getCreatedBy(), is(equalTo(user1.getId())));
        assertThat(user1CreatedTrips.hasNext(), is(equalTo(false)));

        Trip user2CreatedTrip1 = user2CreatedTrips.next();
        assertThat(user2CreatedTrip1.getTripName(), is(equalTo(user2Trip2)));
        assertThat(user2CreatedTrip1.getCreatedBy(), is(equalTo(user2.getId())));
        assertThat(user2CreatedTrips.hasNext(), is(equalTo(false)));
    }

    @Test
    public void addPlacesToCreatedTrip() throws Exception {
        startSessionForTestUser();
        Trip trip = application.createTrip("Test Trip");
        assertThat(trip.getPlacesToVisit(), is(empty()));

        Place place1 = application.findPlace("Place1");
        Place place2 = application.findPlace("Place2");
        application.addPlaces(trip.getId(), Arrays.asList(place1, place2));

        Iterator<Trip> trips = application.getCreatedTrips();
        Trip createdTrip = trips.next();
        List<Place> savedPlacesToVisit = createdTrip.getPlacesToVisit();
        savedPlacesToVisit.toString();
    }

    private void logoutSilently() {
        try {
            application.logout();
        } catch (Exception e) {
            // eat exception
        }
    }

    private User startSessionForTestUser() throws Exception {
        Credential cred = new PasswordBasedCredential("testusr", "testpwd".toCharArray());
        User user = application.register(cred);
        application.login(cred);
        return user;
    }
}
