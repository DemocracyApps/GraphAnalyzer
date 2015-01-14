package com.democracyapps.cnp.graphanalyzer.tasks;


import com.democracyapps.cnp.graphanalyzer.Main;
import com.democracyapps.cnp.graphanalyzer.dataproviders.CNPPostgresDataProvider;
import com.democracyapps.cnp.graphanalyzer.dataproviders.DataProvider;
import com.democracyapps.cnp.graphanalyzer.dataproviders.GraphDataProvider;
import com.democracyapps.cnp.graphanalyzer.graph.AdjMatrixGraph;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GraphAnalysisTask extends Task {
    JSONObject dataProviderSpecification = null;
    JSONObject analysisSpecification = null;
    private Logger logger;

    public GraphAnalysisTask(JSONObject taskConfiguration) throws IOException {
        super(taskConfiguration);

        dataProviderSpecification = (JSONObject) taskConfiguration.get("dataprovider");
        analysisSpecification     = (JSONObject) taskConfiguration.get("analysis");

        logger = Main.gaGetLogger();
    }

    @Override
    public void run() {
        Graph graph = null;
        DataProvider gdp;

        String providerType = (String) dataProviderSpecification.get("type");

        // @TODO Need to create a factory for this.
        if (providerType.equalsIgnoreCase("postgres")) {
            gdp = new CNPPostgresDataProvider();
        }
        else if (providerType.equalsIgnoreCase("generate")) {
            gdp = new GraphDataProvider();
        }
        else {
            return;
        }
        try {
            gdp.initialize(dataProviderSpecification);
            graph = (Graph) gdp.getData();
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.severe("Task " + this.name + " - unable to initialize CNPPostgresDataProvider");
            logger.severe(" Task " + this.name + " - " + e.getMessage());
            return;
        }

        // Probably need to create some analysis classes or something, but for now ...

        if (graph != null) {
            String analysisType = (String) analysisSpecification.get("type");

            if (analysisType.equalsIgnoreCase("counts")) {
                int edgeCount = graph.countEdges();
                int nodeCount = graph.countNodes();
                System.out.println("The counts from " + this.name + " are " + nodeCount + " nodes and " + edgeCount + " edges.");
            } else if (analysisType.equalsIgnoreCase("printmatrix")) {
                AdjMatrixGraph adjMatrixGraph = new AdjMatrixGraph(graph);
                adjMatrixGraph.printAdjMatrix();
            } else if (analysisType.equalsIgnoreCase("maxdegree")) {
                AdjMatrixGraph adjMatrixGraph = new AdjMatrixGraph(graph);
                ArrayList<Node> nodes = adjMatrixGraph.getAllNodes();
                double[][] adjMatrix = adjMatrixGraph.getAdjMatrix();
                int i,j;
                int numNodes = adjMatrixGraph.getNumNodes();
                int maxDegree = 0;
                Node maxDegreeNode = nodes.get(0);
                int[] degree = new int[numNodes];
                for (i=0; i<numNodes; i++) {
                    int nodeDegree = 0;
                    for (j=0; j<numNodes; j++) {
                        nodeDegree += adjMatrix[i][j];
                    }
                    degree[i] = nodeDegree;
                    if (nodeDegree > maxDegree) {
                        maxDegree = nodeDegree;
                        maxDegreeNode = nodes.get(i);
                    }
                }
                System.out.println("Maximum degree is " + Integer.toString(maxDegree)
                        + " at node " + Long.toString(maxDegreeNode.getId()));
            }
        }
    }
}

