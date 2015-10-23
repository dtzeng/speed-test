package com.dtzeng;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Derek on 9/25/2015.
 */
public class ClientThread implements Runnable {
    private HostPort hp;
    private ThreadProgressTracker tracker;
    private long totalUploadTime;
    private long totalDownloadTime;
    private byte[] b;

    public ClientThread(HostPort hp, ThreadProgressTracker tracker) {
        this.hp = hp;
        this.tracker = tracker;
        this.totalUploadTime = 0;
        this.totalDownloadTime = 0;
        b = new byte[ClientConstants.PACKET_SIZE_KILOBYTES];
    }

    public long getTotalUploadTime() {
        return totalUploadTime;
    }

    public long getTotalDownloadTime() {
        return totalDownloadTime;
    }

    @Override
    public void run() {
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            try {
                socket = new Socket(hp.getHost(), hp.getPort());
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (Exception e) {
                System.out.println(e.toString());
                return;
            }


            for (int x = 0; x < ClientConstants.NUM_PROGRESS; x++) {
                try {
                    new Random().nextBytes(b);
                    oos.writeInt(1);
                    long upload_before = System.currentTimeMillis();
                    for (int packet = 0; packet < 1024; packet++) {
                        oos.write(b, 0, ClientConstants.PACKET_SIZE_KILOBYTES);
                        oos.flush();
                    }
                    long upload_after = System.currentTimeMillis();

                    if (ois.readInt() != 2) {
                        throw new Exception("Server error.");
                    }

                    long download_before = System.currentTimeMillis();
                    for (int packet = 0; packet < 1024; packet++) {
                        ois.read(b, 0, ClientConstants.PACKET_SIZE_KILOBYTES);
                    }
                    long download_after = System.currentTimeMillis();


                    totalUploadTime += (upload_after - upload_before);
                    totalDownloadTime += (download_after - download_before);

                    tracker.notifyDone();

                    while (tracker.getThreadsDone() != ((x + 1) * ClientConstants.NUM_THREADS));

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        } finally {
            try {
                if (oos != null)
                    oos.close();
                if (ois != null)
                    ois.close();
                if (socket != null && !socket.isClosed())
                    socket.close();
            } catch (Exception e) {
                // ignore
            }
        }

    }
}
