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
        c.setAutoCommit(false);
        ResultSet rs;

        PreparedStatement selectElements = null, selectRelations = null;

        Integer project = parameters.getIntegerParam("project");

        String elementFields = "elements.id, elements.type, elements.name, elements.content";
        String relationFields = "id, fromid, toid, relationid";

        if (project <= 0) {
            String stmt = "SELECT " + elementFields + " FROM ELEMENTS;";
            selectElements = c.prepareStatement(stmt);
            stmt = "SELECT " + relationFields + " FROM RELATIONS;";
            selectRelations = c.prepareStatement(stmt);
        }
        else {
            String stmt = "SELECT DISTINCT " + elementFields + " FROM ELEMENTS ";
            stmt += "INNER JOIN RELATIONS ON RELATIONS.FROMID=ELEMENTS.ID WHERE RELATIONS.PROJECT=" + project + ";";
            selectElements = c.prepareStatement(stmt);
            stmt = "SELECT " + relationFields + " FROM RELATIONS WHERE PROJECT=" + project + ";";
            selectRelations = c.prepareStatement(stmt);
        }

        Graph g = new Graph();

        // Get the Elements
        try {
            rs = selectElements.executeQuery();

            while (rs.next()) {
                long id = rs.getBigDecimal("id").longValue();
                int type = rs.getInt("type");
                Node n = new Node(id, type, rs.getString("name"), rs.getString("content"));
                g.addNode(n);
            }
            rs.close();

            rs = selectRelations.executeQuery();

            while (rs.next()) {
                long id = rs.getBigDecimal("id").longValue();
                int type = rs.getInt("relationid");
                long from = rs.getBigDecimal("fromid").longValue();
                long to = rs.getBigDecimal("toid").longValue();

                Edge e = new Edge(id, type, from, to, null);
                g.addEdge(e);
            }
            rs.close();
            c.commit();

            databaseAccessor.closeConnection();
        }
        finally {
            if (selectElements != null)  selectElements.close();
            if (selectRelations != null) selectRelations.close();
        }
        return g;
    }
}
