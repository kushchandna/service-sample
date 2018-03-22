package com.kush.apps.tripper.launcher;

import com.kush.apps.tripper.SampleLocalTripperServer;

public class TripperServerLauncher {

    public static void main(String[] args) {
        SampleLocalTripperServer server = new SampleLocalTripperServer();
        server.start();
    }
}
