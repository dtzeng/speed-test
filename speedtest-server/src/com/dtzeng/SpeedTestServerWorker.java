package com.dtzeng;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Derek on 9/25/2015.
 */
public class SpeedTestServerWorker implements Runnable {
    private Socket clientSocket;
    private byte[] b;

    public SpeedTestServerWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
        b = new byte[com.dtzeng.ServerConstants.PACKET_SIZE_KILOBYTES];
    }

    @Override
    public void run() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            try {
                System.out.println("Received request from " + clientSocket.getInetAddress().toString());

                ois = new ObjectInputStream(clientSocket.getInputStream());
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
            } catch (Exception e) {
                System.out.println(e.toString());
                return;
            }

            for (int x = 0; x < ServerConstants.NUM_PROGRESS; x++) {
                try {
                    if (ois.readInt() != 1) {
                        throw new IOException("Client error.");
                    }

                    for (int packet = 0; packet < 1024; packet++) {
                        ois.read(b, 0, ServerConstants.PACKET_SIZE_KILOBYTES);
                    }

                    new Random().nextBytes(b);
                    oos.writeInt(2);
                    for (int packet = 0; packet < 1024; packet++) {
                        oos.write(b, 0, ServerConstants.PACKET_SIZE_KILOBYTES);
                        oos.flush();
                    }
                } catch (IOException e) {
                    System.out.println(e.toString());
                    return;
                }
            }
        } finally {
            try {
                if (oos != null)
                    oos.close();
                if (ois != null)
                    ois.close();
                if (clientSocket != null && !clientSocket.isClosed())
                    clientSocket.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
