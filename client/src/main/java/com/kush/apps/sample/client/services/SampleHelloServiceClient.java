package com.kush.apps.sample.client.services;

import com.kush.commons.async.Response;
import com.kush.serviceclient.ServiceClient;

public class SampleHelloServiceClient extends ServiceClient {

    public SampleHelloServiceClient() {
        super("com.kush.lib.service.sample.server.SampleHelloService");
    }

    public Response<String> sayHello(String name) {
        return invoke("sayHello", name);
    }

    public Response<String> sayHelloToMe() {
        return authInvoke("sayHelloToMe");
    }
}
