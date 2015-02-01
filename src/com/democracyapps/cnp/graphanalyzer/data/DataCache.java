package com.democracyapps.cnp.graphanalyzer.data;

import com.democracyapps.cnp.graphanalyzer.data.providers.DBGraphDataProvider;
import com.democracyapps.cnp.graphanalyzer.data.providers.DataProvider;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * Created by ericjackson on 1/29/15.
 */
public class DataCache {
    Integer                         currentGraphId  = 1;
    private HashMap<Integer,DataSet> datasets = null;


    public synchronized DataSet getDataSet(Integer datasetId) {
        return datasets.get(datasetId);
    }

    public synchronized Integer loadDataSet(Workspace workspace, String dataSourceType, String dataSourceName, Integer project,
                              ParameterSet parameters) throws Exception {
        Integer id = currentGraphId++;
        DataProvider dp = null;

        if (dataSourceType.equalsIgnoreCase("db")) {
            DBGraphDataProvider pdp = new DBGraphDataProvider();
            parameters.put("project", project);
            pdp.initialize(workspace, parameters);
            dp = pdp;
        }
        else if (dataSourceType.equalsIgnoreCase("generator")) {
            // Use the dataSourceName to look up both class name and parameters in the configuration.
            parameters.put("generatorName", dataSourceName);

            Class<?> c = Class.forName("com.democracyapps.cnp.graphanalyzer.data.providers." + dataSourceName + "DataProvider");
            if (c == null) throw new Exception("Unable to find class " + dataSourceName + "DataProvider");
            dp = (DataProvider) c.newInstance();
            dp.initialize(workspace, parameters);
        }

        Graph g = dp.getData();

        if (datasets == null) datasets = new HashMap<Integer, DataSet>();
        datasets.put(id, new DataSet(id, g));
        return id;
    }

}
