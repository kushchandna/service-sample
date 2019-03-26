package com.kush.apps.tripper.services;

import static java.util.stream.Collectors.toList;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kush.apps.tripper.api.Duration;
import com.kush.apps.tripper.api.DurationToTextAdapter;
import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.service.UserGroupService;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.questionnaire.Preference;
import com.kush.lib.questionnaire.PreferenceAnswer;
import com.kush.lib.questionnaire.PreferenceOption;
import com.kush.lib.questionnaire.PreferenceQuestion;
import com.kush.lib.questionnaire.PreferenceQuestionService;
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
    public TripPlan createTripPlan(String name) throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        Clock clock = getInstance(Clock.class);
        ZonedDateTime currentTime = ZonedDateTime.now(clock);

        UserGroupService userGroupService = getUserGroupService();
        Group tripGroup = userGroupService.createGroup("<trip-group>");

        PreferenceQuestionService questionService = getPreferenceQuestionService();
        PreferenceQuestion durationQuestion = questionService.createQuestion("Preferred Duration?");

        TripPlanPersistor tripPlanPersistor = getTripPlanPersistor();
        return tripPlanPersistor.createTripPlan(name, currentUserId, tripGroup, currentTime, durationQuestion);
    }

    @ServiceMethod
    @AuthenticationRequired
    public void addMembersToTrip(Identifier tripPlanId, Set<Identifier> userIds)
            throws PersistorOperationFailedException, ValidationFailedException {
        Identifier currentUserId = getCurrentUser().getId();

        TripPlan tripPlan = fetchTripPlan(tripPlanId);

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

    @ServiceMethod
    @AuthenticationRequired
    public void proposeDuration(Identifier tripPlanId, Duration duration, Preference preference)
            throws PersistorOperationFailedException {
        TripPlan tripPlan = fetchTripPlan(tripPlanId);
        PreferenceQuestionService questionService = getPreferenceQuestionService();
        DurationToTextAdapter toTextAdapter = getInstance(DurationToTextAdapter.class);
        String content = toTextAdapter.toText(duration);
        Identifier prefQuesId = tripPlan.getDurationPreferenceQuestion().getId();
        PreferenceOption option = questionService.addOptionIfDoesNotExist(prefQuesId, content);
        questionService.addAnswer(tripPlanId, option.getId(), preference);
    }

    @ServiceMethod
    @AuthenticationRequired
    public Map<Preference, List<PreferenceAnswer>> getDurationPreferences(Identifier tripPlanId)
            throws PersistorOperationFailedException {
        TripPlan tripPlan = fetchTripPlan(tripPlanId);
        PreferenceQuestionService questionService = getPreferenceQuestionService();
        Identifier prefQuesId = tripPlan.getDurationPreferenceQuestion().getId();
        return questionService.getAnswersSummary(prefQuesId);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(TripPlanPersistor.class);
        checkContextHasValueFor(UserGroupService.class);
        checkContextHasValueFor(PreferenceQuestionService.class);
        addIfDoesNotExist(Clock.class, Clock.systemUTC());
        addIfDoesNotExist(DurationToTextAdapter.class, new DurationToTextAdapter() {

            @Override
            public String toText(Duration duration) {
                return duration.toString();
            }
        });
    }

    private UserGroupService getUserGroupService() {
        return getInstance(UserGroupService.class);
    }

    private TripPlanPersistor getTripPlanPersistor() {
        return getInstance(TripPlanPersistor.class);
    }

    private PreferenceQuestionService getPreferenceQuestionService() {
        return getInstance(PreferenceQuestionService.class);
    }

    private TripPlan fetchTripPlan(Identifier tripPlanId) throws PersistorOperationFailedException {
        TripPlanPersistor tripPlanPersistor = getTripPlanPersistor();
        return tripPlanPersistor.fetch(tripPlanId);
    }
}
