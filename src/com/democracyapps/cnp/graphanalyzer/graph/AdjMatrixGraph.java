package com.democracyapps.cnp.graphanalyzer.graph;

/**
 * Matrix class for rudimentary adjacency matrix graph representation
 * Node size is fixed, but edges can be added or subtracted at will
 * If the graph is directed, then edges extend from the row node to the column node
 * If the graph is undirected, just the upper triangular matrix is stored
 */


public class AdjMatrixGraph {

    private int numNodes;
    private boolean directed;
    private double[][] edges;
    private Node[] nodes;

    public AdjMatrixGraph(Node[] newNodes) {
        // builds an empty adjacency matrix for the given node array
        numNodes = newNodes.length;
        nodes = newNodes;
        edges = new double[numNodes][numNodes];
    }

    private int findNode (Node n) {
        // returns the index of the node in the array
        int i;
        int ind = -1;
        for (i=0; i<numNodes; i++) {
            if (n.id == nodes[i].id) {
                ind = i;
            }
        }
        if (ind == -1) {
            System.out.println("The node is not in the graph.\n");
        }
        return ind;
    }

    public void addEdge (Node n1, Node n2, double weight) {
        // adds an edge from n1 to n2
        int ind1 = findNode(n1);
        int ind2 = findNode(n2);
        if (directed) {
            edges[ind1][ind2] = weight;
        } else {
            if (ind1 < ind2) {
                edges[ind1][ind2] = weight;
            } else {
                edges[ind2][ind1] = weight;
            }
        }
    }

    public void removeEdge (Node n1, Node n2) {
        // removes the edge from n1 to n2
        int ind1 = findNode(n1);
        int ind2 = findNode(n2);
        if (directed) {
            edges[ind1][ind2] = 0;
        } else {
            if (ind1 < ind2) {
                edges[ind1][ind2] = 0;
            } else {
                edges[ind2][ind1] = 0;
            }
        }
    }


    public int countEdges() {
        int count = 0;
        int i,j;
        if (directed) {
            for (i = 0; i < numNodes; i++) {
                for (j = 0; j < numNodes; j++) {
                    if (edges[i][j]>0) {
                    count++;
                    }
                }
            }
        } else {
            for (i = 0; i < numNodes; i++) {
                for (j = i; j < numNodes; j++) {
                    if (edges[i][j]>0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void printAdjMatrix () {
        int i,j;
        for (i=0; i<numNodes; i++) {
            for (j=0; j<numNodes; j++) {
                System.out.println( Double.toString(edges[i][j]) + " ");
            }
            System.out.println("\n");
        }
    }
}
