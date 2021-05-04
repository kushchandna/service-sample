package com.kush.apps.sample.launcher;

import static com.kush.lib.persistence.helpers.InMemoryPersister.forType;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.apps.sample.app.SampleHelloTextProvider;
import com.kush.apps.sample.services.SampleHelloService;
import com.kush.lib.persistence.api.Persister;
import com.kush.service.ApplicationServer;
import com.kush.service.Context;
import com.kush.service.ContextBuilder;
import com.kush.service.auth.credentials.DefaultUserCredentialPersister;
import com.kush.service.auth.credentials.UserCredential;
import com.kush.service.auth.credentials.UserCredentialPersister;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.StartupFailedException;
import com.kush.utils.remoting.server.socket.SocketBasedResolutionRequestsProcessor;
import com.kush.utils.signaling.RemoteSignalSpace;
import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.client.SignalHandlerRegistrationRequest;

public class SampleServerLauncher {

    private static final int PORT = 3789;

    public static void main(String[] args) throws StartupFailedException {

        Executor executor = Executors.newFixedThreadPool(5);
        ResolutionRequestsReceiver serviceRequestReceiver = new SocketBasedResolutionRequestsProcessor(executor, PORT);

        RemoteSignalSpace signalSpace = new RemoteSignalSpace();
        serviceRequestReceiver.addResolver(SignalHandlerRegistrationRequest.class, signalSpace);

        ApplicationServer server = new ApplicationServer(serviceRequestReceiver);
        server.registerService(SampleHelloService.class);
        SampleHelloTextProvider greetingProvider = new SampleHelloTextProvider();
        Persister<UserCredential> delegate = forType(UserCredential.class);
        Context context = ContextBuilder.create()
            .withInstance(SampleHelloTextProvider.class, greetingProvider)
            .withInstance(UserCredentialPersister.class, new DefaultUserCredentialPersister(delegate))
            .withInstance(SignalSpace.class, signalSpace)
            .build();
        server.start(context);

    }
}
