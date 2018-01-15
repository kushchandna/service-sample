package com.kush.tripper.sample.server;

import com.kush.lib.service.server.api.ApplicationServer;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ContextBuilder;
import com.kush.lib.service.server.api.ServiceInitializationFailedException;

public class SampleTripperServer {

    public static void main(String[] args) {
        ApplicationServer server = new ApplicationServer();
        server.registerService(SampleTripperUserService.class);
        server.registerService(SampleTripperApplicationService.class);
        Context context = ContextBuilder.create()
            .build();
        try {
            server.start(context);
        } catch (ServiceInitializationFailedException e) {
            e.printStackTrace();
        }
    }
}
