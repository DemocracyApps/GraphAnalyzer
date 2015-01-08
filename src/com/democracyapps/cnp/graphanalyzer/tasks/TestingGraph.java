package com.democracyapps.cnp.graphanalyzer.tasks;

import com.democracyapps.cnp.graphanalyzer.Main;
import com.democracyapps.cnp.graphanalyzer.dataproviders.GraphDataProvider;
import com.democracyapps.cnp.graphanalyzer.graph.AdjMatrixGraph;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
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
        Graph graphBase = null;
        GraphDataProvider gdp = new GraphDataProvider ();
        gdp.initialize(dataProviderSpecification);
        graphBase = (Graph)gdp.getData();
        AdjMatrixGraph graph = new AdjMatrixGraph(graphBase);

        graph.printNodes();
        if (graph.isDirected()) {System.out.println("graph is directed"); } else {System.out.println("graph is not directed");}
        graph.setDirected(false);
        if (graph.isDirected()) {System.out.println("graph is directed"); } else {System.out.println("graph is not directed");}
        graph.setDirected(true);
        if (graph.isWeighted()) {System.out.println("graph is weighted"); } else {System.out.println("graph is not weighted");}
        graph.setWeighted(false);
        if (graph.isWeighted()) {System.out.println("graph is weighted"); } else {System.out.println("graph is not weighted");}
        graph.setWeighted(true);
        if (graph.isWeighted()) {System.out.println("graph is weighted"); } else {System.out.println("graph is not weighted");}

        graph.printAdjMatrix();
        System.out.println("there are currently " +
                Integer.toString(graph.countEdges()) + " edges");

        graph.printAdjMatrix();
        graph.setDirected(false);
        graph.setWeighted(false);
        graph.printAdjMatrix();
    }
}

