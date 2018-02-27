package com.kush.tripper.sample.client;

import static com.kush.utils.id.Identifier.id;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.connect.ServiceConnection;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.location.Location;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationE2E {

    @Mock
    private ApplicationLocator locator;
    @Mock
    private TripperItineraryView itineraryView;
    @Mock
    private ServiceConnection connection;
    @Mock
    private ServiceConnectionFactory connFactory;

    @Captor
    private ArgumentCaptor<ServiceRequest> requestCaptor;

    private SampleTripperApplication application;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        when(connFactory.createConnection()).thenReturn(connection);
        doAnswer(new Answer<Identifier[]>() {

            @Override
            public Identifier[] answer(InvocationOnMock invocation) throws Throwable {
                ServiceRequest request = requestCaptor.getValue();
                if (request.getMethodName().equals("getAllTrips")) {
                    return new Identifier[] { id("Saved Trip") };
                }
                return null;
            }
        }).when(connection).resolve(requestCaptor.capture());

        Executor executor = Executors.newSingleThreadExecutor();
        ApplicationClient client = new ApplicationClient();
        client.start(connFactory);
        client.activateServiceClient(SampleTripperUserServiceClient.class, executor);
        client.activateServiceClient(SampleTripperApplicationServiceClient.class, executor);

        application = new SampleTripperApplication(client, locator, itineraryView);
    }

    @Test
    public void e2e() throws Exception {
        application.registerUser("testuser1", "testpwd1");
        application.login("testuser1", "testpwd1");

        Trip tripLaddakh = new Trip();
        tripLaddakh.setName("Laddakh Trip");
        tripLaddakh.setDurationInDays(9);
        tripLaddakh.setStartTime(LocalDateTime.now());
        Location currentLocation = application.getCurrentLocation();
        tripLaddakh.setStartingPoint(currentLocation);
        tripLaddakh.setReturnPoint(currentLocation);

        Place placeRohtangPass = application.findPlace("Rohtang Pass");
        Place placeKeylong = application.findPlace("Keylong, Jispa");
        Place placeLeh = application.findPlace("Leh");
        Place placePangongLake = application.findPlace("Pangong Lake");
        Place placeGoldenTemple = application.findPlace("Golden Temple");
        tripLaddakh.setPlacesToVisit(asList(placeRohtangPass, placeKeylong, placeLeh, placePangongLake, placeGoldenTemple));

        Identifier idTripLaddakh = application.saveTrip(tripLaddakh);

        Itinerary itineraryLaddakh = application.getItinerary(idTripLaddakh);
        application.displayItinerary(itineraryLaddakh);

        application.logout();

        application.login("testuser1", "testpwd1");
        Identifier[] allTrips = application.getAllTrips();
        Identifier savedLaddakhTripId = allTrips[0];
        Itinerary savedItineraryLaddakh = application.getItinerary(savedLaddakhTripId);
        application.displayItinerary(savedItineraryLaddakh);

        application.logout();
    }
}
