package com.kush.tripper.sample.api.remoting;

import com.kush.lib.service.server.api.BaseService;
import com.kush.tripper.sample.api.SampleTripperUserServiceApi;

public class DelegatingSampleTripperUserService extends BaseService implements SampleTripperUserServiceApi {

    private final SampleTripperUserServiceApi delegate;

    public DelegatingSampleTripperUserService(SampleTripperUserServiceApi delegate) {
        this.delegate = delegate;
    }

    @Override
    public void registerUser(String username, String password) {
        delegate.registerUser(username, password);
    }

    @Override
    public void login(String username, String password) {
        delegate.login(username, password);
    }

    @Override
    public void logout() {
        delegate.logout();
    }
}
