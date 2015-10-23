package com.dtzeng;

/**
 * Created by Derek on 9/25/2015.
 */
public class SpeedTestServer {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: <port>");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Usage: <port>");
            return;
        }

        SpeedTestDispatcher dispatcher = new SpeedTestDispatcher(port);
        dispatcher.run();
    }
}
