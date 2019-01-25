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
    private final String name;
    private final Identifier ownerUser;
    private Group tripGroup;
    private final ZonedDateTime creationTime;


    public TripPlan(String name, Identifier ownerUser, Group tripGroup, ZonedDateTime creationTime) {
        this(Identifier.NULL, name, ownerUser, tripGroup, creationTime);
    }

    public TripPlan(Identifier tripId, TripPlan tripPlan) {
        this(tripId, tripPlan.getName(), tripPlan.getOwnerUser(), tripPlan.getTripGroup(), tripPlan.getCreationTime());
    }

    public TripPlan(Identifier tripId, String name, Identifier ownerUser, Group tripGroup, ZonedDateTime creationTime) {
        this.tripId = tripId;
        this.name = name;
        this.ownerUser = ownerUser;
        this.setTripGroup(tripGroup);
        this.creationTime = creationTime;
    }

    @Override
    public Identifier getId() {
        return tripId;
    }

    public String getName() {
        return name;
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
