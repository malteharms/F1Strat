package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class Track extends Structure {

    private final double stopTime;
    public Setup setup;
    public TrackData trackData;

    public Track (JSONObject track) {

        this.stopTime = (double) track.get("stopTime");

        String SETUP = "setup";
        if (track.containsKey(SETUP))
            setup = new Setup((JSONObject) track.get(SETUP));

        String TRACKDATA = "trackData";
        if (track.containsKey(TRACKDATA))
            trackData = new TrackData((JSONObject) track.get(TRACKDATA));

    }

    public double getStopTime() {
        return stopTime;
    }


}
