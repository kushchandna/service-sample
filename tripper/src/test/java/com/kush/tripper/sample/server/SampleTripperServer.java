package com.kush.tripper.sample.server;

import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.ServiceInitializationFailedException;

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
