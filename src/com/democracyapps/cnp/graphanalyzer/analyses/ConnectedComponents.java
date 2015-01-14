package com.democracyapps.cnp.graphanalyzer.analyses;

import com.democracyapps.cnp.graphanalyzer.graph.AdjMatrixGraph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Hanna on 1/11/15.
 */
public class ConnectedComponents {
    ArrayList<Node[]> connectedComponents;
    ArrayList<Node> nodes;
    int numNodes;
    boolean[] visited = new boolean[numNodes];
    double[][] adjMatrix;


    public ConnectedComponents(AdjMatrixGraph g) {
        adjMatrix = g.getAdjMatrix();
        numNodes = g.getNumNodes();
        nodes = g.getAllNodes();

        }

       /* private ArrayList<Node[]> breadthFirstSearch() {
            Queue<Integer> queue = new LinkedList<Integer>();
            int i, j, numConnectedComponents;
            numConnectedComponents = 0;
            for (i = 0; i < numNodes; i++) {
                if (!visited[i]) {
                    queue.add(i);
                }
                while (!queue.isEmpty()) {
                    Integer ind = queue.remove();
                    for (j=0;j<numNodes; j++) {
                        if (adjMatrix[ind][j]>0) {
                            queue.add(j);
                        }
                    }
                    visited[ind] = true;
                }
                Node[]
                connectedComponents.set(numConnectedComponents, );
            }
        }*/
}
