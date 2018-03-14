package com.kush.apps.tripper.persistors;

import java.util.Iterator;
import java.util.List;

import com.kush.apps.tripper.api.Trip;
import com.kush.lib.location.api.Place;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface TripPersistor extends Persistor<Trip> {

    Trip createTrip(Identifier createdBy, String tripName) throws PersistorOperationFailedException;

    Iterator<Trip> getTripsCreatedByUser(Identifier userId) throws PersistorOperationFailedException;

    void addPlacesToTrip(Identifier tripId, List<Place> placesToVisit) throws PersistorOperationFailedException;
}
