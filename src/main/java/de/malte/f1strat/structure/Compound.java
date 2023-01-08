package de.malte.f1strat.structure;

import de.malte.f1strat.helper.Converter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Compound extends Structure {

    private final int perLap;
    private final int max;
    private final String ten;
    private final String thirty;
    private final String sixty;
    private final String eighty;
    private final String[] lapTime;

    public Compound(JSONObject compound) {

        long pl = (long) compound.get("perLap");
        this.perLap = (int) pl;

        long m = (long) compound.get("max");
        max = (int) m;

        this.ten = (String) compound.get("ten");
        this.thirty = (String) compound.get("thirty");
        this.sixty = (String) compound.get("eighty");
        this.eighty = (String) compound.get("eighty");
        JSONArray ltm = (JSONArray) compound.get("lapTime");

        this.lapTime = objectArrToStringArr(ltm.toArray());
    }

    public int getPerLap() {
        return perLap;
    }

    public int getMax() {
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

    public Double[] getTrackTimes() {
        Double[] res = new Double[lapTime.length];
        double summed = 0.0;
        for (int i = 0; i < lapTime.length; i++) {
            summed += Converter.lapTimeStringToDouble(getLapTime()[i]);
            res[i] = summed;
        }
        return res;
    }
}
