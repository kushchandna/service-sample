package com.kush.tripper.sample.client;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceInvoker;

public class SampleTripperUserServiceClient extends ServiceClient {

    public SampleTripperUserServiceClient(Executor executor, ServiceInvoker serviceInvoker) {
        super(executor, serviceInvoker, "Sample Tripper User Service");
    }

    public Response<Void> registerUser(String username, String password) {
        return invoke(Void.class, "registerUser", username, password);
    }

    public Response<Void> login(String username, String password) {
        return invoke(Void.class, "login", username, password);
    }

    public Response<Void> logout() {
        return invoke(Void.class, "logout");
    }
}
