package com.kush.apps.tripper.services;

import java.util.Iterator;
import java.util.List;

import com.kush.apps.tripper.api.Trip;
import com.kush.apps.tripper.persistors.TripPersistor;
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
    @ServiceMethod(name = "Create Trip")
    public Trip createTrip(String tripName) throws ServiceRequestFailedException {
        User currentUser = getCurrentUser();
        TripPersistor persistor = getInstance(TripPersistor.class);
        try {
            return persistor.createTrip(currentUser.getId(), tripName);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Add Places")
    public void addPlaces(Identifier tripId, List<Place> placesToVisit) throws ServiceRequestFailedException {
        User currentUser = getCurrentUser();
        TripPersistor persistor = getInstance(TripPersistor.class);
        Trip trip = getTripForId(tripId, persistor);
        if (trip == null) {
            throw new ServiceRequestFailedException("No trip with specified id found");
        }
        if (!trip.getCreatedBy().equals(currentUser.getId())) {
            throw new ServiceRequestFailedException("Specified trip isn't created by current user, hence can't be edited");
        }
        try {
            persistor.addPlacesToTrip(tripId, placesToVisit);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Get Places")
    public Iterator<Place> getPlaces(Identifier tripId) throws ServiceRequestFailedException {
        User currentUser = getCurrentUser();
        TripPersistor persistor = getInstance(TripPersistor.class);
        Trip trip = getTripForId(tripId, persistor);
        if (trip == null) {
            throw new ServiceRequestFailedException("No trip with specified id found");
        }
        if (!trip.getCreatedBy().equals(currentUser.getId())) {
            throw new ServiceRequestFailedException("Specified trip isn't created by current user, hence can't be edited");
        }
        try {
            return persistor.getPlacesFromTrip(tripId);
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

    private Trip getTripForId(Identifier tripId, TripPersistor persistor) throws ServiceRequestFailedException {
        try {
            return persistor.fetch(tripId);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }
}
