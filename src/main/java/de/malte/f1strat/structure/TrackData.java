package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class TrackData extends Structure {

    public Distance twentyFifty;
    public Distance hundred;

    public TrackData(JSONObject trackData) {

        String TWENTYFIFTY = "twentyFifty";
        if (trackData.containsKey(TWENTYFIFTY))
            twentyFifty = new Distance((JSONObject) trackData.get(TWENTYFIFTY));

        String HUNDRED = "hundred";
        if (trackData.containsKey(HUNDRED))
            hundred = new Distance((JSONObject) trackData.get(HUNDRED));

    }
}
