package com.democracyapps.cnp.graphanalyzer;

import com.democracyapps.cnp.graphanalyzer.tasks.GraphAnalysisTask;
import com.democracyapps.cnp.graphanalyzer.tasks.RunningTask;
import com.democracyapps.cnp.graphanalyzer.tasks.Task;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;


public class Main {
    private static String       runDirectory = null;
    public static Logger mainLogger = null;
    private static JSONObject   configuration = null;

    public static Logger gaGetLogger () throws IOException {
        if (mainLogger == null) {
            mainLogger = Logger.getLogger("com.democracyapps.cnp.graphanalyzer");
            FileHandler fh = new FileHandler (runDirectory + "/ga_log.txt");
            fh.setFormatter(new SimpleFormatter());
            mainLogger.addHandler(fh);
            mainLogger.setLevel(Level.ALL);
        }
        return mainLogger;
    }

    public static void main(String[] args) {
        Logger mainLogger = null;

        if (args.length < 1) {
            System.err.println("GraphAnalyzer usage:  graphanalyzer run-directory");
            System.exit(1);
        }
        runDirectory = args[0];

        /*
         * Set up logging and read configuration file
         */
        try {
            mainLogger = gaGetLogger();
            mainLogger.info("GraphAnalyzer main - starting up in run directory: " + args[0]);

            configuration = readConfigurationFile(runDirectory + "/ga.json");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        /*
         * Now pull out and run all the tasks in the configuration file.
         *
         * Later we'll actually pay attention to the trigger for these and turn this process into a
         * service that runs tasks as their turns come up, but for initial development there's no point.
         * We'll just run through the array of tasks, kick them all off, and then wait for them to finish.
         */

        Main.mainLogger.info("Running tasks from configuration " + configuration.get("name"));
        JSONArray array = (JSONArray) configuration.get("tasks");

        ArrayList<RunningTask> allTasks = new ArrayList<RunningTask>();

        for (int i=0; i<array.size(); ++i) {
            JSONObject taskConfiguration = (JSONObject) array.get(i);
            String taskType = (String) taskConfiguration.get("type");
            if (taskType == null) {
                mainLogger.severe("No type specified for task " + (String) taskConfiguration.get("name"));
            }
            else {
                try {
                    Task task;
                    if (taskType.equalsIgnoreCase("graphanalysis")) {
                        task = new GraphAnalysisTask(taskConfiguration);
                    }
                    else {
                        task = new Task(taskConfiguration);
                    }

                    Thread t = new Thread(task);
                    t.start();
                    allTasks.add(new RunningTask(task, t));
                }
                catch (Exception e) {
                    mainLogger.severe ("Failed to run task " + (String) taskConfiguration.get("name"));
                }
            }
        }

        while (allTasks.size() > 0) {
            RunningTask rt = allTasks.remove(0);
            Thread thread = rt.getThread();
            while (thread.isAlive()) {
                try {
                    thread.join(1000);
                }
                catch (InterruptedException e) {
                    Main.mainLogger.severe("Unable to wait for thread " + rt.getTask().getName());
                    break;
                }
            }
            Main.mainLogger.info("Done with task " + rt.getTask().getName());
        }
//
//        Thread t = new Thread(new Task("George"));
//        t.start();
//        Thread t2 = new Thread(new Task("Sam"));
//        t2.start();
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        while (t.isAlive()) {
//            System.err.println("Here we go");
//            try {
//                t.join(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        System.err.println("Bye");
    }

        private static JSONObject readConfigurationFile (String filePath) throws IOException, ParseException {
            FileReader fr = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(fr);
            String contents = "";
            String line;
            while ((line = reader.readLine()) != null) {
                contents += line;
            }
            JSONParser parser = new JSONParser();

            JSONObject obj = (JSONObject) parser.parse(contents);
            return obj;
        }
}
