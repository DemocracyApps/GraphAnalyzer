package com.democracyapps.cnp.graphanalyzer.dataproviders;

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
        Graph g = null;
        if (method.equalsIgnoreCase("silly")) {
            Node n1 = new Node(1, 0, "A"), n2 = new Node (2, 0, "B");
            Edge e = new Edge(1, 3, 1, 2, "Nada");
            g = new Graph();
            g.addNode(n1);
            g.addNode(n2);
            g.addEdge(e);
        }

        return g;
    }

}
