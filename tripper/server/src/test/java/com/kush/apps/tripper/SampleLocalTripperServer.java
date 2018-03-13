package com.kush.apps.tripper;

import com.kush.apps.tripper.services.TripPlannerService;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.service.server.local.LocalApplicationServer;
import com.kush.utils.id.Identifier;
import com.kush.utils.id.SequentialIdGenerator;

public class SampleLocalTripperServer {

    public void start() {
        ApplicationServer server = new LocalApplicationServer();
        server.registerService(TripPlannerService.class);
        Context context = ContextBuilder.create()
            .withPersistor(UserCredential.class, new InMemoryUserCredentialPersistor())
            .build();
        try {
            server.start(context);
        } catch (StartupFailedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static final class InMemoryUserCredentialPersistor extends InMemoryPersistor<UserCredential> {

        private InMemoryUserCredentialPersistor() {
            super(new SequentialIdGenerator());
        }

        @Override
        protected UserCredential createPersistableObject(Identifier id, UserCredential reference) {
            return new UserCredential(id, reference.getUser(), reference.getCredential());
        }
    }
}