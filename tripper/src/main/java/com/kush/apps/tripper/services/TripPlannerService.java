package com.kush.apps.tripper.services;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Set;

import com.kush.apps.tripper.api.Duration;
import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.lib.location.api.Place;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

@Service
public class TripPlannerService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod
    public TripPlan createTripPlan(String tripPlanName) throws PersistorOperationFailedException {
        User currentUser = getCurrentUser();
        TripPlanPersistor persistor = getInstance(TripPlanPersistor.class);
        return persistor.createTripPlan(currentUser.getId(), tripPlanName);
    }

    @AuthenticationRequired
    @ServiceMethod
    public void addPlacesToTripPlan(Identifier tripPlanId, List<Place> placesToVisit)
            throws PersistorOperationFailedException, ValidationFailedException {
        TripPlanPersistor persistor = getInstance(TripPlanPersistor.class);
        validateTripPlanBelongsToCurrentUser(persistor, tripPlanId);
        persistor.addPlacesToTripPlan(tripPlanId, placesToVisit);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Place> getPlacesInTripPlan(Identifier tripPlanId)
            throws PersistorOperationFailedException, ValidationFailedException {
        TripPlanPersistor persistor = getInstance(TripPlanPersistor.class);
        validateTripPlanBelongsToCurrentUser(persistor, tripPlanId);
        return newArrayList(persistor.getPlacesInTripPlan(tripPlanId));
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<TripPlan> getTripPlans() throws PersistorOperationFailedException {
        User currentUser = getCurrentUser();
        TripPlanPersistor tripPlanPersistor = getInstance(TripPlanPersistor.class);
        return newArrayList(tripPlanPersistor.getTripPlansForUser(currentUser.getId()));
    }

    @AuthenticationRequired
    @ServiceMethod
    public void setTripPlanDuration(Identifier tripPlanId, Duration duration)
            throws PersistorOperationFailedException, ValidationFailedException {
        TripPlanPersistor persistor = getInstance(TripPlanPersistor.class);
        TripPlan tripPlan = validateTripPlanBelongsToCurrentUser(persistor, tripPlanId);
        TripPlan updatedTripPlan = new TripPlan(tripPlan.getId(), tripPlan.getCreatedBy(), tripPlan.getTripPlanName(), duration);
        persistor.save(updatedTripPlan);
    }

    @AuthenticationRequired
    @ServiceMethod
    public void addMembersToTripPlan(Identifier tripPlanId, Set<Identifier> memberUserIds)
            throws PersistorOperationFailedException, ValidationFailedException {
        TripPlanPersistor persistor = getInstance(TripPlanPersistor.class);
        validateTripPlanBelongsToCurrentUser(persistor, tripPlanId);
        persistor.addMembersToTripPlan(tripPlanId, memberUserIds);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(TripPlanPersistor.class);
    }

    private TripPlan getTripPlanForId(Identifier tripPlanId, TripPlanPersistor persistor)
            throws PersistorOperationFailedException {
        return persistor.fetch(tripPlanId);
    }

    private TripPlan validateTripPlanBelongsToCurrentUser(TripPlanPersistor persistor, Identifier tripPlanId)
            throws PersistorOperationFailedException, ValidationFailedException {
        User currentUser = getCurrentUser();
        TripPlan tripPlan = getTripPlanForId(tripPlanId, persistor);
        if (tripPlan == null) {
            throw new ValidationFailedException("No trip plan with specified id found");
        }
        if (!tripPlan.getCreatedBy().equals(currentUser.getId())) {
            throw new ValidationFailedException("Operation only supported for trip plans created by current user");
        }
        return tripPlan;
    }
}
