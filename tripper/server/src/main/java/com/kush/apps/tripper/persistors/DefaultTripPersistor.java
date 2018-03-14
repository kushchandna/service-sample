package com.kush.apps.tripper.persistors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.kush.apps.tripper.api.Trip;
import com.kush.lib.location.api.Place;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultTripPersistor extends DelegatingPersistor<Trip> implements TripPersistor {

    public DefaultTripPersistor(Persistor<Trip> delegate) {
        super(delegate);
    }

    @Override
    public Trip createTrip(Identifier createdBy, String tripName) throws PersistorOperationFailedException {
        Trip trip = new Trip(createdBy, tripName, Collections.emptyList());
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
        Trip existing = fetch(tripId);
        List<Place> existingPlacesToVisit = new ArrayList<>(existing.getPlacesToVisit());
        existingPlacesToVisit.addAll(placesToVisit);
        Trip updated = new Trip(existing.getId(), existing.getCreatedBy(), existing.getTripName(), existingPlacesToVisit);
        save(updated);
    }
}
