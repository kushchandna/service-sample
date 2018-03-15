package com.kush.apps.restro.entities;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

@Exportable
public class Restaurant implements Identifiable {

    private final Identifier restaurantId;
    private final Identifier ownerUserId;
    private final RestaurantProfile restaurantProfile;

    public Restaurant(Identifier restaurantId, Identifier ownerUserId, RestaurantProfile restaurantProfile) {
        this.restaurantId = restaurantId;
        this.ownerUserId = ownerUserId;
        this.restaurantProfile = restaurantProfile;
    }

    @Override
    public Identifier getId() {
        return restaurantId;
    }

    public Identifier getOwnerUserId() {
        return ownerUserId;
    }

    public RestaurantProfile getRestaurantProfile() {
        return restaurantProfile;
    }
}
