package com.democracyapps.cnp.graphanalyzer.dataproviders;

import org.json.simple.JSONObject;

import java.sql.SQLException;

abstract public class DataProvider {
    public abstract void initialize(JSONObject configuration) throws Exception;
    public abstract Object getData() throws Exception;
}
