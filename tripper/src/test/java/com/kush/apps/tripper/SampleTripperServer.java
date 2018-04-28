package com.kush.apps.tripper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.api.TripPlanMember;
import com.kush.apps.tripper.api.TripPlanPlace;
import com.kush.apps.tripper.persistors.DefaultTripPlanPersistor;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.apps.tripper.services.TripPlannerService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.receiver.socket.ServerSocketServiceRequestReceiver;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.authentication.credential.DefaultUserCredentialPersistor;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.service.server.authentication.credential.UserCredentialPersistor;

public class SampleTripperServer {

    private static final int PORT = 8888;

    public static void main(String[] args) {
        try {
            startServer();
        } catch (StartupFailedException e) {
            e.printStackTrace();
            System.err.println("Failed to start server. Reason: " + e.getMessage());
        }
    }

    private static void startServer() throws StartupFailedException {

        ApplicationServer server = new ApplicationServer();

        Executor executor = Executors.newFixedThreadPool(3);
        ServerSocketServiceRequestReceiver requestReceiver = new ServerSocketServiceRequestReceiver(executor, PORT);
        server.registerServiceRequestReceiver(requestReceiver);

        server.registerService(TripPlannerService.class);

        Persistor<TripPlan> inMemTripPlanPersistor = InMemoryPersistor.forType(TripPlan.class);
        Persistor<TripPlanPlace> inMemTripPlanPlacePersistor = InMemoryPersistor.forType(TripPlanPlace.class);
        Persistor<TripPlanMember> inMemTripPlanMemberPersistor = InMemoryPersistor.forType(TripPlanMember.class);
        Persistor<UserCredential> inMemUserCredentialPersistor = InMemoryPersistor.forType(UserCredential.class);
        TripPlanPersistor tripPlanPersistor =
                new DefaultTripPlanPersistor(inMemTripPlanPersistor, inMemTripPlanPlacePersistor, inMemTripPlanMemberPersistor);
        UserCredentialPersistor userCredentialPersistor = new DefaultUserCredentialPersistor(inMemUserCredentialPersistor);
        Context context = ContextBuilder.create()
            .withInstance(TripPlanPersistor.class, tripPlanPersistor)
            .withInstance(UserCredentialPersistor.class, userCredentialPersistor)
            .build();
        server.start(context);
    }
}