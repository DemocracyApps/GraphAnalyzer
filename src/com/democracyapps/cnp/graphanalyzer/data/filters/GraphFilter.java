package com.democracyapps.cnp.graphanalyzer.data.filters;

import org.json.simple.JSONObject;

/**
 * Created by ericjackson on 1/30/15.
 */
public class GraphFilter extends Filter {

    public GraphFilter (JSONObject take, JSONObject drop) throws Exception {
        if (take != null || drop != null)
            throw new Exception ("Take/Drop filtering not yet implemented");
        else
            this.nullFilter = true;
    }
}
