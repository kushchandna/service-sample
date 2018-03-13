package com.kush.apps.tripper.client;

import java.util.List;

import com.kush.apps.tripper.api.Trip;
import com.kush.apps.tripper.serviceclients.TripPlannerServiceClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.client.api.session.LoginServiceClient;
import com.kush.lib.service.remoting.auth.Credential;

public class SampleTripperApplication {

    private final ServiceClientProvider serviceClientProvider;

    public SampleTripperApplication(ServiceClientProvider serviceClientProvider) {
        this.serviceClientProvider = serviceClientProvider;
    }

    public void register(Credential credential) throws Exception {
        LoginServiceClient client = serviceClientProvider.getServiceClient(LoginServiceClient.class);
        client.register(credential).waitForResult();
    }

    public void login(Credential credential) throws Exception {
        LoginServiceClient client = serviceClientProvider.getServiceClient(LoginServiceClient.class);
        client.login(credential).waitForResult();
    }

    public void logout() throws Exception {
        LoginServiceClient client = serviceClientProvider.getServiceClient(LoginServiceClient.class);
        client.logout().waitForResult();
    }

    public Trip createTrip(String tripName) throws Exception {
        TripPlannerServiceClient client = serviceClientProvider.getServiceClient(TripPlannerServiceClient.class);
        return client.createTrip(tripName).getResult();
    }

    public List<Trip> getCreatedTrips() {
        return null;
    }
}
