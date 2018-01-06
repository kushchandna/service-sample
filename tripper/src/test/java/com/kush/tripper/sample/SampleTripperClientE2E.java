package com.kush.tripper.sample;

import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import com.kush.lib.service.api.Identifier;
import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.location.Location;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;

public class SampleTripperClientE2E {

    @Test
    public void e2e() throws Exception {
        SampleTripperClient client = new SampleTripperClient();
        client.registerUser("testuser1", "testpwd1");
        client.login("testuser1", "testpwd1");

        Trip tripLaddakh = new Trip();
        tripLaddakh.setName("Laddakh Trip");
        tripLaddakh.setDurationInDays(9);
        tripLaddakh.setStartTime(LocalDateTime.now());
        Location currentLocation = client.getCurrentLocation();
        tripLaddakh.setStartingPoint(currentLocation);
        tripLaddakh.setReturnPoint(currentLocation);

        Place placeRohtangPass = client.findPlace("Rohtang Pass");
        Place placeKeylong = client.findPlace("Keylong, Jispa");
        Place placeLeh = client.findPlace("Leh");
        Place placePangongLake = client.findPlace("Pangong Lake");
        Place placeGoldenTemple = client.findPlace("Golden Temple");
        tripLaddakh.setPlacesToVisit(asList(placeRohtangPass, placeKeylong, placeLeh, placePangongLake, placeGoldenTemple));

        Identifier idTripLaddakh = client.saveTrip(tripLaddakh);

        Itinerary itineraryLaddakh = client.getItinerary(idTripLaddakh);
        client.displayItinerary(itineraryLaddakh);

        client.logout();

        client.login("testuser1", "testpwd1");
        List<Identifier> allTrips = client.getAllTrips();
        Identifier savedLaddakhTripId = allTrips.iterator().next();
        Itinerary savedItineraryLaddakh = client.getItinerary(savedLaddakhTripId);
        client.displayItinerary(savedItineraryLaddakh);

        client.logout();
    }
}
