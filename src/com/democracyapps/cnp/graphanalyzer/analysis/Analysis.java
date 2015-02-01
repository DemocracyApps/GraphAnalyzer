package com.democracyapps.cnp.graphanalyzer.analysis;

import com.democracyapps.cnp.graphanalyzer.data.DataCache;
import com.democracyapps.cnp.graphanalyzer.data.DataSet;
import com.democracyapps.cnp.graphanalyzer.data.filters.GraphFilter;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;

/**
 * Created by ericjackson on 1/27/15.
 */
abstract public class Analysis {
    Integer id = null;
    Integer project = null;
    Workspace workspace = null;
    ParameterSet parameters = null;
    Integer dataSetId = null;
    GraphFilter filter = null;

    public Analysis () {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProject (Integer id) {
        this.project = id;
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
        DataSet ds;
        DataCache dc = workspace.getDataCache();
        if (dc == null) throw new Exception ("No damn datacache!");
        ds = dc.getDataSet(dataSetId);
        if (ds == null) throw new Exception ("No damn data set with id " + dataSetId);
        return ds.getGraph(filter);
    }
    abstract public void run();
    abstract public void output();
}
