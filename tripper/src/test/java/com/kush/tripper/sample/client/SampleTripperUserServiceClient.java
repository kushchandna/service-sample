package com.kush.tripper.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.utils.async.Response;

public class SampleTripperUserServiceClient extends ServiceClient {

    public SampleTripperUserServiceClient() {
        super("Sample Tripper User Service");
    }

    public Response<Void> registerUser(String username, String password) {
        return invoke("registerUser", username, password);
    }

    public Response<Void> login(String username, String password) {
        return invoke("login", username, password);
    }

    public Response<Void> logout() {
        return invoke("logout");
    }
}
