package com.kush.tripper.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.utils.async.Response;

public class SampleTripperUserServiceClient extends ServiceClient {

    public Response<Void> registerUser(String username, String password) {
        return invoke("registerUser", Void.class, username, password);
    }

    public Response<Void> login(String username, String password) {
        return invoke("login", Void.class, username, password);
    }

    public Response<Void> logout() {
        return invoke("logout", Void.class);
    }
}
