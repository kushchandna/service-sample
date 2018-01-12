package com.kush.tripper.sample.api;

import com.kush.lib.service.remoting.ServiceApi;

public interface SampleTripperUserServiceApi extends ServiceApi {

    void registerUser(String username, String password);

    void login(String username, String password);

    void logout();

}