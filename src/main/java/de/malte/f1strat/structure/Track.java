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

    public boolean containsData() {

        if (stopTime > 0)
            return true;
        return false;

    }

    private boolean isSetupComplete () {
        if (setup.getAerodynamics().length == 2 &&
            setup.getTransmission().length == 2 &&
            setup.getSuspensionGeometry().length == 4 &&
            setup.getSuspension().length == 6 &&
            setup.getBreaks().length == 2 &&
            setup.getTyres().length == 4 &&
            setup.getTeam() != null)
            return true;
        return false;
    }

}
