package com.democracyapps.cnp.graphanalyzer.data.providers;

import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;

abstract public class DataProvider {
    Workspace workspace = null;
    ParameterSet parameters = null;

    public DataProvider() {

    }
    public void initialize(Workspace workspace, ParameterSet parameters) throws Exception {
        this.workspace = workspace;
        this.parameters = parameters;
    }
    public abstract Graph getData() throws Exception;
}
