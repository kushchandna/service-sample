package com.kush.apps.sample.client.launcher;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.apps.sample.client.services.SampleHelloServiceClient;
import com.kush.serviceclient.ApplicationClient;
import com.kush.serviceclient.ServiceClientActivationFailedException;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.socket.SocketBasedResolutionConnectionFactory;

public class SampleClientLauncher {

    private static final String HOST = "localhost";
    private static final int PORT = 3789;

    public void launch() throws ServiceClientActivationFailedException {

        ResolutionConnectionFactory connectionFactory = new SocketBasedResolutionConnectionFactory(HOST, PORT);
        ApplicationClient client = new ApplicationClient(connectionFactory);
        client.start();
        Executor executor = Executors.newSingleThreadExecutor();
        client.activateServiceClient(SampleHelloServiceClient.class, executor);
        client.activateLoginServiceClient(executor);

    }
}
