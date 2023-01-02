package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class Distance extends Structure {

    private final double fuelLoad;
    private final long laps;
    public Compound soft;
    public Compound medium;
    public Compound hard;

    public Distance(JSONObject distance) {

        fuelLoad = (double) distance.get("fuelLoad");
        laps = (long) distance.get("laps");

        if (distance.containsKey("soft"))
            soft = new Compound((JSONObject) distance.get("soft"));

        if (distance.containsKey("medium"))
            medium = new Compound((JSONObject) distance.get("medium"));

        if (distance.containsKey("hard"))
            hard = new Compound((JSONObject) distance.get("hard"));
    }

    public double getFuelLoad() {
        return fuelLoad;
    }

    public long getLaps() {
        return laps;
    }



}
