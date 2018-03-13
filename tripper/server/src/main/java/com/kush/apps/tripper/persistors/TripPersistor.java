package com.kush.apps.tripper.persistors;

import java.util.Iterator;

import com.kush.apps.tripper.api.Trip;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface TripPersistor extends Persistor<Trip> {

    Iterator<Trip> getTripsCreatedByUser(Identifier userId) throws PersistorOperationFailedException;
}
