package com.kush.apps.tripper.persistors;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.lib.location.api.Place;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface TripPlanPersistor extends Persistor<TripPlan> {

    TripPlan createTripPlan(Identifier createdBy, String tripPlanName) throws PersistorOperationFailedException;

    Iterator<TripPlan> getTripPlansForUser(Identifier userId) throws PersistorOperationFailedException;

    void addPlacesToTripPlan(Identifier tripPlanId, List<Place> placesToVisit) throws PersistorOperationFailedException;

    Iterator<Place> getPlacesInTripPlan(Identifier tripPlanId) throws PersistorOperationFailedException;

    void addMembersToTripPlan(Identifier tripPlanId, Set<Identifier> userIds) throws PersistorOperationFailedException;
}
