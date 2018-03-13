package com.kush.apps.tripper.services;

import com.kush.apps.tripper.api.Trip;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;

@Service(name = "Trip Planner")
public class TripPlannerService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod(name = "Create Trip")
    public Trip createTrip(String tripName) {
        User currentUser = getCurrentUser();
        return new Trip(currentUser.getId(), tripName);
    }
}
