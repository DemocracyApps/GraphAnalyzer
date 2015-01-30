package com.democracyapps.cnp.graphanalyzer.data;

import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;
import com.democracyapps.cnp.graphanalyzer.tasks.AnalysisTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

public class CNPDatabaseAccessor {
    private String connectionString = null;
    private String userName = null;
    private String password = null;
    private Logger logger = null;

    private Connection currentConnection = null;

    public CNPDatabaseAccessor(String connectionString, String userName, String password) throws Exception {
        this.connectionString = connectionString;
        this.userName = userName;
        this.password = password;
        logger = Logger.getLogger("com.democracyapps.cnp.graphanalyzer");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new Exception("Can't find PostgreSQL JDBC Driver");
        }
    }

    public Connection openConnection() throws SQLException {

        currentConnection = DriverManager.getConnection(this.connectionString, this.userName, this.password);

        if (currentConnection == null) {
            throw new SQLException("Unable to create connection to PostgreSQL database");
        }

        return currentConnection;
    }

    public void closeConnection() throws SQLException {
        if (currentConnection != null) {
            currentConnection.close();
            currentConnection = null;
        }
    }

    public ArrayList<AnalysisTask> loadNewTasks(Boolean daemon, Workspace workspace) throws Exception {
        ArrayList<AnalysisTask> allAnalysisTasks = new ArrayList<AnalysisTask>();

        PreparedStatement selectStmt = null, updateStmt = null;
        try {
            Connection c = this.openConnection();
            ResultSet rs;
            String select, update;
            if (daemon) {
                select = "SELECT id, name, project, specification FROM ANALYSES WHERE (last IS NULL OR last < updated_at) FOR UPDATE;";
                update = "UPDATE ANALYSES SET last = ? WHERE (last IS NULL OR last < updated_at);";
            } else {
                select = "SELECT id, name, project, specification FROM ANALYSES FOR UPDATE;";
                update = "UPDATE ANALYSES SET last = ? ;";
            }

            selectStmt = c.prepareStatement(select);
            updateStmt = c.prepareStatement(update);

            c.setAutoCommit(false);

            rs = selectStmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");

                allAnalysisTasks.add(new AnalysisTask(workspace, rs.getString("name"), id, rs.getInt("project"), rs.getString("specification")));

            }
            rs.close();

            Timestamp now = new Timestamp(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            updateStmt.setTimestamp(1, now, cal);
            updateStmt.executeUpdate();

            c.commit();

            this.closeConnection();
        }
        finally {
            try {
                if (selectStmt != null) selectStmt.close();
                if (updateStmt != null) updateStmt.close();
            } catch (SQLException e) {
                logger.severe(e.getMessage());
            }
        }

        return allAnalysisTasks;
    }

}