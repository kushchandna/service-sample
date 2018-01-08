package com.kush.tripper.sample;

import java.util.List;

import com.kush.tripper.itinerary.Itinerary;
import com.kush.tripper.location.Location;
import com.kush.tripper.place.Place;
import com.kush.tripper.trip.Trip;
import com.kush.utils.id.Identifier;

public class SampleTripperClient {

    public void registerUser(String username, String password) {
    }

    public void login(String username, String password) {
    }

    public void logout() {
    }

    public Identifier saveTrip(Trip trip) {
        return null;
    }

    public Place findPlace(String placeName) {
        return null;
    }

    public Location getCurrentLocation() {
        return null;
    }

    public Itinerary getItinerary(Identifier id) {
        return null;
    }

    public void displayItinerary(Itinerary itinerary) {
    }

    public List<Identifier> getAllTrips() {
        return null;
    }
}
