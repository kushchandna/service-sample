package com.kush.apps.tripper;

import java.util.Random;

import com.kush.lib.location.api.Location;
import com.kush.lib.location.api.Place;

class RandomPlace extends Place {

    private static final long serialVersionUID = 1L;

    private static final Random RANDOM = new Random();

    public RandomPlace(String name) {
        super(name, randomLocation(), "Address: " + name);
    }

    private static Location randomLocation() {
        return new Location(RANDOM.nextDouble(), RANDOM.nextDouble());
    }
}
