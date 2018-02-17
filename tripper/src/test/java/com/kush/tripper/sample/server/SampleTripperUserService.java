package com.kush.tripper.sample.server;

import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.annotations.Service;
import com.kush.lib.service.server.api.annotations.ServiceMethod;
import com.kush.logger.Logger;
import com.kush.logger.LoggerFactory;

@Service(name = "Sample Tripper User Service")
public class SampleTripperUserService extends BaseService {

    private static final Logger LOGGER = LoggerFactory.INSTANCE.getLogger(SampleTripperUserService.class);

    @ServiceMethod(name = "registerUser")
    public void registerUser(String username, String password) {
        LOGGER.info("User %s registered", username);
    }

    @ServiceMethod(name = "login")
    public void login(String username, String password) {
        LOGGER.info("User %s logged in", username);
    }

    @ServiceMethod(name = "logout")
    public void logout() {
        LOGGER.info("User logged out");
    }
}
