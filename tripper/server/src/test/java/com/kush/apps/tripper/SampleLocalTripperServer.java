package com.kush.apps.tripper;

import static com.kush.lib.persistence.helpers.InMemoryPersistor.forType;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.api.TripPlanMember;
import com.kush.apps.tripper.api.TripPlanPlace;
import com.kush.apps.tripper.persistors.DefaultTripPersistor;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.apps.tripper.services.TripPlannerService;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.lib.location.services.PlaceService;
import com.kush.lib.persistence.api.Persistor;
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
        Persistor<TripPlan> tripPlanPersistor = forType(TripPlan.class);
        Persistor<TripPlanPlace> tripPlanPlacePersistor = forType(TripPlanPlace.class);
        Persistor<TripPlanMember> tripPlanMemberPersistor = forType(TripPlanMember.class);
        Persistor<UserProfile> userProfilePersistor = forType(UserProfile.class);
        Persistor<UserCredential> userCredentialPersistor = forType(UserCredential.class);
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
}
