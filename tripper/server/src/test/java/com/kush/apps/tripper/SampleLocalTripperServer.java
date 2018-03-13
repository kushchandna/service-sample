package com.kush.apps.tripper;

import com.kush.apps.tripper.api.Trip;
import com.kush.apps.tripper.persistors.DefaultTripPersistor;
import com.kush.apps.tripper.persistors.TripPersistor;
import com.kush.apps.tripper.services.TripPlannerService;
import com.kush.lib.persistence.api.Persistor;
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
        ApplicationServer server = new LocalApplicationServer() {

            @Override
            protected void preStartup(Context context) {
                super.preStartup(context);
            }
        };
        server.registerService(TripPlannerService.class);

        Persistor<Trip> persistor = new InMemoryTripPersistor();
        TripPersistor tripPersistor = new DefaultTripPersistor(persistor);
        Context context = ContextBuilder.create()
            .withPersistor(UserCredential.class, new InMemoryUserCredentialPersistor())
            .withInstance(TripPersistor.class, tripPersistor)
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

    private static final class InMemoryTripPersistor extends InMemoryPersistor<Trip> {

        private InMemoryTripPersistor() {
            super(new SequentialIdGenerator());
        }

        @Override
        protected Trip createPersistableObject(Identifier id, Trip reference) {
            return new Trip(id, reference.getCreatedBy(), reference.getTripName());
        }
    }
}
