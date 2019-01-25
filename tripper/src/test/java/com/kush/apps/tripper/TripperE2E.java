package com.kush.apps.tripper;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.persistors.DefaultTripPlanPersistor;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.apps.tripper.services.TripperMessagingService;
import com.kush.apps.tripper.services.TripperPlanningService;
import com.kush.apps.tripper.services.TripperProfileService;
import com.kush.lib.group.entities.DefaultGroupPersistor;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.group.service.UserGroupService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.profile.entities.DefaultProfilePersistor;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.profile.fields.Field;
import com.kush.lib.profile.fields.Fields;
import com.kush.lib.profile.fields.validators.standard.EmailValidator;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.services.UserProfileService;
import com.kush.lib.profile.template.ProfileTemplate;
import com.kush.lib.profile.template.ProfileTemplateBuilder;
import com.kush.lib.service.remoting.auth.User;
import com.kush.messaging.content.TextContent;
import com.kush.messaging.message.Message;
import com.kush.messaging.persistors.DefaultMessagePersistor;
import com.kush.messaging.persistors.MessagePersistor;
import com.kush.messaging.services.MessagingService;
import com.kush.service.BaseServiceTest;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

public class TripperE2E extends BaseServiceTest {

    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_PHONE = "phone";

    private static final String[] EMAILS_IN_CONTACTS = {
            "seconduser@domain.com",
            "thirduser@domain.com"
    };

    private TripperPlanningService tripperPlanningService;
    private TripperProfileService tripperProfileService;
    private TripperMessagingService tripperMessagingService;

    @Before
    public void setup() throws Exception {
        setupUserGroupService();
        setupTripPlannerPersistor();
        tripperPlanningService = registerService(TripperPlanningService.class);

        setupUserProfileService();
        tripperProfileService = registerService(TripperProfileService.class);

        setupMessagingService();
        tripperMessagingService = registerService(TripperMessagingService.class);
    }

    @Test
    public void e2e() throws Exception {
        User firstUser = user(0);
        User secondUser = user(1);
        User thirdUser = user(2);

        updateNameAndEmail(firstUser, "First User", "firstuser@domain.com");
        updateNameAndEmail(secondUser, "Second User", "seconduser@domain.com");
        updateNameAndEmail(thirdUser, "Third User", "thirduser@domain.com");

        runAuthenticatedOperation(firstUser, () -> {
            TripPlan tripPlan = createTripPlan();

            Map<String, Set<Object>> userFilter = ImmutableMap.of(FIELD_EMAIL,
                    new HashSet<>(asList(EMAILS_IN_CONTACTS)));
            addTripMembers(tripPlan, userFilter);
        });

        runAuthenticatedOperation(secondUser, () -> {
            tripperMessagingService.registerMessageHandler((msg) -> {
                System.out.println("Second User got message " + msg);
            });
        });

        runAuthenticatedOperation(thirdUser, () -> {
            List<TripPlan> tripPlans = tripperPlanningService.getTripPlans();
            tripperMessagingService.sendMessage(tripPlans.get(0).getId(), new TextContent("Test Message"));
        });

        runAuthenticatedOperation(firstUser, () -> {
            List<TripPlan> tripPlans = tripperPlanningService.getTripPlans();
            List<Message> messages = tripperMessagingService.getMessages(tripPlans.get(0).getId());
            System.out.println("First User got messages " + messages);
        });

        TimeUnit.SECONDS.sleep(1);
    }

    private void addTripMembers(TripPlan tripPlan, Map<String, Set<Object>> userFilter)
            throws PersistorOperationFailedException, ValidationFailedException {
        List<User> foundUsers = tripperProfileService.findMatchingUsers(userFilter);
        Set<Identifier> userIdsToAdd = foundUsers.stream().map(u -> u.getId()).collect(toSet());
        tripperPlanningService.addMembersToTrip(tripPlan.getId(), userIdsToAdd);
    }

    private TripPlan createTripPlan() throws PersistorOperationFailedException {
        return tripperPlanningService.createTripPlan("First Trip");
    }

    private void updateNameAndEmail(User user, String name, String email) throws Exception {
        runAuthenticatedOperation(user, () -> {
            tripperProfileService.updateProfileField(FIELD_NAME, name);
            tripperProfileService.updateProfileField(FIELD_EMAIL, email);
        });
    }

    private void setupProfilePersistor() {
        Persistor<Profile> delegate = InMemoryPersistor.forType(Profile.class);
        addToContext(ProfilePersistor.class, new DefaultProfilePersistor(delegate));
    }

    private void setupProfileTemplate() {
        addToContext(ProfileTemplate.class, createProfileTemplate());
    }

    private ProfileTemplate createProfileTemplate() {
        Field nameField = Fields.createTextFieldBuilder(FIELD_NAME)
            .withDisplayName("Name")
            .build();
        Field emailField = Fields.createTextFieldBuilder(FIELD_EMAIL)
            .addValidator(new EmailValidator())
            .withDisplayName("Email Id")
            .withNoRepeatitionAllowed()
            .build();
        Field phoneField = Fields.createTextFieldBuilder(FIELD_PHONE)
            .withNoRepeatitionAllowed()
            .build();
        return ProfileTemplateBuilder.create()
            .withField(nameField)
            .withField(emailField)
            .withField(phoneField)
            .build();
    }

    private void setupTripPlannerPersistor() {
        Persistor<TripPlan> planPers = InMemoryPersistor.forType(TripPlan.class);
        TripPlanPersistor tripPlanPersistor = new DefaultTripPlanPersistor(planPers);
        addToContext(TripPlanPersistor.class, tripPlanPersistor);
    }

    private void setupUserProfileService() throws Exception {
        setupProfilePersistor();
        setupProfileTemplate();
        registerService(UserProfileService.class);
    }

    private void setupUserGroupService() throws Exception {
        Persistor<Group> groupPers = InMemoryPersistor.forType(Group.class);
        Persistor<GroupMembership> memPers = InMemoryPersistor.forType(GroupMembership.class);
        GroupPersistor groupPersistor = new DefaultGroupPersistor(groupPers, memPers);
        addToContext(GroupPersistor.class, groupPersistor);
        registerService(UserGroupService.class);
    }

    private void setupMessagingService() throws Exception {
        MessagePersistor messagePersistor = new DefaultMessagePersistor(InMemoryPersistor.forType(Message.class));
        addToContext(MessagePersistor.class, messagePersistor);
        registerService(MessagingService.class);
    }
}
