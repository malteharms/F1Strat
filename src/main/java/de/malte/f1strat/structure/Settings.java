package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class Settings {

    private String steeringAssistant;
    private String breakAssistant;
    private String abs;
    private String tractionControl;
    private String ersMission;
    private String drsMission;

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

    public void setSteeringAssistant(String steeringAssistant) {
        this.steeringAssistant = steeringAssistant;
    }

    public String getBreakAssistant() {
        return breakAssistant;
    }

    public void setBreakAssistant(String breakAssistant) {
        this.breakAssistant = breakAssistant;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getTractionControl() {
        return tractionControl;
    }

    public void setTractionControl(String tractionControl) {
        this.tractionControl = tractionControl;
    }

    public String getErsMission() {
        return ersMission;
    }

    public void setErsMission(String ersMission) {
        this.ersMission = ersMission;
    }

    public String getDrsMission() {
        return drsMission;
    }

    public void setDrsMission(String drsMission) {
        this.drsMission = drsMission;
    }


}
