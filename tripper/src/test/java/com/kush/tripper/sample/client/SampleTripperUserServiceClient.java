package com.kush.tripper.sample.client;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceFailedException;
import com.kush.lib.service.client.api.ServiceTask;
import com.kush.tripper.sample.api.SampleTripperUserServiceApi;

public class SampleTripperUserServiceClient extends ServiceClient<SampleTripperUserServiceApi> {

    public SampleTripperUserServiceClient(Executor executor, SampleTripperUserServiceApi serviceApi) {
        super(executor, serviceApi);
    }

    public Response<Void> registerUser(String username, String password) {
        return invoke(new ServiceTask<Void>() {

            @Override
            public Void execute() throws ServiceFailedException {
                getService().registerUser(username, password);
                return null;
            }
        });
    }

    public Response<Void> login(String username, String password) {
        return invoke(new ServiceTask<Void>() {

            @Override
            public Void execute() throws ServiceFailedException {
                getService().login(username, password);
                return null;
            }
        });
    }

    public Response<Void> logout() {
        return invoke(new ServiceTask<Void>() {

            @Override
            public Void execute() throws ServiceFailedException {
                getService().logout();
                return null;
            }
        });
    }
}
