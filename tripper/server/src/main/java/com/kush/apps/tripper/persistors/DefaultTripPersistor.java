package com.kush.apps.tripper.persistors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.api.TripPlanPlace;
import com.kush.lib.location.api.Place;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultTripPersistor extends DelegatingPersistor<TripPlan> implements TripPlanPersistor {

    private final Persistor<TripPlanPlace> tripPlanPlacePersistor;

    public DefaultTripPersistor(Persistor<TripPlan> delegate, Persistor<TripPlanPlace> tripPlanPlacePersistor) {
        super(delegate);
        this.tripPlanPlacePersistor = tripPlanPlacePersistor;
    }

    @Override
    public TripPlan createTripPlan(Identifier createdBy, String tripPlanName) throws PersistorOperationFailedException {
        TripPlan tripPlan = new TripPlan(createdBy, tripPlanName);
        return save(tripPlan);
    }

    @Override
    public Iterator<TripPlan> getTripPlansForUser(Identifier userId) throws PersistorOperationFailedException {
        List<TripPlan> tripPlansForUser = new ArrayList<>();
        Iterator<TripPlan> allTripPlans = fetchAll();
        while (allTripPlans.hasNext()) {
            TripPlan tripPlan = allTripPlans.next();
            if (tripPlan.getCreatedBy().equals(userId)) {
                tripPlansForUser.add(tripPlan);
            }
        }
        return tripPlansForUser.iterator();
    }

    @Override
    public void addPlacesToTripPlan(Identifier tripPlanId, List<Place> placesToVisit) throws PersistorOperationFailedException {
        TripPlan tripPlan = fetch(tripPlanId);
        for (Place place : placesToVisit) {
            TripPlanPlace tripPlanPlace = new TripPlanPlace(tripPlan, place);
            tripPlanPlacePersistor.save(tripPlanPlace);
        }
    }

    @Override
    public Iterator<Place> getPlacesInTripPlan(Identifier tripPlanId) throws PersistorOperationFailedException {
        List<Place> result = new ArrayList<>();
        Iterator<TripPlanPlace> allTripPlanPlaces = tripPlanPlacePersistor.fetchAll();
        while (allTripPlanPlaces.hasNext()) {
            TripPlanPlace tripPlanPlace = allTripPlanPlaces.next();
            if (tripPlanPlace.getTrip().getId().equals(tripPlanId)) {
                result.add(tripPlanPlace.getPlace());
            }
        }
        return result.iterator();
    }
}
