package com.kush.apps.tripper.launcher;

import com.kush.apps.tripper.SampleLocalTripperServer;
import com.kush.apps.tripper.client.SampleTripperClient;

public class LocalTripperLauncher {

    public static void main(String[] args) {
        SampleLocalTripperServer server = new SampleLocalTripperServer();
        server.start();

        SampleTripperClient client = new SampleTripperClient();
        client.start();
    }
}
