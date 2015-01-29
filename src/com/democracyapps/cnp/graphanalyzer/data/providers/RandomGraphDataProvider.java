package com.democracyapps.cnp.graphanalyzer.data.providers;

import com.democracyapps.cnp.graphanalyzer.data.providers.DataProvider;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;
import com.democracyapps.cnp.graphanalyzer.graph.Edge;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;

import java.util.ArrayList;
import java.util.Random;

public class RandomGraphDataProvider extends DataProvider {
    String method = null;
    Graph graph = null;

    @Override
    public void initialize(Workspace workspace, ParameterSet parameters) throws Exception {
        super.initialize(workspace, parameters);
        String name = (String) parameters.get("generatorName");

        method = parameters.getStringParam(name + ".method");
        graph = generateGraph(method);
    }

    @Override
    public Graph getData() {
        return graph;
    }

    private Graph generateGraph (String method) {
        Graph g = new Graph();
        if (method.equalsIgnoreCase("Erdos")) {
            double probability = .7;
            Random rand = new Random();
            int numNodes = 5;
            int i, j;
            for (i = 0; i < numNodes; i++) {
                long id = rand.nextLong();
                int type = 1;
                String content = "Node content";
                Node n = new Node(id, type, content);
                g.addNode(n);
            }
            ArrayList<Node> nodes = g.getAllNodes();
            for (i=0; i<nodes.size(); i++) {
                Node n1 = nodes.get(i);
                for (j=i; j<nodes.size(); j++) {
                    double p = rand.nextDouble();
                    if (i!=j && p <= probability) {
                        Node n2 = nodes.get(j);
                        long id = rand.nextLong();
                        int type = 1;
                        long from = n1.getId();
                        long to = n2.getId();
                        String content = "Edge content";
                        Edge e = new Edge(id, type, from, to, content);
                        g.addEdge(e);
                    }
                }
            }
        }
        return g;
    }

}
