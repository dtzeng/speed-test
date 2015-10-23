package com.dtzeng;

/**
 * Created by Derek on 9/25/2015.
 */
public class ThreadProgressTracker {
    private volatile int numThreadsDone;

    public ThreadProgressTracker() {
        this.numThreadsDone = 0;
    }

    public synchronized int getThreadsDone() {
        return numThreadsDone;
    }

    public synchronized int notifyDone() {
        return ++numThreadsDone;
    }
}
