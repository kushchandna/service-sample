package com.kush.apps.tripper.persistors;

import java.time.ZonedDateTime;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.lib.group.entities.Group;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface TripPlanPersistor extends Persistor<TripPlan> {

    TripPlan createTripPlan(Identifier ownerUserId, Group tripGroup, ZonedDateTime creationTime)
            throws PersistorOperationFailedException;
}
