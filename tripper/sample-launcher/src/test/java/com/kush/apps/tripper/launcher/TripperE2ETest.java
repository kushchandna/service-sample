package com.kush.apps.tripper.launcher;

import static java.time.Month.APRIL;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.kush.apps.tripper.SampleLocalTripperServer;
import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.client.Duration;
import com.kush.apps.tripper.client.SampleTripperApplication;
import com.kush.apps.tripper.services.servicegen.generated.clients.TripPlannerServiceClient;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.services.servicegen.generated.clients.PlaceServiceClient;
import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.client.api.session.LoginServiceClient;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.socket.SocketServiceConnectionFactory;
import com.kush.lib.service.remoting.receiver.socket.ServerSocketServiceRequestReceiver;
import com.kush.lib.userprofile.servicegen.generated.clients.UserProfileServiceClient;
import com.kush.utils.id.Identifier;

public class TripperE2ETest {

    private static SampleLocalTripperServer server;
    private static ExecutorService serverExecutor;

    private SampleTripperApplication application;
    private ExecutorService clientExecutor;

    @Rule
    public FakeSessionManager sessionManager = new FakeSessionManager("test", 5);

    @BeforeClass
    public static void beforeAllTests() throws Exception {
    }

    @AfterClass
    public static void afterAllTests() throws Exception {
    }

    @Before
    public void beforeEachTest() throws Exception {
        server = new SampleLocalTripperServer();
        serverExecutor = Executors.newSingleThreadExecutor();
        server.start(new ServerSocketServiceRequestReceiver(serverExecutor, 3789));

        ApplicationClient client = new ApplicationClient();
        ServiceConnectionFactory connectionFactory = new SocketServiceConnectionFactory("localhost", 3789);
        client.start(connectionFactory);
        clientExecutor = Executors.newSingleThreadExecutor();
        client.activateLoginServiceClient(clientExecutor);
        client.activateServiceClient(TripPlannerServiceClient.class, clientExecutor);
        client.activateServiceClient(UserProfileServiceClient.class, clientExecutor);
        client.activateServiceClient(PlaceServiceClient.class, clientExecutor);

        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        application = new SampleTripperApplication(serviceClientProvider);
        sessionManager.initialize(serviceClientProvider.getServiceClient(LoginServiceClient.class));
    }

    @After
    public void afterEachTest() throws Exception {
        server.stop();
        clientExecutor.shutdownNow();
        serverExecutor.shutdownNow();
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
        List<TripPlan> user1CreatedTripPlans = application.getTripPlans();
        sessionManager.endSession();

        sessionManager.beginSession(user2);
        List<TripPlan> user2CreatedTripPlans = application.getTripPlans();
        sessionManager.endSession();

        assertThat(user1CreatedTripPlans, hasSize(2));
        assertThat(user1CreatedTripPlans.get(0).getTripPlanName(), is(equalTo(user1TripPlan1)));
        assertThat(user1CreatedTripPlans.get(0).getCreatedBy(), is(equalTo(user1.getId())));
        assertThat(user1CreatedTripPlans.get(1).getTripPlanName(), is(equalTo(user1TripPlan3)));
        assertThat(user1CreatedTripPlans.get(1).getCreatedBy(), is(equalTo(user1.getId())));

        assertThat(user2CreatedTripPlans, hasSize(1));
        assertThat(user2CreatedTripPlans.get(0).getTripPlanName(), is(equalTo(user2TripPlan2)));
        assertThat(user2CreatedTripPlans.get(0).getCreatedBy(), is(equalTo(user2.getId())));
    }

    @Test
    public void addPlacesToCreatedTripPlan() throws Exception {
        sessionManager.beginTestSession();
        TripPlan tripPlan = application.createTripPlan("Test Trip Plan");
        assertThat(application.getPlacesInTripPlan(tripPlan), is(empty()));

        Place place1 = application.findPlace("Place1");
        Place place2 = application.findPlace("Place2");
        application.addPlacesToTripPlan(tripPlan.getId(), asList(place1, place2));

        List<TripPlan> tripPlans = application.getTripPlans();
        List<Place> savedPlacesToVisit = application.getPlacesInTripPlan(tripPlans.get(0));
        assertThat(savedPlacesToVisit, hasSize(2));
        assertThat(savedPlacesToVisit.get(0).getName(), is(equalTo("Place1")));
        assertThat(savedPlacesToVisit.get(1).getName(), is(equalTo("Place2")));
    }

    @Test
    public void planATrip() throws Exception {
        // decide dates for a trip plan
        LocalDateTime tripPlanStartTime = LocalDateTime.of(LocalDate.of(2018, APRIL, 10), LocalTime.of(22, 0));
        LocalDateTime tripPlanEndTime = LocalDateTime.of(LocalDate.of(2018, APRIL, 14), LocalTime.of(2, 0));
        Duration duration = Duration.during(tripPlanStartTime, tripPlanEndTime);

        // decide places for trip plan
        Place place1 = application.findPlace("Place1");
        Place place2 = application.findPlace("Place2");
        Place place3 = application.findPlace("Place3");
        List<Place> places = asList(place1, place2, place3);

        // add members to trip plan
        User[] users = sessionManager.getUsers();
        User tripPlanOwner = users[0];
        List<Identifier> memberUserIds = asList(users[1].getId(), users[2].getId(), users[3].getId());

        // create trip plan
        sessionManager.beginSession(tripPlanOwner);
        TripPlan tripPlan = application.createTripPlan("Test Trip Plan");
        application.setTripPlanDuration(tripPlan.getId(), duration);
        application.addPlacesToTripPlan(tripPlan.getId(), places);
        application.addMembersToTripPlan(tripPlan.getId(), memberUserIds);
        sessionManager.endSession();
    }
}
