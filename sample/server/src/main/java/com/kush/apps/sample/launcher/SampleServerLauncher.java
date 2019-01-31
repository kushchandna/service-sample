package com.kush.apps.sample.launcher;

import static com.kush.lib.persistence.helpers.InMemoryPersistor.forType;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.apps.sample.app.SampleHelloTextProvider;
import com.kush.apps.sample.services.SampleHelloService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.service.ApplicationServer;
import com.kush.service.Context;
import com.kush.service.ContextBuilder;
import com.kush.service.auth.credentials.DefaultUserCredentialPersistor;
import com.kush.service.auth.credentials.UserCredential;
import com.kush.service.auth.credentials.UserCredentialPersistor;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.StartupFailedException;
import com.kush.utils.remoting.server.socket.SocketBasedResolutionRequestsProcessor;
import com.kush.utils.signaling.RemoteSignalSpace;
import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalEmitters;
import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.client.SignalHandlerRegistrationRequest;

public class SampleServerLauncher {

    private static final int PORT = 3789;

    public static void main(String[] args) throws StartupFailedException {

        Executor executor = Executors.newFixedThreadPool(5);
        ResolutionRequestsReceiver serviceRequestReceiver = new SocketBasedResolutionRequestsProcessor(executor, PORT);

        SignalEmitter signalEmitter = SignalEmitters.newAsyncEmitter(executor, executor);
        RemoteSignalSpace signalSpace = new RemoteSignalSpace(signalEmitter);
        serviceRequestReceiver.addResolver(SignalHandlerRegistrationRequest.class, signalSpace);

        ApplicationServer server = new ApplicationServer(serviceRequestReceiver);
        server.registerService(SampleHelloService.class);
        SampleHelloTextProvider greetingProvider = new SampleHelloTextProvider();
        Persistor<UserCredential> delegate = forType(UserCredential.class);
        Context context = ContextBuilder.create()
            .withInstance(SampleHelloTextProvider.class, greetingProvider)
            .withInstance(UserCredentialPersistor.class, new DefaultUserCredentialPersistor(delegate))
            .withInstance(SignalSpace.class, signalSpace)
            .build();
        server.start(context);

    }
}
