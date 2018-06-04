package com.kush.apps.sample.services;

import com.kush.apps.sample.app.SampleHelloTextProvider;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;

@Service
public class SampleHelloService extends BaseService {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(SampleHelloService.class);

    @ServiceMethod
    public String sayHello(String name) {
        String text = sayHelloInternal(name);
        LOGGER.info("[Server] returning result for sayHello: %s", text);
        return text;
    }

    @AuthenticationRequired
    @ServiceMethod
    public String sayHelloToMe() {
        User currentUser = getCurrentUser();
        String name = fetchName(currentUser);
        String text = sayHelloInternal(name);
        LOGGER.info("[Server] returning result for sayHelloToMe: %s", text);
        return text;
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(SampleHelloTextProvider.class);
    }

    private String sayHelloInternal(String name) {
        SampleHelloTextProvider helloTextProvider = getInstance(SampleHelloTextProvider.class);
        return helloTextProvider.getHelloText() + " " + name;
    }

    private String fetchName(User user) {
        return user.getId().toString();
    }
}
