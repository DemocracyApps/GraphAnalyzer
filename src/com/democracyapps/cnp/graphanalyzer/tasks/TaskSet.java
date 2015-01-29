package com.democracyapps.cnp.graphanalyzer.tasks;

/**
 * Created by ericjackson on 1/27/15.
 */

import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class TaskSet {
    HashMap<String,AnalysisTask> tasksById = null;
    HashMap<Integer, ArrayList<AnalysisTask>> tasksByProject = null;
    Logger logger;
    Workspace workspace;

    public TaskSet(Workspace workspace) {
        logger = Logger.getLogger("com.democracyapps.cnp.graphanalyzer");
        this.workspace = workspace;
    }

    public void updateTasks(ArrayList<AnalysisTask> newAnalysisTasks) {

        if (newAnalysisTasks == null) logger.severe("calling updateTasks with NULL newAnalysisTasks");
        if (newAnalysisTasks == null || newAnalysisTasks.size() == 0) return;
        if (tasksById == null) tasksById = new HashMap<String,AnalysisTask>();
        if (tasksByProject == null) tasksByProject = new HashMap<Integer, ArrayList<AnalysisTask>>();

        for (AnalysisTask task: newAnalysisTasks) {
            tasksById.put(task.getId(), task);
            Integer project = task.getProject();
            if (! tasksByProject.containsKey(project)) {
                tasksByProject.put(project, new ArrayList<AnalysisTask>());
            }
            tasksByProject.get(project).add(task);
        }
    }

    public ArrayList<AnalysisTask> getTasksToRun() {
        ArrayList<AnalysisTask> tasks = new ArrayList<AnalysisTask>();
        if (tasksById == null) {
            logger.severe("Null tasksById");
        }
        for (AnalysisTask t: tasksById.values()) {
            tasks.add(t);
        }
        return tasks;
    }
}
