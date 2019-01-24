package com.kush.apps.tripper.services;

import static java.util.stream.Collectors.toList;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.service.UserGroupService;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

@Service
public class TripperPlanningService extends BaseService {

    @ServiceMethod
    @AuthenticationRequired
    public TripPlan createTripPlan() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        Clock clock = getInstance(Clock.class);
        ZonedDateTime currentTime = ZonedDateTime.now(clock);

        UserGroupService userGroupService = getUserGroupService();
        Group tripGroup = userGroupService.createGroup("<trip-group>");

        TripPlanPersistor tripPlanPersistor = getTripPlanPersistor();
        return tripPlanPersistor.createTripPlan(currentUserId, tripGroup, currentTime);
    }

    @ServiceMethod
    @AuthenticationRequired
    public void addMembersToTrip(Identifier tripPlanId, Set<Identifier> userIds)
            throws PersistorOperationFailedException, ValidationFailedException {
        Identifier currentUserId = getCurrentUser().getId();

        TripPlanPersistor tripPlanPersistor = getTripPlanPersistor();
        TripPlan tripPlan = tripPlanPersistor.fetch(tripPlanId);

        Group tripGroup = tripPlan.getTripGroup();
        UserGroupService userGroupService = getUserGroupService();
        List<GroupMembership> groupMemberships = userGroupService.getGroupMembers(tripGroup.getId());

        boolean currentUserIsMember = groupMemberships.stream().anyMatch(m -> m.getMember().equals(currentUserId));
        if (!currentUserIsMember) {
            throw new ValidationFailedException("User [%s] is not a trip member", currentUserId);
        }

        userGroupService.addMembers(tripGroup.getId(), userIds);
    }

    @ServiceMethod
    @AuthenticationRequired
    public List<TripPlan> getTripPlans() throws PersistorOperationFailedException {
        UserGroupService userGroupService = getUserGroupService();
        List<Group> groups = userGroupService.getGroups();
        List<Identifier> groupIds = groups.stream().map(g -> g.getId()).collect(toList());
        TripPlanPersistor tripPlanPersistor = getTripPlanPersistor();
        return tripPlanPersistor.fetch(plan -> groupIds.contains(plan.getTripGroup().getId()));
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(TripPlanPersistor.class);
        checkContextHasValueFor(UserGroupService.class);
        addIfDoesNotExist(Clock.class, Clock.systemUTC());
    }

    private UserGroupService getUserGroupService() {
        return getInstance(UserGroupService.class);
    }

    private TripPlanPersistor getTripPlanPersistor() {
        return getInstance(TripPlanPersistor.class);
    }
}
