package com.democracyapps.cnp.graphanalyzer.graph;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Matrix class for rudimentary adjacency matrix graph representation
 * Node size is fixed, but edges can be added or subtracted at will
 * If the graph is directed, then edges extend from the row node to the column node
 * If the graph is undirected, just the upper triangular matrix is stored
 */


public class AdjMatrixGraph {

    private boolean directed;
    private boolean weighted;
    private ArrayList<Node> nodes;
    private int numNodes;
    private double[][] edges;

    public AdjMatrixGraph(boolean directedGraph, boolean weightedGraph) {
        directed = directedGraph;
        weighted = weightedGraph;
        nodes = new ArrayList<Node>(10);
        numNodes = 0;
        edges = new double[numNodes][numNodes];
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
                        if ((edges[i][j] > 0) && (edges[j][i] > 0)) {
                            edges[i][j] = (edges[i][j] + edges[j][i]) / 2;
                            edges[j][i] = 0;
                        } else if (edges[i][j] == 0) {
                            edges[i][j] = edges[j][i];
                            edges[j][i] = 0;
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
                        if (edges[i][j] > 0) {
                            edges[i][j] = 1;
                        }
                    }
                }
            }
            weighted = false;
        }

    }

    public void addNode (Node n) {
        // adds the node to the node array, but does NOT automatically
        // update the matrix, must call updateMatrix after adding nodes
        nodes.ensureCapacity(numNodes);
        nodes.add(numNodes,n);
        numNodes++;
    }

    public void addEdge (Node n1, Node n2) {
        // for unweighted graphs
        int ind1 = nodes.indexOf(n1);
        int ind2 = nodes.indexOf(n2);
        if ((ind1 == -1)||(ind2 == -1)) {
            System.out.println("These two nodes aren't in the graph.");
        }
        if (directed) {
            edges[ind1][ind2] = 1;
        } else {
            if (ind1 < ind2) {
                edges[ind1][ind2] = 1;
            } else {
                edges[ind2][ind1] = 1;
            }
        }
    }

    public void addEdge (Node n1, Node n2, double weight) {
        // for weighted graphs
        if (!weighted && weight<1) {
            System.out.println("warning: weighted edge in an unweighted graph");
        }
        int ind1 = nodes.indexOf(n1);
        int ind2 = nodes.indexOf(n2);
        if ((ind1 == -1)||(ind2 == -1)) {
            System.out.println("These two nodes aren't in the graph.");
        }
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
        int ind1 = nodes.indexOf(n1);
        int ind2 = nodes.indexOf(n2);
        if ((ind1 == -1)||(ind2 == -1)) {
            System.out.println("These two nodes aren't in the graph.");
        }
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

    public void updateMatrix () {
        // must run after adding new nodes before new edges can be added
        double[][] oldEdges = edges;
        edges = new double[numNodes][numNodes];
        int i,j;
        for (i=0; i<oldEdges.length; i++) {
            for (j=0; j<oldEdges.length; j++) {
                double w = oldEdges[i][j];
                edges[i][j] = w;
            }
        }
    }

    public int countEdges() {
        int count = 0;
        int i, j;
        if (directed) {
            for (i = 0; i < numNodes; i++) {
                for (j = 0; j < numNodes; j++) {
                    if (edges[i][j] > 0) {
                        count++;
                    }
                }
            }
        } else {
            for (i = 0; i < numNodes; i++) {
                for (j = i; j < numNodes; j++) {
                    if (edges[i][j] > 0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public double edgeWeight(Node n1,Node n2) {
        int ind1 = nodes.indexOf(n1);
        int ind2 = nodes.indexOf(n2);
        if ((ind1 == -1) || (ind2 == -1)) {
            System.out.println("A node is missing from the graph.");
            return -1;
        } else {
            if (directed) {
                return edges[ind1][ind2];
            } else {
                if (ind1 < ind2) {
                    return edges[ind1][ind2];
                } else {
                    return edges[ind2][ind1];
                }
            }
        }
    }

    public void printAdjMatrix() {
        int i, j;
        for (i = 0; i < numNodes; i++) {
            for (j = 0; j < numNodes; j++) {
                System.out.print(Double.toString(edges[i][j]) + " ");
            }
            System.out.print("\n");
        }
    }

    public void printNodes() {
        for (Node n : nodes) {
            System.out.println(n.content);
        }
        System.out.println("there are " + Integer.toString(numNodes) + " nodes");

    }

    public void runTests() {
        if (this.isDirected()) {System.out.println("graph is directed"); } else {System.out.println("graph is not directed");}
        this.setDirected(false);
        if (this.isDirected()) {System.out.println("graph is directed"); } else {System.out.println("graph is not directed");}
        this.setDirected(true);
        if (this.isWeighted()) {System.out.println("graph is weighted"); } else {System.out.println("graph is not weighted");}
        this.setWeighted(false);
        if (this.isWeighted()) {System.out.println("graph is weighted"); } else {System.out.println("graph is not weighted");}
        this.setWeighted(true);
        if (this.isWeighted()) {System.out.println("graph is weighted"); } else {System.out.println("graph is not weighted");}

        Node n1 = this.nodes.get(0);
        Node n2 = this.nodes.get(1);
        Node n3 = this.nodes.get(2);

        this.addEdge(n1,n2,.1);
        this.addEdge(n1,n3,.5);
        this.addEdge(n3,n2);
        this.printAdjMatrix();
        System.out.println("there are currently " +
                Integer.toString(this.countEdges()) + " edges");

        if (this.edgeWeight(n1,n2)>0) {
            System.out.println("n1 and n2 are connected with weight "
                    + Double.toString(this.edgeWeight(n1, n2)));
        } else {
            System.out.println("n1 and n2 are not connected");
        }

        this.removeEdge(n1,n2);
        if (this.edgeWeight(n1,n2)>0) {
            System.out.println("n1 and n2 are connected with weight "
                    + Double.toString(this.edgeWeight(n1, n2)));
        } else {
            System.out.println("n1 and n2 are not connected");
        }

        this.printAdjMatrix();
        this.setDirected(false);
        this.setWeighted(false);
        this.printAdjMatrix();


    }
}

