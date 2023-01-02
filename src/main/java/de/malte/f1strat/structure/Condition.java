package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class Condition {

    private final String SETTINGS = "settings";
    private final String TRACKS = "tracks";

    private Settings settings;
    private Tracks tracks;

    public Condition(JSONObject con) {

        if (con.containsKey(SETTINGS))
            settings = new Settings((JSONObject) con.get(SETTINGS));

        if (con.containsKey(TRACKS))
            tracks = new Tracks((JSONObject) con.get(TRACKS));
    }



}
