package com.democracyapps.cnp.graphanalyzer.graph;
import java.util.ArrayList;
import java.util.Iterator;

/** 
 * Created by Hanna on 2/2/15. 
 */

public class AdjList {
    public ArrayList<Node> nodeOrder = null;
    public ArrayList<Long>[] adj = null;
    public  AdjList(Graph G){
        nodeOrder = G.getAllNodes();
        Iterator<Long> iter = G.edges.keySet().iterator();
        while(iter.hasNext()) {
            Long id = iter.next();
            Edge e = G.edges.get(id);
            int ind = nodeOrder.indexOf(e.from);
            adj[ind].add(e.to);
        }
    }

    public ArrayList<Node> getNodeOrder() {
        return nodeOrder;
    }

    public void findDegrees() {
        int i;
        int numNodes = nodeOrder.size();
        for (i = 1 ; i<=numNodes; i++) {
            Node n = nodeOrder.get(i);
            n.setDegree(adj[i].size());
        }
    }
}