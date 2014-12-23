package com.democracyapps.cnp.graphanalyzer.dataproviders;

import com.democracyapps.cnp.graphanalyzer.graph.Edge;
import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.sql.*;

public class CNPPostgresDataProvider extends DataProvider {

    @Override
    public void initialize(JSONObject configuration) throws Exception {

        System.out.println("Testing JDBC Postgres connection");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new Exception("Can't find PostgreSQL JDBC Driver");
        }

        // Just verify that we can establish the connection
        Connection connection = openConnection();
        closeConnection(connection);
    }

    private static Connection openConnection() throws SQLException {
        Connection connection = null;

        connection = DriverManager.getConnection(
                "jdbc:postgresql://cnp.dev:5432/cnp", "ga",
                "graph01");

        if (connection == null) {
            throw new SQLException("Unable to create connection to PostgreSQL database");
        }
        return connection;
    }

    private static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public Object getData() throws SQLException {
        Connection c = openConnection();
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
        //public Edge (long id, int type, long from, long to, String content) {

        closeConnection(c);
        return g;
    }
}
