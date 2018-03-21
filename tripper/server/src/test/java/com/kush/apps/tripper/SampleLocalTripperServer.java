package com.kush.apps.tripper;

import com.kush.apps.tripper.api.Trip;
import com.kush.apps.tripper.api.TripPlace;
import com.kush.apps.tripper.persistors.DefaultTripPersistor;
import com.kush.apps.tripper.persistors.TripPersistor;
import com.kush.apps.tripper.services.TripPlannerService;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.lib.location.services.PlaceService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.service.server.local.LocalApplicationServer;
import com.kush.lib.userprofile.DefaultUserProfilePersistor;
import com.kush.lib.userprofile.UserProfile;
import com.kush.lib.userprofile.UserProfilePersistor;
import com.kush.lib.userprofile.UserProfileService;
import com.kush.utils.id.Identifier;
import com.kush.utils.id.SequentialIdGenerator;

public class SampleLocalTripperServer {

    public void start() {
        ApplicationServer server = new LocalApplicationServer();
        registerServices(server);
        Context context = createContext();
        startServer(server, context);
    }

    private void startServer(ApplicationServer server, Context context) {
        try {
            server.start(context);
        } catch (StartupFailedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private Context createContext() {
        Persistor<Trip> tripPersistor = new InMemoryTripPersistor();
        Persistor<UserProfile> userProfilePersistor = new InMemoryUserProfilePersistor();
        Persistor<UserCredential> userCredentialPersistor = new InMemoryUserCredentialPersistor();
        Persistor<TripPlace> tripPlacePersistor = new InMemoryTripPlacePersistor();
        return ContextBuilder.create()
            .withInstance(PlaceFinder.class, new DummyPlaceFinder())
            .withInstance(TripPersistor.class, new DefaultTripPersistor(tripPersistor, tripPlacePersistor))
            .withInstance(UserProfilePersistor.class, new DefaultUserProfilePersistor(userProfilePersistor))
            .withPersistor(UserCredential.class, userCredentialPersistor)
            .build();
    }

    private void registerServices(ApplicationServer server) {
        server.registerService(TripPlannerService.class);
        server.registerService(UserProfileService.class);
        server.registerService(PlaceService.class);
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

    private static final class InMemoryUserProfilePersistor extends InMemoryPersistor<UserProfile> {

        private InMemoryUserProfilePersistor() {
            super(new SequentialIdGenerator());
        }

        @Override
        protected UserProfile createPersistableObject(Identifier id, UserProfile reference) {
            return new UserProfile(id, reference.getAllFields());
        }
    }

    private static final class InMemoryTripPlacePersistor extends InMemoryPersistor<TripPlace> {

        private InMemoryTripPlacePersistor() {
            super(new SequentialIdGenerator());
        }

        @Override
        protected TripPlace createPersistableObject(Identifier id, TripPlace reference) {
            return new TripPlace(id, reference.getTrip(), reference.getPlace());
        }
    }
}
