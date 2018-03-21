package com.kush.apps.tripper.launcher;

import java.util.HashMap;
import java.util.Map;

import org.junit.rules.ExternalResource;

import com.kush.lib.service.client.api.session.LoginServiceClient;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;

public class FakeSessionManager extends ExternalResource {

    private final Map<User, Credential> userVsCredential = new HashMap<>();

    private final String prefix;
    private final int count;

    private LoginServiceClient loginServiceClient;

    public FakeSessionManager(String prefix, int count) {
        this.prefix = prefix;
        this.count = count;
    }

    @Override
    protected void before() throws Throwable {
        userVsCredential.clear();
    }

    @Override
    protected void after() {
        try {
            endSession();
        } catch (Exception e) {
            // eat exception
        }
        userVsCredential.clear();
        loginServiceClient = null;
    }

    public void initialize(LoginServiceClient loginServiceClient) throws Exception {
        this.loginServiceClient = loginServiceClient;
        for (int i = 1; i <= count; i++) {
            Credential credential = new PasswordBasedCredential(prefix + "usr" + i, (prefix + "pwd" + i).toCharArray());
            User user = loginServiceClient.register(credential).getResult();
            userVsCredential.put(user, credential);
        }
    }

    public User[] getUsers() {
        return userVsCredential.keySet().toArray(new User[userVsCredential.size()]);
    }

    public void beginTestSession() throws Exception {
        beginSession(getUsers()[0]);
    }

    public void beginSession(User user) throws Exception {
        loginServiceClient.login(userVsCredential.get(user)).waitForResult();
    }

    public void endSession() throws Exception {
        loginServiceClient.logout().waitForResult();
    }
}
