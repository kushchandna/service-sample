package com.kush.tripper.sample.server;

import com.kush.lib.service.server.api.BaseService;
import com.kush.logger.Logger;
import com.kush.logger.LoggerFactory;
import com.kush.tripper.sample.api.SampleTripperUserServiceApi;

public class SampleTripperUserService extends BaseService implements SampleTripperUserServiceApi {

    private static final Logger LOGGER = LoggerFactory.INSTANCE.getLogger(SampleTripperUserService.class);

    @Override
    public void registerUser(String username, String password) {
        LOGGER.info("User %s registered", username);
    }

    @Override
    public void login(String username, String password) {
        LOGGER.info("User %s logged in", username);
    }

    @Override
    public void logout() {
        LOGGER.info("User logged out");
    }
}
