package de.malte.f1strat.helper;

public class Converter {

    public static double lapTimeStringToDouble (String lapTime) {
        int min = 0, sec = 0, mil = 0;

        String[] minCutSek = lapTime.split(":");
        min += Integer.parseInt(minCutSek[0]);

        String[] sekCutMil = minCutSek[1].split("\\.");
        sec += Integer.parseInt(sekCutMil[0]);
        mil += Integer.parseInt(sekCutMil[1]);


        if (mil > 999) {    // reset milliseconds
            sec += mil / 999;
            mil = mil % 999;
        }
        if (sec > 59) {     // reset seconds
            min += sec / 59;
            sec = sec % 59;
        }

        return (double) (min * 60) + sec + (mil * 0.001);
    }

}
