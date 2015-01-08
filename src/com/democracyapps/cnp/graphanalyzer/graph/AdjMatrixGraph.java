package com.democracyapps.cnp.graphanalyzer.graph;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Matrix class for rudimentary adjacency matrix graph representation
 * Node size is fixed, but edges can be added or subtracted at will
 * If the graph is directed, then edges extend from the row node to the column node
 * If the graph is undirected, just the upper triangular matrix is stored
 */


public class AdjMatrixGraph extends Graph{

    private boolean directed;
    private boolean weighted;
    /*private ArrayList<Node> nodes;
    private ArrayList<Edge> edges; */
    private int numNodes;
    private ArrayList<Long>nodeOrder;
    private double[][] adjMatrix;

    public AdjMatrixGraph(boolean directedGraph, boolean weightedGraph) {
        directed = directedGraph;
        weighted = weightedGraph;
        nodeOrder = null;
        nodes = new HashMap<Long, Node>();
        edges = new HashMap<Long, Edge>();
        numNodes = this.nodes.size();
        adjMatrix = new double[numNodes][numNodes];
    }

    public AdjMatrixGraph(Graph g) {
        directed = true;
        weighted = false;
        numNodes = nodes.size();
        adjMatrix = new double[numNodes][numNodes];
        for(long n : nodes.keySet()) {
            nodeOrder.add(n);
        }
        for(Edge e : edges.values()) {
            addEdge(e);
        }
    }

    public boolean isDirected() {
        return directed;
    }

    public boolean isWeighted() {
        return  weighted;
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
                            adjMatrix[j][i] = 0;
                        } else if (adjMatrix[i][j] == 0) {
                            adjMatrix[i][j] = adjMatrix[j][i];
                            adjMatrix[j][i] = 0;
                        }
                    }
                }
            }
            directed = false;
        }
    }

    public void setWeighted(boolean bool) {
        if (bool) {
            weighted = true;
        } else {
            if (weighted) {
                int i, j;
                for (i = 0; i < numNodes; i++) {
                    for (j = 0; j < numNodes; j++) {
                        if (adjMatrix[i][j] > 0) {
                            adjMatrix[i][j] = 1;
                        }
                    }
                }
            }
            weighted = false;
        }

    }

    public void addEdge (Edge e) {
        // for unweighted graphs
        edges.put(e.id, e);
        int ind1 = nodeOrder.indexOf(e.from);
        int ind2 = nodeOrder.indexOf(e.to);
        if ((ind1 == -1)||(ind2 == -1)) {
            System.out.println("These two nodes aren't in the graph.");
        }
        if (directed) {
            adjMatrix[ind1][ind2] = 1;
        } else {
            if (ind1 < ind2) {
                adjMatrix[ind1][ind2] = 1;
            } else {
                adjMatrix[ind2][ind1] = 1;
            }
        }
    }

    public void addEdge (Edge e, double weight) {
        // for weighted graphs
        if (!weighted && weight<1) {
            System.out.println("warning: weighted edge in an unweighted graph");
        }
        int ind1 = nodeOrder.indexOf(e.from);
        int ind2 = nodeOrder.indexOf(e.to);
        if ((ind1 == -1)||(ind2 == -1)) {
            System.out.println("These two nodes aren't in the graph.");
        }
        if (directed) {
            adjMatrix[ind1][ind2] = weight;
        } else {
            if (ind1 < ind2) {
                adjMatrix[ind1][ind2] = weight;
            } else {
                adjMatrix[ind2][ind1] = weight;
            }
        }
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
            if (ind1 < ind2) {
                adjMatrix[ind1][ind2] = 0;
            } else {
                adjMatrix[ind2][ind1] = 0;
            }
        }
        edges.remove(e.id);
    }

    public void updateMatrix () {
        // must run after adding new nodes before new edges can be added
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

    public void printNodes() {
        for (Long i : nodeOrder) {
            System.out.println(nodes.get(i).content);
        }
        System.out.println("there are " + Integer.toString(numNodes) + " nodes");

    }

}

