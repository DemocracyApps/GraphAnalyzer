package com.democracyapps.cnp.graphanalyzer.analysis;

import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;

/**
 * Created by ericjackson on 1/27/15.
 */
abstract public class Analysis {
    Workspace workspace = null;
    ParameterSet parameters = null;
    Integer dataSetId = null;

    public Analysis () {
    }

    public void initialize (Workspace w, ParameterSet p) {
        workspace = w;
        parameters = p;
    }

    public void registerDataSet (Integer dataId) {
        dataSetId = dataId;
    }
    abstract public void run();
    abstract public void output();
}
