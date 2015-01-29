package com.democracyapps.cnp.graphanalyzer.tasks;

import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;

/**
 * Created by ericjackson on 1/29/15.
 */
abstract public class Task implements Runnable {
    Thread thread                   = null;
    Workspace workspace             = null;

    public Task (Workspace w) {
        workspace = w;
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    public Thread getThread() {
        return thread;
    }

}
