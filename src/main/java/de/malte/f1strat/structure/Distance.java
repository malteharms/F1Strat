package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class Distance extends Structure {

    private final double fuelLoad;
    private final int laps;
    public Compound soft;
    public Compound medium;
    public Compound hard;

    public Distance(JSONObject distance) {

        fuelLoad = (double) distance.get("fuelLoad");
        long l = (long) distance.get("laps");
        laps = (int) l;

        String SOFT = "soft";
        if (distance.containsKey(SOFT))
            soft = new Compound((JSONObject) distance.get(SOFT));

        String MEDIUM = "medium";
        if (distance.containsKey(MEDIUM))
            medium = new Compound((JSONObject) distance.get(MEDIUM));

        String HARD = "hard";
        if (distance.containsKey(HARD))
            hard = new Compound((JSONObject) distance.get(HARD));
    }

    public double getFuelLoad() {
        return fuelLoad;
    }

    public int getLaps() {
        return laps;
    }

    public boolean containsData() {
        if (laps != 0 && soft != null && medium != null && hard != null)
            return true;
        return false;
    }



}
