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
import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.service.ApplicationServer;
import com.kush.service.Context;
import com.kush.service.ContextBuilder;
import com.kush.service.auth.credentials.DefaultUserCredentialPersistor;
import com.kush.service.auth.credentials.UserCredential;
import com.kush.service.auth.credentials.UserCredentialPersistor;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.StartupFailedException;
import com.kush.utils.remoting.server.socket.SocketBasedResolutionRequestsProcessor;

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
        ResolutionRequestsReceiver<ServiceRequest> requestReceiver = new SocketBasedResolutionRequestsProcessor<>(executor, PORT);
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
