package com.democracyapps.cnp.graphanalyzer.graph;

public class Node {
    long id;
    int type;
    String content = null;

    public Node (long id, int type, String content) {
        this.id = id;
        this.type = type;
        this.content = content;
    }

    public long getId() {
        return this.id;
    }
}
