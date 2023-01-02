package de.malte.f1strat;

import de.malte.f1strat.structure.Condition;
import org.json.simple.JSONObject;

public class DataInstance {

    public Condition dry;
    public Condition wet;


    public DataInstance(JSONObject data) {
        build(data);
    }

    private void build (JSONObject data) {
        // error handling
        if (data == null || data.size() < 1)
             System.err.println("No data available .. check your json file");

        // build data tree
        if (data.containsKey("dry"))
            dry = new Condition((JSONObject) data.get("dry"));
        if (data.containsKey("wet"))
            wet = new Condition((JSONObject) data.get("wet"));


    }
}
