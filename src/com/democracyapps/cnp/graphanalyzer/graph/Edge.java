package com.democracyapps.cnp.graphanalyzer.graph;

public class Edge {
    long id;
    int type;
    long from, to;
    String content = null;

    public Edge (long id, int type, long from, long to, String content) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.to = to;
        this.content = content;
    }
}
