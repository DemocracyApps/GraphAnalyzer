package com.democracyapps.cnp.graphanalyzer.graph;

public class Node {
    long id;
    int type;
    String content = null;
    int degree = 0;

    public Node (long id, int type, String content) {
        this.id = id;
        this.type = type;
        this.content = content;
    }

    public long getId() {
        return this.id;
    }

    public void setDegree(int d){
        this.degree = d;
    }

    public int getDegree(){
        return this.degree;
    }
}
