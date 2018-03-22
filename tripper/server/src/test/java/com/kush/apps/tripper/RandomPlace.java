package com.kush.apps.tripper;

import java.util.Random;

import com.kush.lib.location.api.Location;
import com.kush.lib.location.api.Place;

class RandomPlace implements Place {

    private static final long serialVersionUID = 1L;

    private static final Random RANDOM = new Random();

    private final Location location = new Location(RANDOM.nextDouble(), RANDOM.nextDouble());

    private final String name;

    public RandomPlace(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getAddress() {
        return "Address: " + name;
    }
}
