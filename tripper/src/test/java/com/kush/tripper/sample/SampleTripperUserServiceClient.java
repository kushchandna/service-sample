package com.kush.tripper.sample;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceFailedException;
import com.kush.lib.service.client.api.ServiceTask;

public class SampleTripperUserServiceClient extends ServiceClient {

    public SampleTripperUserServiceClient(Executor executor) {
        super(executor);
    }

    public Response<Void> registerUser(String username, String password) {
        return invoke(new ServiceTask<Void>() {

            @Override
            public Void execute() throws ServiceFailedException {
                return null;
            }
        });
    }

    public Response<Void> login(String username, String password) {
        return invoke(new ServiceTask<Void>() {

            @Override
            public Void execute() throws ServiceFailedException {
                return null;
            }
        });
    }

    public Response<Void> logout() {
        return invoke(new ServiceTask<Void>() {

            @Override
            public Void execute() throws ServiceFailedException {
                return null;
            }
        });
    }
}
