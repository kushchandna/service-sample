package com.kush.apps.tripper.api;

import java.util.Collections;
import java.util.List;

import com.kush.lib.location.api.Place;
import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

@Exportable
public class Trip implements Identifiable {

    private final Identifier tripId;
    private final Identifier createdBy;
    private final String tripName;
    private final List<Place> placesToVisit;

    public Trip(Identifier createdBy, String tripName, List<Place> placesToVisit) {
        this(Identifier.NULL, createdBy, tripName, placesToVisit);
    }

    public Trip(Identifier tripId, Identifier createdBy, String tripName, List<Place> placesToVisit) {
        this.tripId = tripId;
        this.createdBy = createdBy;
        this.tripName = tripName;
        this.placesToVisit = placesToVisit;
    }

    @Override
    public Identifier getId() {
        return tripId;
    }

    public Identifier getCreatedBy() {
        return createdBy;
    }

    public String getTripName() {
        return tripName;
    }

    public List<Place> getPlacesToVisit() {
        return Collections.unmodifiableList(placesToVisit);
    }
}
