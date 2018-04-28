package com.kush.apps.tripper.persistors;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.api.TripPlanMember;
import com.kush.apps.tripper.api.TripPlanPlace;
import com.kush.lib.location.api.Place;
import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultTripPlanPersistor extends DelegatingPersistor<TripPlan> implements TripPlanPersistor {

    private final Persistor<TripPlanPlace> tripPlanPlacePersistor;
    private final Persistor<TripPlanMember> tripPlanMemberPersistor;

    public DefaultTripPlanPersistor(Persistor<TripPlan> delegate, Persistor<TripPlanPlace> tripPlanPlacePersistor,
            Persistor<TripPlanMember> tripPlanMemberPersistor) {
        super(delegate);
        this.tripPlanPlacePersistor = tripPlanPlacePersistor;
        this.tripPlanMemberPersistor = tripPlanMemberPersistor;
    }

    @Override
    public TripPlan createTripPlan(Identifier createdBy, String tripPlanName) throws PersistorOperationFailedException {
        TripPlan tripPlan = new TripPlan(createdBy, tripPlanName, null);
        return save(tripPlan);
    }

    @Override
    public List<TripPlan> getTripPlansForUser(Identifier userId) throws PersistorOperationFailedException {
        return fetch(p -> p.getCreatedBy().equals(userId));
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
    public List<Place> getPlacesInTripPlan(Identifier tripPlanId) throws PersistorOperationFailedException {
        List<TripPlanPlace> tripPlanPlaces = tripPlanPlacePersistor.fetch(tpp -> tpp.getTripPlan().getId().equals(tripPlanId));
        return tripPlanPlaces.stream().map(tpp -> tpp.getPlace()).collect(Collectors.toList());
    }

    @Override
    public void addMembersToTripPlan(Identifier tripPlanId, Set<Identifier> userIds) throws PersistorOperationFailedException {
        TripPlan tripPlan = fetch(tripPlanId);
        for (Identifier userId : userIds) {
            TripPlanMember tripPlanMember = new TripPlanMember(tripPlan, userId);
            tripPlanMemberPersistor.save(tripPlanMember);
        }
    }
}
