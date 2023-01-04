package de.malte.f1strat.logic;

import de.malte.f1strat.structure.Track;

import java.util.Arrays;

public class TyreStrategy {

    Track track;

    public TyreStrategy(Track track) {

        if (track.containsData())
            this.track = track;
        else
            System.err.println(TyreStrategy.class.getName() + ": There is no Data in this Object.");
    }

    public void calculateFastestStrategy50() {
        int amountSoftCompound = 3;
        int amountMediumCompound = 2;
        int amountHardCompound = 1;

        int maxPossibleStops = 1 + (amountSoftCompound - 1) +
                (amountMediumCompound - 1) + (amountHardCompound - 1);

        /*
            at first calculate all lap times of each compound for each age
        */

        // soft compound
        String[] lapTimeSoft = track.trackData.fifty.soft.getLapTime();
        String[] trackTimeSoft = new String[track.trackData.fifty.soft.getLapTime().length];
        for (int age = 0; age < max50(track.trackData.fifty.soft.getMax()); age++)
            trackTimeSoft[age] = calculateLapTimes(lapTimeSoft, age);

        // medium compound
        String[] lapTimeMedium = track.trackData.fifty.medium.getLapTime();
        String[] trackTimeMedium = new String[track.trackData.fifty.medium.getLapTime().length];
        for (int age = 0; age < max50(track.trackData.fifty.medium.getMax()); age++)
            trackTimeMedium[age] = calculateLapTimes(lapTimeMedium, age);

        //hard compound
        String[] lapTimeHard = track.trackData.fifty.hard.getLapTime();
        String[] trackTimeHard = new String[track.trackData.fifty.hard.getLapTime().length];
        for (int age = 0; age < max50(track.trackData.fifty.hard.getMax()); age++)
            trackTimeHard[age] = calculateLapTimes(lapTimeHard, age);

        System.out.println(Arrays.toString(trackTimeSoft));
        System.out.println(Arrays.toString(trackTimeMedium));
        System.out.println(Arrays.toString(trackTimeHard));

        // now calculate strategy

    }


    private String calculateLapTimes(String[] lapTime, int age) {
        int min = 0, sec = 0, mil = 0;

        for (int i = 0; i <= age; i++) {

            String[] minCutSek = lapTime[i].split(":");
            min += Integer.parseInt(minCutSek[0]);

            String[] sekCutMil = minCutSek[1].split("\\.");
            sec += Integer.parseInt(sekCutMil[0]);
            mil += Integer.parseInt(sekCutMil[1]);

        }

        if (mil > 999) {    // reset miliseconds
            sec += mil / 999;
            mil = mil % 999;
        }
        if (sec > 59) {     // reset seconds
            min += sec / 59;
            sec = sec % 59;
        }
        // build string
        return Integer.toString(min) + ":" + Integer.toString(sec) + "." + Integer.toString(mil);
    }

    private int max50(long age) {
        if (age > track.trackData.fifty.getLaps())
                return (int) track.trackData.fifty.getLaps() - 1;
        return (int) age - 1;
    }

}
