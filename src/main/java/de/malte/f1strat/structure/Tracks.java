package de.malte.f1strat.structure;

import org.json.simple.JSONObject;

public class Tracks extends Structure {

    public Track bahrain;
    public Track jeddah;
    public Track australia;
    public Track imola;
    public Track miami;
    public Track spain;
    public Track monaco;
    public Track baku;
    public Track canada;
    public Track silverstone;
    public Track austria;
    public Track france;
    public Track hungary;
    public Track spa;
    public Track dutch;
    public Track monza;
    public Track singapore;
    public Track japan;
    public Track usa;
    public Track mexico;
    public Track brazil;
    public Track abudhabi;
    public Track china;
    public Track portuguese;

    public Tracks(JSONObject tracks) {

        String BAHRAIN = "bahrain";
        if (tracks.containsKey(BAHRAIN))
            bahrain = new Track((JSONObject) tracks.get(BAHRAIN));

        String JEDDAH = "jeddah";
        if (tracks.containsKey(JEDDAH))
            jeddah = new Track((JSONObject) tracks.get(JEDDAH));

        String AUSTRALIA = "australia";
        if (tracks.containsKey(AUSTRALIA))
            australia = new Track((JSONObject) tracks.get(AUSTRALIA));

        String IMOLA = "imola";
        if (tracks.containsKey(IMOLA))
            imola = new Track((JSONObject) tracks.get(IMOLA));

        String MIAMI = "miami";
        if (tracks.containsKey(MIAMI))
            miami = new Track((JSONObject) tracks.get(MIAMI));

        String SPAIN = "spain";
        if (tracks.containsKey(SPAIN))
            spain = new Track((JSONObject) tracks.get(SPAIN));

        String MONACO = "monaco";
        if (tracks.containsKey(MONACO))
            monaco = new Track((JSONObject) tracks.get(MONACO));

        String BAKU = "baku";
        if (tracks.containsKey(BAKU))
            baku = new Track((JSONObject) tracks.get(BAKU));

        String CANADA = "canada";
        if (tracks.containsKey(CANADA))
            canada = new Track((JSONObject) tracks.get(CANADA));

        String SILVERSTONE = "silverstone";
        if (tracks.containsKey(SILVERSTONE))
            silverstone = new Track((JSONObject) tracks.get(SILVERSTONE));

        String AUSTRIA = "austria";
        if (tracks.containsKey(AUSTRIA))
            austria = new Track((JSONObject) tracks.get(AUSTRIA));

        String FRANCE = "france";
        if (tracks.containsKey(FRANCE))
            france = new Track((JSONObject) tracks.get(FRANCE));

        String HUNGARY = "hungary";
        if (tracks.containsKey(HUNGARY))
            hungary = new Track((JSONObject) tracks.get(HUNGARY));

        String SPA = "spa";
        if (tracks.containsKey(SPA))
            spa = new Track((JSONObject) tracks.get(SPA));

        String DUTCH = "dutch";
        if (tracks.containsKey(DUTCH))
            dutch = new Track((JSONObject) tracks.get(DUTCH));

        String MONZA = "monza";
        if (tracks.containsKey(MONZA))
            monza = new Track((JSONObject) tracks.get(MONZA));

        String SINGAPORE = "singapore";
        if (tracks.containsKey(SINGAPORE))
            singapore = new Track((JSONObject) tracks.get(SINGAPORE));

        String JAPAN = "japan";
        if (tracks.containsKey(JAPAN))
            japan = new Track((JSONObject) tracks.get(JAPAN));

        String USA = "usa";
        if (tracks.containsKey(USA))
            usa = new Track((JSONObject) tracks.get(USA));

        String MEXICO = "mexico";
        if (tracks.containsKey(MEXICO))
            mexico = new Track((JSONObject) tracks.get(MEXICO));

        String BRAZIL = "brazil";
        if (tracks.containsKey(BRAZIL))
            brazil = new Track((JSONObject) tracks.get(BRAZIL));

        String ABUDHABI = "abudhabi";
        if (tracks.containsKey(ABUDHABI))
            abudhabi = new Track((JSONObject) tracks.get(ABUDHABI));

        String CHINA = "china";
        if (tracks.containsKey(CHINA))
            china = new Track((JSONObject) tracks.get(CHINA));

        String PORTUGUESE = "portuguese";
        if (tracks.containsKey(PORTUGUESE))
            portuguese = new Track((JSONObject) tracks.get(PORTUGUESE));
    }

}
