package de.malte.f1strat.structure;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Setup extends Structure {

    private final String team;
    private final long[] aerodynamics;
    private final long[] transmission;
    private final double[] suspensionGeometry;
    private final long[] suspension;
    private final long[] breaks;
    private final double[] tyres;

    public Setup (JSONObject setup) {

        this.team = (String) setup.get("team");
        JSONArray aero = (JSONArray) setup.get("aerodynamics");
        JSONArray trans = (JSONArray) setup.get("transmission");
        JSONArray susGeo = (JSONArray) setup.get("suspensionGeometry");
        JSONArray sus = (JSONArray) setup.get("transmission");
        JSONArray bre = (JSONArray) setup.get("breaks");
        JSONArray tyr = (JSONArray) setup.get("tyres");

        this.aerodynamics = objectArrToIntArr(aero.toArray());
        this.transmission = objectArrToIntArr(trans.toArray());
        this.suspensionGeometry = objectArrToDoubleArr(susGeo.toArray());
        this.suspension = objectArrToIntArr(sus.toArray());
        this.breaks = objectArrToIntArr(bre.toArray());
        this.tyres = objectArrToDoubleArr(tyr.toArray());
    }

    public String getTeam() {
        return team;
    }

    public long[] getAerodynamics() {
        return aerodynamics;
    }

    public long[] getTransmission() {
        return transmission;
    }

    public double[] getSuspensionGeometry() {
        return suspensionGeometry;
    }

    public long[] getSuspension() {
        return suspension;
    }

    public long[] getBreaks() {
        return breaks;
    }

    public double[] getTyres() {
        return tyres;
    }
}
