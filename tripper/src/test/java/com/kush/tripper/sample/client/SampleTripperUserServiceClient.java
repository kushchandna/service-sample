package com.kush.tripper.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.utils.async.Response;
import com.kush.utils.async.ServiceFailedException;
import com.kush.utils.async.ServiceTask;

public class SampleTripperUserServiceClient extends ServiceClient<SampleTripperUserServiceApi> {

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

    @Override
    protected Class<SampleTripperUserServiceApi> getServiceApiClass() {
        return SampleTripperUserServiceApi.class;
    }
}
