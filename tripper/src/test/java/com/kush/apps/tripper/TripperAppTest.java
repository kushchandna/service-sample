package com.kush.apps.tripper;

import static com.google.common.collect.Sets.newHashSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.group.entities.DefaultGroupPersistor;
import com.kush.lib.group.entities.Group;
import com.kush.lib.group.entities.GroupMembership;
import com.kush.lib.group.persistors.GroupPersistor;
import com.kush.lib.group.service.UserGroupService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.profile.entities.DefaultProfilePersistor;
import com.kush.lib.profile.entities.Profile;
import com.kush.lib.profile.fields.Field;
import com.kush.lib.profile.fields.Fields;
import com.kush.lib.profile.persistors.ProfilePersistor;
import com.kush.lib.profile.services.UserProfileService;
import com.kush.lib.profile.template.ProfileTemplate;
import com.kush.lib.profile.template.ProfileTemplateBuilder;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseServiceTest;

public class TripperAppTest extends BaseServiceTest {

    private static final String FIELD_PHONE = "Phone";

    private UserProfileService profileService;
    private UserGroupService userGroupService;

    @Before
    public void beforeEachTest() throws Exception {
        setupProfileContext();
        profileService = registerService(UserProfileService.class);

        setupGroupContext();
        userGroupService = registerService(UserGroupService.class);

    }

    @Test
    public void testName() throws Exception {
        registerOtherUsers();

        runAuthenticatedOperation(() -> {
            profileService.updateProfileField(FIELD_PHONE, "000000000");
        });

        List<User> selectedUsers = fetchUsersWithPhones("111111111", "222222222", "333333333");
        selectedUsers.toString();

        runAuthenticatedOperation(() -> {
            userGroupService.toString();
        });
    }

    private List<User> fetchUsersWithPhones(Object... phoneNumbers) throws Exception {
        List<User> selectedUsers = new ArrayList<>();
        runAuthenticatedOperation(() -> {
            Map<String, Set<Object>> fieldVsValues = new HashMap<>();
            fieldVsValues.put(FIELD_PHONE, newHashSet(phoneNumbers));
            List<User> matchingUsers = profileService.findMatchingUsers(fieldVsValues);
            selectedUsers.addAll(matchingUsers);
        });
        return selectedUsers;
    }

    private void registerOtherUsers() throws Exception {
        runAuthenticatedOperation(user(1), () -> {
            profileService.updateProfileField(FIELD_PHONE, "111111111");
        });
        runAuthenticatedOperation(user(2), () -> {
            profileService.updateProfileField(FIELD_PHONE, "222222222");
        });
        runAuthenticatedOperation(user(3), () -> {
            profileService.updateProfileField(FIELD_PHONE, "333333333");
        });
        runAuthenticatedOperation(user(4), () -> {
            profileService.updateProfileField(FIELD_PHONE, "444444444");
        });
    }

    private void setupProfilePersistor() {
        Persistor<Profile> delegate = InMemoryPersistor.forType(Profile.class);
        addToContext(ProfilePersistor.class, new DefaultProfilePersistor(delegate));
    }

    private void setupProfileService() {
        addToContext(ProfileTemplate.class, createProfileTemplate());
    }

    private ProfileTemplate createProfileTemplate() {
        Field phoneField = Fields.createTextFieldBuilder(FIELD_PHONE)
            .withNoRepeatitionAllowed()
            .build();
        return ProfileTemplateBuilder.create()
            .withField(phoneField)
            .build();
    }

    private void setupProfileContext() {
        setupProfilePersistor();
        setupProfileService();
    }

    private void setupGroupContext() {
        Persistor<Group> delegateGroupPersistor = InMemoryPersistor.forType(Group.class);
        Persistor<GroupMembership> delegateGroupMembershipPersistor = InMemoryPersistor.forType(GroupMembership.class);
        addToContext(GroupPersistor.class, new DefaultGroupPersistor(delegateGroupPersistor, delegateGroupMembershipPersistor));
    }
}
