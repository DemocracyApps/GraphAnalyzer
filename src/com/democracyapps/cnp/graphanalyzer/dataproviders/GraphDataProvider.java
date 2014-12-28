package com.democracyapps.cnp.graphanalyzer.dataproviders;

import com.democracyapps.cnp.graphanalyzer.graph.AdjMatrixGraph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
import org.json.simple.JSONObject;

public class GraphDataProvider extends DataProvider {
    private JSONObject specification = null;
    AdjMatrixGraph graph = null;

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
    public AdjMatrixGraph getGraph() {
        return graph;
    }

    private AdjMatrixGraph generateGraph (String method) {
        AdjMatrixGraph g;
        Node n1 = new Node(10, 1, "I'm the first node!");
        Node n2 = new Node(20, 2, "I'm the second node!");
        Node n3 = new Node(30, 3, "I'm the third node!");
        g = new AdjMatrixGraph(true,true);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.updateMatrix();
        return g;
    }

}
