package com.democracyapps.cnp.graphanalyzer;

import com.democracyapps.cnp.graphanalyzer.data.CNPDatabaseAccessor;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.JsonUtilities;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;
import com.democracyapps.cnp.graphanalyzer.tasks.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
    static Workspace workspace = null;

    public static void main(String[] args) {
        String runDirectory = null;
        String tasksSource = null;
        Boolean daemon = false, keepRunning = true;

        if (args.length < 1) {
            System.err.println("GraphAnalyzer usage:  graphanalyzer run-directory");
            System.exit(1);
        }
        runDirectory = args[0];

        /*
         * Set up logging, read configuration file, prepare the database connection
         */
        try {
            workspace = new Workspace(runDirectory);
            workspace.logger.info("GraphAnalyzer main - starting up in run directory: " + args[0]);

            workspace.addParameters("main", readConfigurationFile(runDirectory + "/ga.json"));

            tasksSource = workspace.getStringParameter("main.tasksSource");
            daemon = workspace.getBooleanParameter("main.daemon");

            if (workspace.getStringParameter("main.db_connection") != null) {
                workspace.setDatabaseAccessor(new CNPDatabaseAccessor(workspace.getStringParameter("main.db_connection"),
                        workspace.getStringParameter("main.db_user"),
                        workspace.getStringParameter("main.db_password")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        /*
         * Main loop
         */
        while (keepRunning) {
            /*
             * Load in recently updated tasks (or all tasks if non-daemon tasksSource)
             */
            ArrayList<AnalysisTask> newAnalysisTasks = null;

            try {
                if (tasksSource.equalsIgnoreCase("db")) {
                    newAnalysisTasks = workspace.getDatabaseAccessor().loadNewTasks(daemon, workspace);
                } else if (tasksSource.equalsIgnoreCase("file")) {
                    newAnalysisTasks = loadTasksFromFile(runDirectory, workspace.getStringParameter("main.tasks_file"), workspace);
                }
                else {
                    workspace.logger.severe("Unknown tasksSource " + tasksSource + ". Exiting ...");
                    System.exit(1);
                }
            }
            catch (Exception e) {
                workspace.logger.severe("Exception encountered trying to read new tasks: " + e.getMessage());
                workspace.logger.severe("Continuing without updating tasks\n\n");
                e.printStackTrace();
                newAnalysisTasks = null;
            }

            workspace.getTaskSet().updateTasks(newAnalysisTasks);
            newAnalysisTasks = workspace.getTaskSet().getTasksToRun(); // For now this returns a list of all tasks. Later we can get fancier

            for (AnalysisTask task : newAnalysisTasks) {
                task.start();
            }

            // For now we just wait for all the tasks to end. Later we can get fancier.
            while (newAnalysisTasks.size() > 0) {
                AnalysisTask rt = newAnalysisTasks.remove(0);
                Thread thread = rt.getThread();
                while (thread.isAlive()) {
                    try {
                        thread.join(1000);
                    } catch (InterruptedException e) {
                        workspace.logger.severe("Unable to wait for thread " + rt.getName());
                        break;
                    }
                }
                workspace.logger.info("Done with task " + rt.getName());
            }
            //
            //        Thread t = new Thread(new AnalysisTask("George"));
            //        t.start();
            //        Thread t2 = new Thread(new AnalysisTask("Sam"));
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
            if (!daemon) keepRunning = false;
        }
    }

    private static JSONObject readConfigurationFile (String filePath) throws IOException, ParseException {
        FileReader fr = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fr);
        String contents = "";
        String line;
        while ((line = reader.readLine()) != null) {
            contents += line;
        }
        JSONObject obj = JsonUtilities.parse(contents);

        return obj;
    }

    private static ArrayList<AnalysisTask> loadTasksFromFile(String runDirectory, String tasksFile, Workspace workspace) throws Exception{

        JSONObject tasksConfiguration = readConfigurationFile(runDirectory + "/" + tasksFile);
        JSONArray analyses = (JSONArray) tasksConfiguration.get("analyses");

        ArrayList<AnalysisTask> allAnalysisTasks = new ArrayList<AnalysisTask>();
        for (int i = 0; i < analyses.size(); ++i) {
            allAnalysisTasks.add(new AnalysisTask(workspace, (JSONObject) analyses.get(i)));
        }

        return allAnalysisTasks;
    }
}
