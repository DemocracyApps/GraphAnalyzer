package com.democracyapps.cnp.graphanalyzer.tasks;

import com.democracyapps.cnp.graphanalyzer.data.DataCache;
import com.democracyapps.cnp.graphanalyzer.data.filters.GraphFilter;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.JsonUtilities;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;
import com.democracyapps.cnp.graphanalyzer.analysis.Analysis;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;
import org.json.simple.JSONObject;

public class AnalysisTask extends Task {
    ParameterSet parameters         = null;

    String name                     = null;
    String id                       = null;
    Integer project                 = null;

    String dataSourceType           = null;
    String dataSourceName           = null;
    Integer dataSetId               = null;

    Analysis analysis               = null;


    public AnalysisTask(Workspace workspace, JSONObject taskSpecification) throws Exception {
        super(workspace);
        parameters = new ParameterSet(taskSpecification);
        id = parameters.getStringParam("id");
        name = parameters.getStringParam("name");
        project = parameters.getIntegerParam("project");
        dataSourceName = parameters.getStringParam("dataSourceName");
        dataSourceType = parameters.getStringParam("dataSourceType");
        analysis = createAnalysis();
    }

    public AnalysisTask(Workspace workspace, String name, Integer analysisId, Integer projectId, String analysisSpecification) throws Exception {
        super(workspace);
        parameters = new ParameterSet();
        id = analysisId.toString();
        this.name = name;
        project = projectId;
        dataSourceType = "db";
        parameters.put("id", id);
        parameters.put("project", project);

        JSONObject analysisSpec;
        if (analysisSpecification != null) {
            analysisSpec = JsonUtilities.parse(analysisSpecification);
        }
        else {
            analysisSpec = new JSONObject();
        }
        parameters.put("analysis", analysisSpec);

        analysis = createAnalysis();
        analysis.setId(analysisId);
        analysis.setProject(projectId);
    }

    private Analysis createAnalysis () throws Exception {
        String analysisType = parameters.getStringParam("analysis.type");
        Analysis a;

        Class<?> c = Class.forName("com.democracyapps.cnp.graphanalyzer.analysis." + analysisType + "Analysis");
        if (c == null) throw new Exception("Unable to find class " + analysisType + "Analysis");
        a = (Analysis) c.newInstance();
        a.initialize (workspace, parameters);
        return a;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("AnalysisTask Thread " + this.name);
        System.out.println("Hello from thread " + Thread.currentThread().getName());
        DataCache dataCache = workspace.getDataCache();

        try {
            dataSetId = dataCache.loadDataSet(workspace, dataSourceType, dataSourceName, project, parameters);

            analysis.registerDataSet(dataSetId);
            GraphFilter gf = new GraphFilter(parameters.getJSONObject("analysis.take"), parameters.getJSONObject("analysis.drop"));
            analysis.registerFilter(gf);

            analysis.run();
            analysis.output();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String getId() {
        return id;
    }

    public Integer getProject() {
        return project;
    }

    public String getName() {
        return name;
    }
}
