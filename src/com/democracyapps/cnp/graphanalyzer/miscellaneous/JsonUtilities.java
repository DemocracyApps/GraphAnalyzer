package com.democracyapps.cnp.graphanalyzer.miscellaneous;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by ericjackson on 1/29/15.
 */
public class JsonUtilities {

    public static JSONObject parse(String s) throws ParseException { // Need to figure out a minify later.
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(s);
    }
}
