package com.democracyapps.cnp.graphanalyzer.graph;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Matrix class for rudimentary adjacency matrix graph representation
 * Node size is fixed, but edges can be added or subtracted at will
 * If the graph is directed, then edges extend from the row node to the column node
 */


public class AdjMatrixGraph {
    private int numNodes;
    private ArrayList<Long> nodeOrder;
    private double[][] adjMatrix;
    private boolean directed;
    public AdjMatrixGraph(Graph g) {
        directed = g.directed;
        numNodes = g.nodes.size();
        adjMatrix = new double[numNodes][numNodes];
        nodeOrder = new ArrayList<Long>();
        for(long n : g.nodes.keySet()) {
            nodeOrder.add(n);
        }
        for(Edge e : g.edges.values()) {
            addEdge(e);
        }
    }
    public void setDirected(boolean bool) {
        if (bool) {
            directed = true;
        } else {
            if (directed) {
                int i, j;
                for (i = 0; i < numNodes; i++) {
                    for (j = i; j < numNodes; j++) {
                        if ((adjMatrix[i][j] > 0) && (adjMatrix[j][i] > 0)) {
                            adjMatrix[i][j] = (adjMatrix[i][j] + adjMatrix[j][i]) / 2;
                            adjMatrix[j][i] = (adjMatrix[i][j] + adjMatrix[j][i]) / 2;
                        } else if (adjMatrix[i][j] >= 0) {
                            adjMatrix[j][i] = adjMatrix[i][j];
                        } else if (adjMatrix[j][i] >= 0) {
                            adjMatrix[i][j] = adjMatrix[j][i];
                        }
                    }
                }
            }
            directed = false;
        }
    }
    public void addEdge (Edge e, double weight) {
        int ind1 = nodeOrder.indexOf(e.from);
        int ind2 = nodeOrder.indexOf(e.to);
        if ((ind1 == -1) || (ind2 == -1)) {
            System.out.println("These two nodes are not in the graph.");
            if (directed) {
                adjMatrix[ind1][ind2] = weight;
            } else {
                adjMatrix[ind1][ind2] = weight;
                adjMatrix[ind2][ind1] = weight;
            }
        }
    }
    public void addEdge (Edge e) {
        addEdge(e, 1);
    }
    public void removeEdge (Edge e) {
        int ind1 = nodeOrder.indexOf(e.from);
        int ind2 = nodeOrder.indexOf(e.to);
        if ((ind1 == -1)||(ind2 == -1)) {
            System.out.println("These two nodes aren't in the graph.");
        }
        if (directed) {
            adjMatrix[ind1][ind2] = 0;
        } else {
            adjMatrix[ind1][ind2] = 0;
            adjMatrix[ind2][ind1] = 0;
        }
    }
    public void updateMatrix () {       // must run after adding new nodes before new edges can be added 
       double[][] oldAdjMatrix = adjMatrix;
        adjMatrix = new double[numNodes][numNodes];
        int i,j;
        for (i=0; i<oldAdjMatrix.length; i++) {
          for (j=0; j<oldAdjMatrix.length; j++) {
            double w = oldAdjMatrix[i][j];
       adjMatrix[i][j] = w;
          }
        }
    }
    public void printAdjMatrix() {
        int i, j;
        for (i = 0; i < numNodes; i++) {
            for (j = 0; j < numNodes; j++) {
                System.out.print(Double.toString(adjMatrix[i][j]) + " ");
            }
            System.out.print("\n");
        }
    }
    public ArrayList<Long> getNodeOrder() {
        return nodeOrder;
    }
}

