package com.dtzeng;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Derek on 9/25/2015.
 */
public class SpeedTestDispatcher {
    private int port;

    public SpeedTestDispatcher(int port) {
        this.port = port;
    }

    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println(e.toString());
            System.exit(-1);
        }

        System.out.println("Started server on port " + Integer.toString(port));

        Socket clientSocket = null;
        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println(e.toString());
                break;
            }
            SpeedTestServerWorker worker = new SpeedTestServerWorker(clientSocket);
            new Thread(worker).start();
        }
    }
}
