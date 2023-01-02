package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class Settings extends Structure {

    private final String steeringAssistant;
    private final String breakAssistant;
    private final String abs;
    private final String tractionControl;
    private final String ersMission;
    private final String drsMission;

    public Settings (JSONObject settings) {
        this.steeringAssistant = (String) settings.get("steeringAssistant");
        this.breakAssistant = (String) settings.get("breakAssistant");
        this.abs = (String) settings.get("breakAssistant");
        this.tractionControl = (String) settings.get("tractionControl");
        this.ersMission = (String) settings.get("ersMission");
        this.drsMission = (String) settings.get("drsMission");
    }

    public String getSteeringAssistant() {
        return steeringAssistant;
    }

    public String getBreakAssistant() {
        return breakAssistant;
    }

    public String getAbs() {
        return abs;
    }

    public String getTractionControl() {
        return tractionControl;
    }

    public String getErsMission() {
        return ersMission;
    }

    public String getDrsMission() {
        return drsMission;
    }

}
