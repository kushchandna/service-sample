package com.kush.apps.tripper.api;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.kush.lib.group.entities.Group;
import com.kush.service.annotations.Exportable;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

@Exportable
public class TripPlan implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier tripId;
    private final Identifier ownerUser;
    private Group tripGroup;
    private final ZonedDateTime creationTime;

    public TripPlan(Identifier ownerUser, Group tripGroup, ZonedDateTime creationTime) {
        this(Identifier.NULL, ownerUser, tripGroup, creationTime);
    }

    public TripPlan(Identifier tripId, TripPlan tripPlan) {
        this(tripId, tripPlan.getOwnerUser(), tripPlan.getTripGroup(), tripPlan.getCreationTime());
    }

    public TripPlan(Identifier tripId, Identifier ownerUser, Group tripGroup, ZonedDateTime creationTime) {
        this.tripId = tripId;
        this.ownerUser = ownerUser;
        this.setTripGroup(tripGroup);
        this.creationTime = creationTime;
    }

    @Override
    public Identifier getId() {
        return tripId;
    }

    public Identifier getOwnerUser() {
        return ownerUser;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public Group getTripGroup() {
        return tripGroup;
    }

    public void setTripGroup(Group tripGroup) {
        this.tripGroup = tripGroup;
    }
}
