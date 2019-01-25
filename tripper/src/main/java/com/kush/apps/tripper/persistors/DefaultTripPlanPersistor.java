package com.kush.apps.tripper.persistors;

import java.time.ZonedDateTime;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.lib.group.entities.Group;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultTripPlanPersistor extends DelegatingPersistor<TripPlan> implements TripPlanPersistor {

    public DefaultTripPlanPersistor(Persistor<TripPlan> delegate) {
        super(delegate);
    }

    @Override
    public TripPlan createTripPlan(String name, Identifier ownerUserId, Group tripGroup, ZonedDateTime creationTime)
            throws PersistorOperationFailedException {
        TripPlan tripPlan = new TripPlan(name, ownerUserId, tripGroup, creationTime);
        return save(tripPlan);
    }
}
