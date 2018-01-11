package com.kush.tripper.sample.server;

import com.kush.lib.service.server.api.BaseService;
import com.kush.logger.Logger;
import com.kush.logger.LoggerFactory;

public class SampleTripperUserService extends BaseService {

    private static final Logger LOGGER = LoggerFactory.INSTANCE.getLogger(SampleTripperUserService.class);

    public void registerUser(String username, String password) {
        LOGGER.info("User %s registered", username);
    }

    public void login(String username, String password) {
        LOGGER.info("User %s logged in", username);
    }

    public void logout() {
        LOGGER.info("User logged out");
    }
}
