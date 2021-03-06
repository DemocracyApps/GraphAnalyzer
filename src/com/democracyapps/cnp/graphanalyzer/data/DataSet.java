package com.democracyapps.cnp.graphanalyzer.data;

import com.democracyapps.cnp.graphanalyzer.data.filters.Filter;
import com.democracyapps.cnp.graphanalyzer.data.filters.GraphFilter;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;

/**
 * Created by ericjackson on 1/29/15.
 */
public class DataSet {
    Integer id;
    String type = null;
    Object data = null;

    public DataSet(Integer id, Graph g) {
        this.id = id;
        type = "graph";
        data = g;
    }

    public Graph getGraph() {
        return (Graph) data;
    }

    public Graph getGraph (GraphFilter filter) throws Exception {
        Graph g = null;
        if (filter == null || filter.isNull()) {
            g = this.getGraph();
        }
        else {
            throw new Exception("Filtering not implemented yet");
        }
        return g;
    }
}
