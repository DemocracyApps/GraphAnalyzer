package com.democracyapps.cnp.graphanalyzer.tasks;

import com.democracyapps.cnp.graphanalyzer.Main;
import com.democracyapps.cnp.graphanalyzer.dataproviders.GraphDataProvider;
import com.democracyapps.cnp.graphanalyzer.graph.AdjMatrixGraph;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class TestingGraph extends Task {
    JSONObject dataProviderSpecification = null;
    JSONObject analysisSpecification = null;
    private Logger logger;

    public TestingGraph(JSONObject taskConfiguration) throws IOException {
        super(taskConfiguration);

        dataProviderSpecification = (JSONObject) taskConfiguration.get("dataprovider");
        analysisSpecification     = (JSONObject) taskConfiguration.get("analysis");

        logger = Main.gaGetLogger();
    }

    @Override
    public void run() {
        AdjMatrixGraph graph = null;
        GraphDataProvider gdp = new GraphDataProvider ();
        gdp.initialize(dataProviderSpecification);
        graph = gdp.getGraph();
        graph.printNodes();
        graph.runTests();

    }
}

