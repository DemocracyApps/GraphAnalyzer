package com.democracyapps.cnp.graphanalyzer.tasks;

public class RunningTask {
    Task task = null;
    Thread thread = null;

    public RunningTask (Task pTask, Thread pThread) {
        task = pTask;
        thread = pThread;
    }

    public Task getTask() {
        return task;
    }

    public Thread getThread() {
        return thread;
    }
}
