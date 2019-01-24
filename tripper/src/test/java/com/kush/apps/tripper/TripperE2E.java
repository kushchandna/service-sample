package com.kush.apps.tripper;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.persistors.DefaultTripPlanPersistor;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.apps.tripper.services.TripPlannerService;
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

    private UserProfileService userProfileService;
    private TripPlannerService tripPlannerService;

    @Before
    public void setup() throws Exception {
        setupProfilePersistor();
        setupProfileTemplate();
        userProfileService = registerService(UserProfileService.class);

        setupTripPlannerPersistor();
        tripPlannerService = registerService(TripPlannerService.class);
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
    }

    private void addTripMembers(TripPlan tripPlan, Map<String, Set<Object>> userFilter)
            throws PersistorOperationFailedException, ValidationFailedException {
        List<User> foundUsers = userProfileService.findMatchingUsers(userFilter);
        Set<Identifier> userIdsToAdd = foundUsers.stream().map(u -> u.getId()).collect(toSet());
        tripPlannerService.addMembersToTrip(tripPlan.getId(), userIdsToAdd);
    }

    private TripPlan createTripPlan() throws PersistorOperationFailedException {
        return tripPlannerService.createTripPlan();
    }

    private void updateNameAndEmail(User user, String name, String email) throws Exception {
        runAuthenticatedOperation(user, () -> {
            userProfileService.updateProfileField(FIELD_NAME, name);
            userProfileService.updateProfileField(FIELD_EMAIL, email);
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
}
