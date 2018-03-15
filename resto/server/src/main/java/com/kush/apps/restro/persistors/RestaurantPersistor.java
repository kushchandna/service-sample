package com.kush.apps.restro.persistors;

import com.kush.apps.restro.entities.Restaurant;
import com.kush.apps.restro.entities.RestaurantProfile;
import com.kush.lib.persistence.api.Persistor;
import com.kush.utils.id.Identifier;

public interface RestaurantPersistor extends Persistor<Restaurant> {

    Restaurant createRestaurant(Identifier ownerUserId, RestaurantProfile profile);

    void updateProfile(Identifier restaurantId, RestaurantProfile updatedProfile);
}
