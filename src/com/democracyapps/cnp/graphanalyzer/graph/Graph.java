package com.democracyapps.cnp.graphanalyzer.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Graph {
    HashMap<Long, Node> nodes = null;
    HashMap<Long, Edge> edges = null;
    Boolean directed = true;

    public Graph() {
        nodes = new HashMap<Long, Node>();
        edges = new HashMap<Long, Edge>();
    }

    public void addNode(Node n) {
        nodes.put(n.id, n);
    }

    public void addEdge(Edge e) {
        edges.put(e.id, e);
    }

    public void removeEdge(Edge e) {
        edges.remove(e.id);
    }

    public Node getNode(long id) {
        Node n = null;
        if (nodes.containsKey(id)) n = nodes.get(id);
        return n;
    }

    public Edge getEdge(long id) {
        Edge e = null;
        if (edges.containsKey(id)) e = edges.get(id);
        return e;
    }

    public int countNodes() {
        return this.nodes.size();
    }

    public ArrayList<Node> getAllNodes() {
        ArrayList<Node> result = new ArrayList<Node>();
        Iterator<Long> iter = this.nodes.keySet().iterator();
        while (iter.hasNext()) {
            Long id = iter.next();
            result.add(this.nodes.get(id));
        }
        return result;
    }

    public ArrayList<Edge> getAllEdges() {
        ArrayList<Edge> result = new ArrayList<Edge>();
        Iterator<Long> iter = this.edges.keySet().iterator();
        while (iter.hasNext()) {
            Long id = iter.next();
            result.add(this.edges.get(id));
        }
        return result;
    }

    public int countEdges() {
        return this.edges.size();
    }
}