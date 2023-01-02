package de.malte.f1strat;

import de.malte.f1strat.structure.Condition;
import org.json.simple.JSONObject;

public class DataInstance {

    private JSONObject rawData;

    private Condition dry;
    private Condition wet;


    public DataInstance(JSONObject data) {
        this.rawData = data;
        build();
    }

    private void build () {
        // error handling
        if (rawData == null || rawData.size() < 1)
             System.err.println("No data available .. check your json file");

        // build data tree
        if (rawData.containsKey("dry"))
            dry = new Condition((JSONObject) rawData.get("dry"));
        if (rawData.containsKey("wet"))
            wet = new Condition((JSONObject) rawData.get("wet"));


    }

}
