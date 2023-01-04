package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class TrackData extends Structure {

    public Distance fifty;
    public Distance hundred;

    public TrackData(JSONObject trackData) {

        String FIFTY = "fifty";
        if (trackData.containsKey(FIFTY))
            fifty = new Distance((JSONObject) trackData.get(FIFTY));

        String HUNDRED = "hundred";
        if (trackData.containsKey(HUNDRED))
            hundred = new Distance((JSONObject) trackData.get(HUNDRED));

    }
}
