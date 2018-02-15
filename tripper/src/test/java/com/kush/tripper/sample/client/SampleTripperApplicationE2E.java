package com.kush.tripper.sample.client;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.remoting.api.ConnectionSpecification;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.location.Location;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationE2E {

    private static final String SAMPLE_TRIPPER_APPLICATION_SERVICE = "Sample Tripper Application Service";
    private static final String SAMPLE_TRIPPER_USER_SERVICE = "Sample Tripper User Service";

    @Mock
    private ConnectionSpecification connSpec;
    @Mock
    private ApplicationLocator locator;
    @Mock
    private TripperItineraryView itineraryView;
    @Mock
    private ServiceRequestResolver requestResolver;

    private SampleTripperApplication application;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        when(connSpec.getResolver()).thenReturn(requestResolver);

        Executor executor = Executors.newSingleThreadExecutor();
        ApplicationClient client = new ApplicationClient();
        client.connect(connSpec);
        client.activateServiceClient(SampleTripperUserServiceClient.class, SAMPLE_TRIPPER_USER_SERVICE, executor);
        client.activateServiceClient(SampleTripperApplicationServiceClient.class, SAMPLE_TRIPPER_APPLICATION_SERVICE, executor);

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
