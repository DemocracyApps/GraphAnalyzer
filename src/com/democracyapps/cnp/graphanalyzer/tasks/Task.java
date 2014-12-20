package com.democracyapps.cnp.graphanalyzer.tasks;

import org.json.simple.JSONObject;

public class Task implements Runnable {
    JSONObject configuration        = null;
    String name                     = null;
    Trigger trigger                 = Trigger.NONE;


    public Task (JSONObject taskConfiguration) {
        configuration = taskConfiguration;
        name = (String) taskConfiguration.get("name");
        trigger = Trigger.valueOf(((String) configuration.get("trigger")).toUpperCase());
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Task Thread " + this.name);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hello from thread " + Thread.currentThread().getName());
    }

    public String getName() {
        return name;
    }
}
