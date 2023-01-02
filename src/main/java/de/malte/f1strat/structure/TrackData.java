package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class TrackData extends Structure {

    public Distance twentyFifty;
    public Distance hundred;

    public TrackData(JSONObject trackData) {

        if (trackData.containsKey("twentyFifty"))
            twentyFifty = new Distance((JSONObject) trackData.get("twentyFifty"));

        if (trackData.containsKey("hundred"))
            hundred = new Distance((JSONObject) trackData.get("hundred"));

    }
}
