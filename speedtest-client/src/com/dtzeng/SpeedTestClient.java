package com.dtzeng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Derek on 9/25/2015.
 */
public class SpeedTestClient {

    /**
     * Command line argument for IP address.
     */
    private static final String CMDLINE_IP = "--server-ip";

    /**
     * Command line argument for port.
     */
    private static final String CMDLINE_PORT = "--server-port";

    /**
     * Command line usage.
     */
    private static final String CMDLINE_USAGE =
            "Usage:\n" +
            "\t --server-ip <server ip address>\n" +
            "\t --server-port <server port>";

    private static HostPort parseCommandLine(String[] args) {
        if (args.length < 4) {
            return null;
        }

        String host = args[0].equals(CMDLINE_IP) ? args[1] : args[3];
        String portStr = args[0].equals(CMDLINE_PORT) ? args[1] : args[3];
        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            return null;
        }

        return new HostPort(host, port);
    }

    public static void main(String[] args) throws InterruptedException {
        HostPort hp = parseCommandLine(args);
        if (hp == null) {
            System.out.println(CMDLINE_USAGE);
            return;
        }

        /* Instantiate threads */
        Object monitor = new Object();
        ThreadProgressTracker tracker = new ThreadProgressTracker();
        List<ClientThread> threadList = new ArrayList<ClientThread>();
        for (int x = 0; x < ClientConstants.NUM_THREADS; x++) {
            ClientThread thread = new ClientThread(hp, tracker);
            threadList.add(thread);
            new Thread(thread).start();
        }

        while (tracker.getThreadsDone() != (ClientConstants.NUM_PROGRESS * ClientConstants.NUM_THREADS)) {
            TimeUnit.MILLISECONDS.sleep(250);
        }


        long totalUploadTime = 0;
        long totalDownloadTime = 0;
        for (int x = 0; x < ClientConstants.NUM_THREADS; x++) {
            totalUploadTime += threadList.get(x).getTotalUploadTime();
            totalDownloadTime += threadList.get(x).getTotalDownloadTime();
        }

        float uploadSpeed = (float)(ClientConstants.PACKET_SIZE_KILOBYTES * 1024 * 8) /
                             (((float)(totalUploadTime)) / 1000);
        float downloadSpeed = (float)(ClientConstants.PACKET_SIZE_KILOBYTES * 1024 * 8) /
                               (((float)(totalDownloadTime)) / 1000);
        System.out.println("Upload Speed: " + uploadSpeed + " bits/sec");
        System.out.println("Download Speed: " + downloadSpeed + " bits/sec");
    }
}
