package com.dtzeng;

/**
 * Created by Derek on 9/25/2015.
 */
public class HostPort {

    /**
     * Hostname of node
     */
    private String host;

    /**
     * Port number of node
     */
    private int port;

    public HostPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
