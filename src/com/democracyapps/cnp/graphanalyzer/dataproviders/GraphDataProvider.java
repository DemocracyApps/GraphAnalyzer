package com.democracyapps.cnp.graphanalyzer.dataproviders;

import com.democracyapps.cnp.graphanalyzer.graph.Edge;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class GraphDataProvider extends DataProvider {
    private JSONObject specification = null;
    Graph graph = null;

    @Override
    public void initialize(JSONObject specification) {
        this.specification = specification;
        String dataProviderType = (String) specification.get("type");
        if (dataProviderType.equalsIgnoreCase("generate")) {
            String method = (String) specification.get("method");
            graph = generateGraph(method);
        }
    }

    @Override
    public Object getData() {
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
                String content = "I'm a node!";
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
                        String content = "I'm an edge!";
                        Edge e = new Edge(id, type, from, to, content);
                        g.addEdge(e);
                    }
                }
            }
        }
        return g;
    }

}
