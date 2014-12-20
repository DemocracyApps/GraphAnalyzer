package com.democracyapps.cnp.graphanalyzer.dataproviders;

import org.json.simple.JSONObject;

abstract public class DataProvider {
    public abstract void initialize(JSONObject configuration);
    public abstract Object getData();
}
