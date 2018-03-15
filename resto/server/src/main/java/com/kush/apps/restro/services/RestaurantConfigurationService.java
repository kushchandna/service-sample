package com.kush.apps.restro.services;

import com.kush.apps.restro.entities.Restaurant;
import com.kush.apps.restro.entities.RestaurantProfile;
import com.kush.apps.restro.persistors.RestaurantPersistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;
import com.kush.utils.id.Identifier;

@Service(name = "Restaurant Configuration")
public class RestaurantConfigurationService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod(name = "Create Restaurant")
    public Restaurant createRestaurant(RestaurantProfile restaurantProfile) {
        User currentUser = getCurrentUser();
        RestaurantPersistor persistor = getInstance(RestaurantPersistor.class);
        return persistor.createRestaurant(currentUser.getId(), restaurantProfile);
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Update Profile")
    public void updateProfile(Identifier restaurantId, RestaurantProfile updatedProfile) throws ServiceRequestFailedException {
        User currentUser = getCurrentUser();
        RestaurantPersistor persistor = getInstance(RestaurantPersistor.class);
        Restaurant restaurant;
        try {
            restaurant = persistor.fetch(restaurantId);
        } catch (PersistorOperationFailedException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
        if (restaurant == null) {
            throw new ServiceRequestFailedException("No restaurant found with id " + restaurantId);
        }
        if (!restaurant.getOwnerUserId().equals(currentUser.getId())) {
            throw new ServiceRequestFailedException("Only owner can update restaurant's profile");
        }
        persistor.updateProfile(restaurantId, updatedProfile);
    }
}
