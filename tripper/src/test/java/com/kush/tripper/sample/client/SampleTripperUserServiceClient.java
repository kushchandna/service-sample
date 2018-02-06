package com.kush.tripper.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.utils.async.Response;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.Request;

public class SampleTripperUserServiceClient extends ServiceClient<SampleTripperUserServiceApi> {

    public Response<Void> registerUser(String username, String password) {
        return invoke(new Request<Void>() {

            @Override
            public Void process() throws RequestFailedException {
                getService().registerUser(username, password);
                return null;
            }
        });
    }

    public Response<Void> login(String username, String password) {
        return invoke(new Request<Void>() {

            @Override
            public Void process() throws RequestFailedException {
                getService().login(username, password);
                return null;
            }
        });
    }

    public Response<Void> logout() {
        return invoke(new Request<Void>() {

            @Override
            public Void process() throws RequestFailedException {
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
