package com.democracyapps.cnp.graphanalyzer.miscellaneous;

import com.democracyapps.cnp.graphanalyzer.data.CNPDatabaseAccessor;
import com.democracyapps.cnp.graphanalyzer.data.DataCache;
import com.democracyapps.cnp.graphanalyzer.tasks.TaskSet;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by ericjackson on 1/28/15.
 *
 * This is basically a convenient place to hold everything we need
 */
public class Workspace {
    private static String           runDirectory    = null;
    String                          loggerName      = "com.democracyapps.cnp.graphanalyzer";
    public Logger                   logger          = null;
    ParameterSet                    parameters      = null;
    private JSONObject              configuration   = null;
    private TaskSet                 taskSet         = null;
    private CNPDatabaseAccessor     databaseAccessor = null;
    private DataCache dataCache = null;

    public Workspace(String runDir) {
        runDirectory = runDir;
        parameters = new ParameterSet();
        taskSet = new TaskSet(this);
        dataCache = new DataCache();
        this.initializeLogger();
    }

    private void initializeLogger ()  {
        if (logger == null) {
            try {
                logger = Logger.getLogger(loggerName);
                FileHandler fh = new FileHandler(runDirectory + "/ga_log.txt");
                fh.setFormatter(new SimpleFormatter());
                logger.addHandler(fh);
                logger.setLevel(Level.ALL);
            } catch (IOException e) {
                System.err.println("Unable to initialize logger - exiting");
                e.printStackTrace();
                System.exit(1);
            }
        }
        return;
    }

    public Logger getLogger() {
        return logger;
    }

    public void addParameters(String key, JSONObject config) {
        this.parameters.put(key, config);
    }

    public void addParameter (String space, String key, Object value) throws Exception {
        this.parameters.addParameter(space, key, value);
    }
    public JSONObject getConfiguration () {
        return configuration;
    }

    public void setDatabaseAccessor(CNPDatabaseAccessor databaseAccessor) {
        this.databaseAccessor = databaseAccessor;
    }

    public CNPDatabaseAccessor getDatabaseAccessor() {
        return this.databaseAccessor;
    }

    public TaskSet getTaskSet() {
        return taskSet;
    }

    public DataCache getDataCache() {
        return dataCache;
    }

    public void setTaskSet(TaskSet taskSet) {
        this.taskSet = taskSet;
    }

    /*
     * Parameter access
     */

    public Object getParameter(String key) {
        Object obj = null;
        try {
            obj = this.parameters.getObjectParam(key);
        } catch (Exception e) {
            Logger.getLogger(loggerName).severe(e.getMessage());
            System.exit(1);
        }
        return obj;
    }

    public String getStringParameter(String key) {
        String obj = null;
        try {
            obj = this.parameters.getStringParam(key);
        } catch (Exception e) {
            Logger.getLogger(loggerName).severe(e.getMessage());
            System.exit(1);
        }
        return obj;
    }

    public Integer getIntegerParameter(String key) {
        Integer obj = null;
        try {
            obj = this.parameters.getIntegerParam(key);
        } catch (Exception e) {
            Logger.getLogger(loggerName).severe(e.getMessage());
            System.exit(1);
        }
        return obj;
    }

    public Boolean getBooleanParameter(String key) {
        Boolean obj = null;
        try {
            obj = this.parameters.getBooleanParam(key);
        } catch (Exception e) {
            Logger.getLogger(loggerName).severe(e.getMessage());
            System.exit(1);
        }
        return obj;
    }


}
