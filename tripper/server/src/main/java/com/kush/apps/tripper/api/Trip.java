package com.kush.apps.tripper.api;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

@Exportable
public class Trip implements Identifiable {

    private final Identifier tripId;
    private final Identifier createdBy;
    private final String tripName;

    public Trip(Identifier createdBy, String tripName) {
        this(Identifier.NULL, createdBy, tripName);
    }

    public Trip(Identifier tripId, Identifier createdBy, String tripName) {
        this.tripId = tripId;
        this.createdBy = createdBy;
        this.tripName = tripName;
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
}
