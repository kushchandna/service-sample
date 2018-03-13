package com.kush.apps.tripper.services;

import java.util.Iterator;

import com.kush.apps.tripper.api.Trip;
import com.kush.apps.tripper.persistors.TripPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;

@Service(name = "Trip Planner")
public class TripPlannerService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod(name = "Create Trip")
    public Trip createTrip(String tripName) throws ServiceRequestFailedException {
        User currentUser = getCurrentUser();
        Trip trip = new Trip(currentUser.getId(), tripName);
        Persistor<Trip> persistor = getPersistor(Trip.class);
        try {
            return persistor.save(trip);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Get Created Trips")
    public Iterator<Trip> getCreatedTrips() throws ServiceRequestFailedException {
        User currentUser = getCurrentUser();
        TripPersistor tripPersistor = getInstance(TripPersistor.class);
        try {
            return tripPersistor.getTripsCreatedByUser(currentUser.getId());
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }
}
