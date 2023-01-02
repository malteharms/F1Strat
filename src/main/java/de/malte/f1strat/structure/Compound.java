package de.malte.f1strat.structure;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Compound extends Structure {

    private final long perLap;
    private final long max;
    private final String ten;
    private final String thirty;
    private final String sixty;
    private final String eighty;
    private final String[] lapTime;

    public Compound(JSONObject compound) {

        this.perLap = (long) compound.get("perLap");
        this.max = (long) compound.get("max");
        this.ten = (String) compound.get("ten");
        this.thirty = (String) compound.get("thirty");
        this.sixty = (String) compound.get("eighty");
        this.eighty = (String) compound.get("eighty");
        JSONArray ltm = (JSONArray) compound.get("lapTime");

        this.lapTime = objectArrToStringArr(ltm.toArray());
    }

    public long getPerLap() {
        return perLap;
    }

    public long getMax() {
        return max;
    }

    public String getTen() {
        return ten;
    }

    public String getThirty() {
        return thirty;
    }

    public String getSixty() {
        return sixty;
    }

    public String getEighty() {
        return eighty;
    }

    public String[] getLapTime() {
        return lapTime;
    }
}
