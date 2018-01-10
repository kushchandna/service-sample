package com.kush.tripper.sample.server;

import com.kush.lib.service.server.api.ApplicationServer;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ContextBuilder;

public class SampleTripperServer {

    public static void main(String[] args) {
        Context context = ContextBuilder.create()
            .build();
        ApplicationServer server = new ApplicationServer(context);
        server.registerService(SampleTripperUserService.class);
        server.registerService(SampleTripperApplicationService.class);
        server.start();
    }
}
