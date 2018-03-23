package com.kush.apps.tripper;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.api.TripPlanMember;
import com.kush.apps.tripper.api.TripPlanPlace;
import com.kush.apps.tripper.persistors.DefaultTripPersistor;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.apps.tripper.services.TripPlannerService;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.lib.location.services.PlaceService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.userprofile.DefaultUserProfilePersistor;
import com.kush.lib.userprofile.UserProfile;
import com.kush.lib.userprofile.UserProfilePersistor;
import com.kush.lib.userprofile.UserProfileService;
import com.kush.utils.id.Identifier;
import com.kush.utils.id.SequentialIdGenerator;

public class SampleLocalTripperServer {

    private ApplicationServer server;

    public void start(ServiceRequestReceiver requestReceiver) {
        server = new ApplicationServer();
        server.registerServiceRequestReceiver(requestReceiver);
        registerServices(server);
        Context context = createContext();
        startServer(server, context);
    }

    public void stop() {
        try {
            server.stop();
        } catch (ShutdownFailedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
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
        Persistor<TripPlan> tripPlanPersistor = new InMemoryTripPersistor();
        Persistor<UserProfile> userProfilePersistor = new InMemoryUserProfilePersistor();
        Persistor<UserCredential> userCredentialPersistor = new InMemoryUserCredentialPersistor();
        Persistor<TripPlanPlace> tripPlanPlacePersistor = new InMemoryTripPlanPlacePersistor();
        Persistor<TripPlanMember> tripPlanMemberPersistor = new InMemoryTripPlanMemberPersistor();
        return ContextBuilder.create()
            .withInstance(PlaceFinder.class, new DummyPlaceFinder())
            .withInstance(TripPlanPersistor.class,
                    new DefaultTripPersistor(tripPlanPersistor, tripPlanPlacePersistor, tripPlanMemberPersistor))
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

    private static final class InMemoryTripPersistor extends InMemoryPersistor<TripPlan> {

        private InMemoryTripPersistor() {
            super(new SequentialIdGenerator());
        }

        @Override
        protected TripPlan createPersistableObject(Identifier id, TripPlan reference) {
            return new TripPlan(id, reference.getCreatedBy(), reference.getTripPlanName(), reference.getDuration());
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

    private static final class InMemoryTripPlanPlacePersistor extends InMemoryPersistor<TripPlanPlace> {

        private InMemoryTripPlanPlacePersistor() {
            super(new SequentialIdGenerator());
        }

        @Override
        protected TripPlanPlace createPersistableObject(Identifier id, TripPlanPlace reference) {
            return new TripPlanPlace(id, reference.getTripPlan(), reference.getPlace());
        }
    }

    private static final class InMemoryTripPlanMemberPersistor extends InMemoryPersistor<TripPlanMember> {

        private InMemoryTripPlanMemberPersistor() {
            super(new SequentialIdGenerator());
        }

        @Override
        protected TripPlanMember createPersistableObject(Identifier id, TripPlanMember reference) {
            return new TripPlanMember(id, reference.getTripPlan(), reference.getUserId());
        }
    }
}
