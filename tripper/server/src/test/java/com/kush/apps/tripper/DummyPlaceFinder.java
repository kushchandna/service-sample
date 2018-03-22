package com.kush.apps.tripper;

import java.util.Random;

import com.kush.lib.location.api.Location;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;

public class DummyPlaceFinder implements PlaceFinder {

    private static final Random RANDOM = new Random();

    @Override
    public Place find(String text) {
        return new Place() {

            private static final long serialVersionUID = 1L;

            private final Location location = new Location(RANDOM.nextDouble(), RANDOM.nextDouble());

            @Override
            public String getName() {
                return text;
            }

            @Override
            public Location getLocation() {
                return location;
            }

            @Override
            public String getAddress() {
                return "Address: " + text;
            }
        };
    }
}
