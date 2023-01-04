package de.malte.f1strat;

import de.malte.f1strat.structure.Condition;
import de.malte.f1strat.structure.Structure;
import org.json.simple.JSONObject;

public class DataInstance extends Structure {

    public Condition dry;
    public Condition wet;

    public DataInstance(JSONObject data) {
        build(data);
    }

    private void build (JSONObject data) {
        // error handling
        if (data == null || data.size() < 1)
             System.err.println("No data available .. check your json file");

        String DRY = "dry";
        if (data.containsKey(DRY))
            dry = new Condition((JSONObject) data.get(DRY));

        String WET = "wet";
        if (data.containsKey(WET))
            wet = new Condition((JSONObject) data.get(WET));

    }
}
