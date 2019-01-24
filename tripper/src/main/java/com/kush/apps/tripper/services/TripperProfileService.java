package com.kush.apps.tripper.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.profile.services.UserProfileService;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;

@Service
public class TripperProfileService extends BaseService {

    @ServiceMethod
    @AuthenticationRequired
    public void updateProfileField(String fieldName, Object value)
            throws NoSuchFieldException, ValidationFailedException, PersistorOperationFailedException {
        UserProfileService userProfileService = getUserProfileService();
        userProfileService.updateProfileField(fieldName, value);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<User> findMatchingUsers(Map<String, Set<Object>> fieldVsValues) throws PersistorOperationFailedException {
        UserProfileService userProfileService = getUserProfileService();
        return userProfileService.findMatchingUsers(fieldVsValues);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(UserProfileService.class);
    }

    private UserProfileService getUserProfileService() {
        return getInstance(UserProfileService.class);
    }
}
