package com.democracyapps.cnp.graphanalyzer.miscellaneous;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.Iterator;

/**
 * Created by ericjackson on 1/29/15.
 */
public class ParameterSet extends JSONObject {

    public ParameterSet (JSONObject orig) {
        super();
        Iterator<String> iter = orig.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            this.put(key,orig.get(key));
        }
    }

    public ParameterSet() {
        super();
    }

    private  Object resolve(String key, boolean spaceOnly) throws Exception {

        JSONObject config = this;
        String[] keys = key.split("\\.");
        String finalKey = keys[keys.length-1];
        if (keys.length > 1) {
            for (int i=0; i<keys.length - 1; ++i) {
                config = (JSONObject) config.get(keys[i]);
                if (config == null) throw new Exception("Null ParameterSet return for key " + keys[i] + "of parameter " + key);
            }
        }
        if (spaceOnly) {
            return config;
        }
        else {
            return config.get(finalKey);
        }
    }

    public Object put (Object key, Object value) {
        return super.put(key, value);
    }

    public void addParameter(String space, String key, Object value) throws Exception {
        JSONObject config = this;
        if (space != null) config = (JSONObject) resolve(space, true);
        config.put(key, value);
    }

    public Object getObjectParam(String key) throws Exception {
        return resolve(key, false);
    }

    public String getStringParam (String key) throws Exception {
        return (String) resolve(key, false);
    }

    public Integer getIntegerParam (String key) throws Exception {
        Object object = resolve(key, false);
        String className = object.getClass().getName();
        Integer value = null;
        if (className.equalsIgnoreCase("java.lang.Long")) {
            Long lvalue = (Long) object;
            value = lvalue.intValue();
        }
        else if (className.equalsIgnoreCase("java.lang.Integer")) {
            value = (Integer) object;
        }
        else {
            throw new Exception("Unknown object type: " + className);
        }
        return value;
    }

    public Boolean getBooleanParam (String key) throws Exception {
        return (Boolean) resolve(key, false);
    }

    public JSONObject getJSONObject (String key) throws Exception {
        return (JSONObject) resolve(key, false);
    }

    public JSONArray getJSONArray (String key) throws Exception {
        return (JSONArray) resolve(key, false);
    }
}
