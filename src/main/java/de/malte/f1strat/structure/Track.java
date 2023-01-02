package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class Track extends Structure {

    private final double stopTime;
    public Setup setup;
    public TrackData trackData;

    public Track (JSONObject track) {

        this.stopTime = (double) track.get("stopTime");

        if (track.containsKey("setup"))
            setup = new Setup((JSONObject) track.get("setup"));

        if (track.containsKey("trackData"))
            trackData = new TrackData((JSONObject) track.get("trackData"));

    }

    public double getStopTime() {
        return stopTime;
    }

}
