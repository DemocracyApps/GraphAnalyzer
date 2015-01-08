package com.democracyapps.cnp.graphanalyzer.dataproviders;

import com.democracyapps.cnp.graphanalyzer.graph.AdjMatrixGraph;
import com.democracyapps.cnp.graphanalyzer.graph.Edge;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
import org.json.simple.JSONObject;

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
        Graph g;
        Node n1 = new Node(10, 1, "I'm the first node!");
        Node n2 = new Node(20, 2, "I'm the second node!");
        Node n3 = new Node(30, 3, "I'm the third node!");
        Edge e1 = new Edge(12, 1, 10, 20, "Edge from first to second" );
        Edge e2 = new Edge(13, 2, 10, 30, "Edge from first to third" );
        Edge e3 = new Edge(23, 3, 10, 20, "Edge from third to first" );
        g = new Graph();
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addEdge(e1);
        g.addEdge(e2);
        g.addEdge(e3);
        return g;
    }

}
