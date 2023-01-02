package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class Condition extends Structure {

    public Settings settings;
    public Tracks tracks;

    public Condition(JSONObject con) {

        String SETTINGS = "settings";
        if (con.containsKey(SETTINGS))
            settings = new Settings((JSONObject) con.get(SETTINGS));

        String TRACKS = "tracks";
        if (con.containsKey(TRACKS))
            tracks = new Tracks((JSONObject) con.get(TRACKS));
    }



}
