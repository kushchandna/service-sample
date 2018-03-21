package com.kush.apps.tripper.persistors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kush.apps.tripper.api.Trip;
import com.kush.apps.tripper.api.TripPlace;
import com.kush.lib.location.api.Place;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultTripPersistor extends DelegatingPersistor<Trip> implements TripPersistor {

    private final Persistor<TripPlace> tripPlacePersistor;

    public DefaultTripPersistor(Persistor<Trip> delegate, Persistor<TripPlace> tripPlacePersistor) {
        super(delegate);
        this.tripPlacePersistor = tripPlacePersistor;
    }

    @Override
    public Trip createTrip(Identifier createdBy, String tripName) throws PersistorOperationFailedException {
        Trip trip = new Trip(createdBy, tripName);
        return save(trip);
    }

    @Override
    public Iterator<Trip> getTripsCreatedByUser(Identifier userId) throws PersistorOperationFailedException {
        List<Trip> tripsCreatedByUser = new ArrayList<>();
        Iterator<Trip> allTrips = fetchAll();
        while (allTrips.hasNext()) {
            Trip trip = allTrips.next();
            if (trip.getCreatedBy().equals(userId)) {
                tripsCreatedByUser.add(trip);
            }
        }
        return tripsCreatedByUser.iterator();
    }

    @Override
    public void addPlacesToTrip(Identifier tripId, List<Place> placesToVisit) throws PersistorOperationFailedException {
        Trip trip = fetch(tripId);
        for (Place place : placesToVisit) {
            TripPlace tripPlace = new TripPlace(trip, place);
            tripPlacePersistor.save(tripPlace);
        }
    }

    @Override
    public Iterator<Place> getPlacesFromTrip(Identifier tripId) throws PersistorOperationFailedException {
        List<Place> result = new ArrayList<>();
        Iterator<TripPlace> allTripPlaces = tripPlacePersistor.fetchAll();
        while (allTripPlaces.hasNext()) {
            TripPlace tripPlace = allTripPlaces.next();
            if (tripPlace.getTrip().getId().equals(tripId)) {
                result.add(tripPlace.getPlace());
            }
        }
        return result.iterator();
    }
}
