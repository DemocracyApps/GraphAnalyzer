package com.democracyapps.cnp.graphanalyzer.analysis;

import com.democracyapps.cnp.graphanalyzer.data.DataSet;
import com.democracyapps.cnp.graphanalyzer.graph.AdjMatrixGraph;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;

/**
 * Created by ericjackson on 1/29/15.
 */
public class HannaTestAnalysis extends Analysis {
    AdjMatrixGraph adjMatrixGraph = null;
    DataSet dataSet = null;
    Integer count = null;

    @Override
    public void initialize(Workspace w, ParameterSet p) {
        super.initialize(w, p);
    }

    @Override
    public void run() {
        try {
            count = parameters.getIntegerParam("analysis.count"); // Just as an example of how to access
            System.out.println("The value of the count parameter is " + count);
            dataSet = workspace.getDataCache().getDataSet(dataSetId);
            adjMatrixGraph = new AdjMatrixGraph(dataSet.getGraph());
        } catch (Exception e) {
            workspace.logger.severe("There was a problem running the HannaTestAnalysis: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void output() {
        adjMatrixGraph.printNodes();
        if (adjMatrixGraph.isDirected()) {System.out.println("graph is directed"); } else {System.out.println("graph is not directed");}
        adjMatrixGraph.setDirected(false);
        if (adjMatrixGraph.isDirected()) {System.out.println("graph is directed"); } else {System.out.println("graph is not directed");}
        adjMatrixGraph.setWeighted(true);
        if (adjMatrixGraph.isWeighted()) {System.out.println("graph is weighted"); } else {System.out.println("graph is not weighted");}
        adjMatrixGraph.setWeighted(false);
        if (adjMatrixGraph.isWeighted()) {System.out.println("graph is weighted"); } else {System.out.println("graph is not weighted");}

        adjMatrixGraph.printAdjMatrix();
        System.out.println("there are currently " +
                Integer.toString(dataSet.getGraph().countEdges()) + " edges");

    }
}
