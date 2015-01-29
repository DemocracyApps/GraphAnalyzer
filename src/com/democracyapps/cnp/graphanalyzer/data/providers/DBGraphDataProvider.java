package com.democracyapps.cnp.graphanalyzer.data.providers;

import com.democracyapps.cnp.graphanalyzer.data.CNPDatabaseAccessor;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;
import com.democracyapps.cnp.graphanalyzer.graph.Edge;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;

import java.sql.*;

public class DBGraphDataProvider extends DataProvider {
    private CNPDatabaseAccessor databaseAccessor = null;

    @Override
    public void initialize(Workspace workspace, ParameterSet parameters) throws Exception {
        super.initialize(workspace, parameters);
        databaseAccessor = workspace.getDatabaseAccessor();
    }

    @Override
    public Graph getData() throws Exception {
        Connection c = databaseAccessor.openConnection();
        Statement stmt = null;
        Graph g = null;

        c.setAutoCommit(false);
        stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM ELEMENTS;");

        g = new Graph();

        while (rs.next()) {
            long id = rs.getBigDecimal("id").longValue();
            int type = rs.getInt("type");
            String content = rs.getString("content");
            System.out.println("ID: " + id + ", Type: " + type + ", Content: " + content);
            Node n = new Node (id, type, content);
            g.addNode(n);
        }
        rs.close();
        stmt.close();

        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM RELATIONS;");
        while (rs.next()) {
            long id = rs.getBigDecimal("id").longValue();
            int type = rs.getInt("relationid");
            long from = rs.getBigDecimal("fromid").longValue();
            long to = rs.getBigDecimal("toid").longValue();

            System.out.println("Relation ID: " + id + ", Type: " + type + ", from/to: " + from + "/" + to);
            Edge e = new Edge (id, type, from, to, null);
            g.addEdge(e);
        }
        rs.close();
        stmt.close();

        databaseAccessor.closeConnection();
        return g;
    }
}
