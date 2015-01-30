package com.democracyapps.cnp.graphanalyzer.graph;

public class Node {
    long id;
    int type;
    String role = null;
    String content = null;

    public Node (long id, int type, String role, String content) {
        this.id = id;
        this.type = type;
        this.role = role;
        this.content = content;
    }

    public long getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public String getRole() {
        return this.role;
    }

    public String getContent() {
        return this.content;
    }

}
