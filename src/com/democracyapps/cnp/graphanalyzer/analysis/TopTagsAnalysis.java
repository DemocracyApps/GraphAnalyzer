package com.democracyapps.cnp.graphanalyzer.analysis;

import com.democracyapps.cnp.graphanalyzer.graph.Graph;
import com.democracyapps.cnp.graphanalyzer.graph.Node;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.ParameterSet;
import com.democracyapps.cnp.graphanalyzer.miscellaneous.Workspace;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.*;

/**
 * Created by ericjackson on 1/29/15.
 */
@SuppressWarnings("unused")
public class TopTagsAnalysis extends Analysis {
    ArrayList<CountMapper> sortList = null;
    int cutoff = -1;

    @Override
    public void initialize(Workspace w, ParameterSet p) {
        super.initialize(w, p);
    }

    private class CountMapper implements Comparable {
        Long id;
        Integer count;
        Node node;

        public CountMapper(Long id, Integer count, Node n) {
            this.id = id;
            this.count = count;
            this.node = n;
        }

        @Override
        public int compareTo(Object o) {
            int val =  ((CountMapper) o).count - this.count;
            if (val == 0) {
                Long lval = (this.id - ((CountMapper) o).id);
                val = -lval.intValue(); // Show newer nodes
            }
            return val;
        }
    }

    @Override
    public void run() {
        try {
            cutoff = parameters.getIntegerParam("analysis.cutoff");
            System.out.println("Cut off is " + cutoff);
            JSONObject target = parameters.getJSONObject("analysis.target");
            Long elementType = (Long) target.get("ElementType");
            if (elementType != null) {
                int eType = elementType.intValue();
                Graph g = this.getGraph();
                HashMap<Long,Integer> degrees = g.getNodeDegreeCounts();
                sortList = new ArrayList<CountMapper>();
                Iterator<Long> iter = degrees.keySet().iterator();
                while (iter.hasNext()) {
                    Long id = iter.next();
                    Node n = g.getNode(id);
                    if (n.getType() == eType) {
                        sortList.add(new CountMapper(id, degrees.get(id), n));
                    }
                }
                Collections.sort(sortList);
            }

        } catch (Exception e) {
            workspace.logger.severe("There was a problem running the TopTagsAnalysis: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void output() {
        PreparedStatement insertOutput = null;
        try {
            Connection c = workspace.getDatabaseAccessor().openConnection();
            c.setAutoCommit(false);
            ResultSet rs;

            // Create the analysis output record
            String stmt = "INSERT INTO analysis_outputs (perspective, project, output, created_at, updated_at) VALUES (?, ?, ?, ?, ?);";
            insertOutput = c.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            insertOutput.setInt(1, this.id);
            insertOutput.setInt(2, this.project);
            insertOutput.setString(3, "This is my output");
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            insertOutput.setTimestamp(4, now, cal);
            insertOutput.setTimestamp(5, now, cal);
            insertOutput.executeUpdate();
            rs = insertOutput.getGeneratedKeys();
            int outputId = -1;
            if (rs.next()) {
                outputId = rs.getInt(1);
                System.out.println("YAY! Got an analysis_output id of " + outputId);
            }

            // Create the analysis set record
            stmt = "INSERT INTO analysis_sets (analysis_output, description, created_at, updated_at) VALUES (?, ?, ?, ?);";
            insertOutput = c.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            insertOutput.setInt(1, outputId);
            insertOutput.setString(2, "Top " + cutoff + " tags");
            now = new Timestamp(System.currentTimeMillis());
            cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            insertOutput.setTimestamp(3, now, cal);
            insertOutput.setTimestamp(4, now, cal);
            insertOutput.executeUpdate();
            rs = insertOutput.getGeneratedKeys();
            int setId = -1;
            if (rs.next()) {
                setId = rs.getInt(1);
                System.out.println("YAY! Got the set ID of " + setId);
            }

            // Create the set items record
            stmt = "INSERT INTO analysis_set_items (analysis_set, item, created_at, updated_at) VALUES (?, ?, ?, ?);";
            insertOutput = c.prepareStatement(stmt);
            int count = 1;
            for (CountMapper item: sortList) {
                System.out.println("Count for " + item.id + "(" + item.node.getContent() + ") is " + item.count);

                insertOutput.setInt(1, setId);
                Long nodeId = item.node.getId();
                insertOutput.setInt(2, nodeId.intValue());
                now = new Timestamp(System.currentTimeMillis());
                cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                insertOutput.setTimestamp(3, now, cal);
                insertOutput.setTimestamp(4, now, cal);
                insertOutput.executeUpdate();

                ++count;
                if (count > cutoff && cutoff > 0) break;
            }

            c.commit();
            workspace.getDatabaseAccessor().closeConnection();
        }
        catch (Exception e) {
            workspace.logger.severe("Failed to output analysis results " + e.getMessage());
        }
        finally {
            try {
                if (insertOutput != null) insertOutput.close();
            } catch (SQLException e) {
                workspace.logger.severe("Error closing prepared statements in analysis output: " + e.getMessage());
            }
        }



    }
}
