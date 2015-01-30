package com.democracyapps.cnp.graphanalyzer.analysis;

import com.democracyapps.cnp.graphanalyzer.data.DataSet;
import com.democracyapps.cnp.graphanalyzer.data.filters.GraphFilter;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;

/**
 * Created by ericjackson on 1/27/15.
 */
abstract public class Analysis {
    Workspace workspace = null;
    ParameterSet parameters = null;
    Integer dataSetId = null;
    GraphFilter filter = null;

    public Analysis () {
    }

    public void initialize (Workspace w, ParameterSet p) {
        workspace = w;
        parameters = p;
    }

    public void registerDataSet (Integer dataId) {
        dataSetId = dataId;
    }
    public void registerFilter (GraphFilter gf) { filter = gf; }

    public Graph getGraph () throws Exception {
        DataSet ds = workspace.getDataCache().getDataSet(dataSetId);
        return ds.getGraph(filter);
    }
    abstract public void run();
    abstract public void output();
}
