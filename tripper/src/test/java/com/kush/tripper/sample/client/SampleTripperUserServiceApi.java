package com.kush.tripper.sample.client;

import com.kush.lib.service.remoting.api.ServiceApi;

public interface SampleTripperUserServiceApi extends ServiceApi {

    void registerUser(String username, String password);

    void login(String username, String password);

    void logout();
}
