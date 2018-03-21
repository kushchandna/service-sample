package com.kush.apps.tripper.services;

import java.util.Iterator;
import java.util.List;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.lib.location.api.Place;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;
import com.kush.utils.id.Identifier;

@Service(name = "Trip Planner")
public class TripPlannerService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod(name = "Create Trip Plan")
    public TripPlan createTripPlan(String tripPlanName) throws ServiceRequestFailedException {
        User currentUser = getCurrentUser();
        TripPlanPersistor persistor = getInstance(TripPlanPersistor.class);
        try {
            return persistor.createTripPlan(currentUser.getId(), tripPlanName);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Add Places To Trip Plan")
    public void addPlacesToTripPlan(Identifier tripPlanId, List<Place> placesToVisit) throws ServiceRequestFailedException {
        TripPlanPersistor persistor = getInstance(TripPlanPersistor.class);
        validateTripPlanBelongsToCurrentUser(persistor, tripPlanId);
        try {
            persistor.addPlacesToTripPlan(tripPlanId, placesToVisit);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Get Places In Trip Plan")
    public Iterator<Place> getPlacesInTripPlan(Identifier tripPlanId) throws ServiceRequestFailedException {
        TripPlanPersistor persistor = getInstance(TripPlanPersistor.class);
        validateTripPlanBelongsToCurrentUser(persistor, tripPlanId);
        try {
            return persistor.getPlacesInTripPlan(tripPlanId);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Get Trip Plans")
    public Iterator<TripPlan> getTripPlans() throws ServiceRequestFailedException {
        User currentUser = getCurrentUser();
        TripPlanPersistor tripPlanPersistor = getInstance(TripPlanPersistor.class);
        try {
            return tripPlanPersistor.getTripPlansForUser(currentUser.getId());
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    private TripPlan getTripPlanForId(Identifier tripPlanId, TripPlanPersistor persistor) throws ServiceRequestFailedException {
        try {
            return persistor.fetch(tripPlanId);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    private void validateTripPlanBelongsToCurrentUser(TripPlanPersistor persistor, Identifier tripPlanId)
            throws ServiceRequestFailedException {
        User currentUser = getCurrentUser();
        TripPlan tripPlan = getTripPlanForId(tripPlanId, persistor);
        if (tripPlan == null) {
            throw new ServiceRequestFailedException("No trip plan with specified id found");
        }
        if (!tripPlan.getCreatedBy().equals(currentUser.getId())) {
            throw new ServiceRequestFailedException("Operation only supported for trip plans created by current user");
        }
    }
}
