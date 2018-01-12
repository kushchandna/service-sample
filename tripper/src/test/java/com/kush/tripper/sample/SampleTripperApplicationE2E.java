package com.kush.tripper.sample;

import static com.kush.utils.id.Identifier.id;
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
import com.kush.lib.service.client.api.ConnectionSpecification;
import com.kush.lib.service.remoting.ServiceProvider;
import com.kush.tripper.location.Location;
import com.kush.tripper.sample.api.SampleTripperApplicationServiceApi;
import com.kush.tripper.sample.api.SampleTripperUserServiceApi;
import com.kush.tripper.sample.api.types.Itinerary;
import com.kush.tripper.sample.api.types.Place;
import com.kush.tripper.sample.api.types.Trip;
import com.kush.tripper.sample.client.ApplicationLocator;
import com.kush.tripper.sample.client.SampleTripperApplicationServiceClient;
import com.kush.tripper.sample.client.SampleTripperClient;
import com.kush.tripper.sample.client.SampleTripperUserServiceClient;
import com.kush.tripper.sample.client.TripperItineraryView;
import com.kush.tripper.sample.server.SampleTripperApplicationService;
import com.kush.tripper.sample.server.SampleTripperUserService;
import com.kush.utils.id.Identifier;

public class SampleTripperApplicationE2E {

    @Mock
    private ConnectionSpecification connSpec;
    @Mock
    private ServiceProvider serviceProvider;
    @Mock
    private ApplicationLocator locator;
    @Mock
    private TripperItineraryView itineraryView;
    @Mock
    private SampleTripperUserService userService;
    @Mock
    private SampleTripperApplicationService applicationService;

    private SampleTripperClient application;

    @Before
    public void setup() throws Exception {
        initMocks(this);

        when(connSpec.getRemoteServiceProvider()).thenReturn(serviceProvider);
        when(serviceProvider.getService(SampleTripperUserServiceApi.class)).thenReturn(userService);
        when(serviceProvider.getService(SampleTripperApplicationServiceApi.class)).thenReturn(applicationService);
        when(applicationService.getAllTrips()).thenReturn(new Identifier[] { id("Random Id") });

        Executor executor = Executors.newSingleThreadExecutor();
        ApplicationClient client = new ApplicationClient();
        client.connect(connSpec);
        client.activateServiceClient(SampleTripperUserServiceClient.class, SampleTripperUserServiceApi.class, executor);
        client.activateServiceClient(SampleTripperApplicationServiceClient.class, SampleTripperApplicationServiceApi.class,
                executor);

        application = new SampleTripperClient(client, locator, itineraryView);
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
