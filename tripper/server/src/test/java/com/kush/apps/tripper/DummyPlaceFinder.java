package com.kush.apps.tripper;

import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;

public class DummyPlaceFinder implements PlaceFinder {

    @Override
    public Place find(String text) {
        return new RandomPlace(text);
    }
}
