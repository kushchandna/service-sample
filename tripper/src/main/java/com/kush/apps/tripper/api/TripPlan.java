package com.kush.apps.tripper.api;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.kush.lib.group.entities.Group;
import com.kush.lib.questionnaire.PreferenceQuestion;
import com.kush.service.annotations.Exportable;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

@Exportable
public class TripPlan implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier tripId;
    private final String name;
    private final Identifier ownerUser;
    private final Group tripGroup;
    private final ZonedDateTime creationTime;
    private final PreferenceQuestion durationPreferenceQuestion;


    public TripPlan(String name, Identifier ownerUser, Group tripGroup, ZonedDateTime creationTime,
            PreferenceQuestion durationPreferenceQuestion) {
        this(Identifier.NULL, name, ownerUser, tripGroup, creationTime, durationPreferenceQuestion);
    }

    public TripPlan(Identifier tripId, TripPlan tripPlan) {
        this(tripId, tripPlan.getName(), tripPlan.getOwnerUser(), tripPlan.getTripGroup(), tripPlan.getCreationTime(),
                tripPlan.getDurationPreferenceQuestion());
    }

    public TripPlan(Identifier tripId, String name, Identifier ownerUser, Group tripGroup, ZonedDateTime creationTime,
            PreferenceQuestion durationPreferenceQuestion) {
        this.tripId = tripId;
        this.name = name;
        this.ownerUser = ownerUser;
        this.tripGroup = tripGroup;
        this.creationTime = creationTime;
        this.durationPreferenceQuestion = durationPreferenceQuestion;
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

    public PreferenceQuestion getDurationPreferenceQuestion() {
        return durationPreferenceQuestion;
    }
}
