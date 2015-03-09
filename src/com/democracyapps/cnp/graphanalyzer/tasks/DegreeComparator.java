package com.democracyapps.cnp.graphanalyzer.tasks;

import java.util.Comparator;
import com.democracyapps.cnp.graphanalyzer.graph.Node;


public class DegreeComparator implements Comparator<Node>{
    @Override
    public int compare(Node n1, Node n2) {
        return n2.getDegree() - n1.getDegree();
    }
}
