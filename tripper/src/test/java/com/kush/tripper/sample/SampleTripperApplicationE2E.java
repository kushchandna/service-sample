package com.kush.tripper.sample;

import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.ConnectionSpecification;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.location.Location;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationE2E {

    @Mock
    private ConnectionSpecification connSpec;
    @Mock
    private ApplicationLocator locator;
    @Mock
    private TripperItineraryView itineraryView;

    private SampleTripperApplication application;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        ApplicationClient client = new ApplicationClient();
        client.connect(connSpec);
        Executor executor = Executors.newSingleThreadExecutor();
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
        List<Identifier> allTrips = application.getAllTrips();
        Identifier savedLaddakhTripId = allTrips.iterator().next();
        Itinerary savedItineraryLaddakh = application.getItinerary(savedLaddakhTripId);
        application.displayItinerary(savedItineraryLaddakh);

        application.logout();
    }
}
