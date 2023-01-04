package de.malte.f1strat.logic;

import de.malte.f1strat.structure.Distance;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class TyreStrategy {

    final int AMOUNT_SOFT = 3;
    final int AMOUNT_MEDIUM = 2;
    final int AMOUNT_HARD = 1;
    final int MAX_STOPS = 5;

    final String SOFT = "Soft";
    final String MEDIUM = "Medium";
    final String HARD = "Hard";

    Distance track;
    List<String> tireTypes;
    Map<String, Integer> tireCounts;
    List<List<String>> tireOrders;


    public TyreStrategy(Distance track) {

        // check if tyre data is available
        if (track.containsData())
            this.track = track;
        else {
            System.err.println(TyreStrategy.class.getName() + ": There is no data in this Object.");
            return;
        }

        // initialize objects
        tireOrders = new ArrayList<>();
        tireTypes = List.of(SOFT, MEDIUM, HARD);

        tireCounts = new HashMap<>();
        tireCounts.put(SOFT, AMOUNT_SOFT);
        tireCounts.put(MEDIUM, AMOUNT_MEDIUM);
        tireCounts.put(HARD, AMOUNT_HARD);

        // generate tyre orders
        for (int i = 0; i < MAX_STOPS - 1; i++) {
            List<List<String>> cur = generateTireOrders(MAX_STOPS - i, tireTypes, tireCounts);
            tireOrders.addAll(cur);
        }

        // delete not allowed strategy's
        deleteNotAllowed();

        // every tyre order has multiple possibilities for in-laps
        // next step is to look which possibility is the fastest for each tyre order

    }

    private List<List<String>> generateTireOrders(int numLaps, List<String> tireTypes, Map<String, Integer> tireCounts) {
        List<List<String>> tireOrders = new ArrayList<>();
        if (numLaps == 0) {
            // If all laps have tires, return the current tire order
            tireOrders.add(new ArrayList<>());
            return tireOrders;
        }

        // Iterate over all available tire types and call generateTireOrders recursively
        // if there are still enough tires of the corresponding type available
        for (String tireType : tireTypes) {
            if (tireCounts.get(tireType) > 0) {
                tireCounts.put(tireType, tireCounts.get(tireType) - 1);
                List<List<String>> orders = generateTireOrders(numLaps - 1, tireTypes, tireCounts);
                for (List<String> order : orders) {
                    order.add(0, tireType);
                }
                tireOrders.addAll(orders);
                tireCounts.put(tireType, tireCounts.get(tireType) + 1);
            }
        }
        return tireOrders;
    }

    private String[] getSummedLapTimesSoft() {
        String[] lapTimeSoft = track.soft.getLapTime();
        String[] trackTimeSoft = new String[track.soft.getLapTime().length];
        for (int age = 0; age < max(track.soft.getMax()); age++)
            trackTimeSoft[age] = calculateLapTimes(lapTimeSoft, age);

        return trackTimeSoft;
    }

    private String[] getSummedLapTimesMedium() {
        String[] lapTimeMedium = track.medium.getLapTime();
        String[] trackTimeMedium = new String[track.medium.getLapTime().length];
        for (int age = 0; age < max(track.medium.getMax()); age++)
            trackTimeMedium[age] = calculateLapTimes(lapTimeMedium, age);

        return trackTimeMedium;
    }

    private String[] getSummedLapTimesHard() {
        String[] lapTimeHard = track.hard.getLapTime();
        String[] trackTimeHard = new String[track.hard.getLapTime().length];
        for (int age = 0; age < max(track.hard.getMax()); age++)
            trackTimeHard[age] = calculateLapTimes(lapTimeHard, age);

        return trackTimeHard;
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

        if (mil > 999) {    // reset milliseconds
            sec += mil / 999;
            mil = mil % 999;
        }
        if (sec > 59) {     // reset seconds
            min += sec / 59;
            sec = sec % 59;
        }
        // build string
        return min + ":" + sec + "." + mil;
    }

    private int max(long age) {
        if (age > track.getLaps())
                return (int) track.getLaps() - 1;
        return (int) age - 1;
    }

    private void deleteNotAllowed() {
        List<List<String>> toDelete = new ArrayList<>();
        for (List<String> order : tireOrders) {
            /* orders with just one compound */
            int differentTyres = 0;
            Set<String> used = new HashSet<>();
            for (String s : order) {
                if (!used.contains(s))
                    differentTyres++;
                used.add(s);
            }

            if (differentTyres <= 1)
                toDelete.add(order);
            else {
                /* orders which are too small for track distance */
                int total = 0;
                for(String compound : order) {
                    if (compound.equals(SOFT))
                        total += track.soft.getMax();
                    else if (compound.equals(MEDIUM))
                        total += track.medium.getMax();
                    else
                        total += track.hard.getMax();
                }
                if (total < track.getLaps())
                    toDelete.add(order);
            }
        }
        for (List<String> td : toDelete)
            tireOrders.remove(td);
    }

}
