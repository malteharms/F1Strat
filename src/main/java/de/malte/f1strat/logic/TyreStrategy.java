package de.malte.f1strat.logic;

import de.malte.f1strat.helper.ArrayHelper;
import de.malte.f1strat.structure.Compound;
import de.malte.f1strat.structure.Distance;
import de.malte.f1strat.structure.Track;

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
    Track stopTime;
    List<String> tireTypes;
    Map<String, Integer> tireCounts;
    List<List<String>> tireOrders;
    Map<Map<Integer, String>, Double> tireOrderWithTrackTime;


    public TyreStrategy(Distance track, Track stopTime) {

        // check if tyre data is available
        if (track.containsData()) {
            this.track = track;
            this.stopTime = stopTime;
        }else {
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

        tireOrderWithTrackTime = new HashMap<Map<Integer, String>, Double>();

        // generate tyre orders
        for (int i = 0; i < MAX_STOPS - 1; i++) {
            List<List<String>> cur = generateTireOrders(MAX_STOPS - i, tireTypes, tireCounts);
            tireOrders.addAll(cur);
        }

        // delete not allowed strategy's
        deleteNotAllowed();

        // every tyre order has multiple possibilities for in-laps
        // next step is to look which possibility is the fastest for each tyre order
        calculateFastestStrategy();
    }

    private void calculateFastestStrategy() {

        for (List<String> tireOrder : tireOrders) {

            switch (tireOrder.size()) {
                case 2 -> fastestStrategyTwoCompounds(tireOrder);
                case 3 -> fastestStrategyThreeCompounds(tireOrder);
                case 4 -> fastestStrategyFourCompounds(tireOrder);
                case 5 -> fastestStrategyFiveCompounds(tireOrder);
            }
        }
    }

    public Map<Map<Integer, String>, Double> getTireOrderWithTrackTime() {
        return tireOrderWithTrackTime;
    }

    private Compound getCompound(String s) {
        return switch (s) {
            case SOFT -> track.soft;
            case MEDIUM -> track.medium;
            default -> track.hard;
        };
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

    public void computeFastestStrategy(List<String> tireOrder) {
        Map<Integer, String> tireWithInLap = new HashMap<>();
        Compound[] compounds = new Compound[tireOrder.size()];
        int[] inLapCnt = new int[tireOrder.size()];
        int[] currentFastestInLap = new int[tireOrder.size()];
        double fastestTime = Double.MAX_VALUE;

        for (int index = 0; index < tireOrder.size(); index++) {
            compounds[index] = getCompound(tireOrder.get(index));
            currentFastestInLap[index] = index + 1;
            inLapCnt[index] = index + 1;
        }

        while(true) {
            // does the distance can be solved
            if (ArrayHelper.sumArray( ArrayHelper.clipArray(inLapCnt, 0, inLapCnt.length - 1) ) +
                    compounds[compounds.length - 1].getMax() > stopTime.getStopTime()) {

                double currentTrackTime = computeTrackTime(compounds, inLapCnt);
                if (fastestTime > currentTrackTime) {
                    fastestTime = currentTrackTime;
                    System.arraycopy(inLapCnt, 0, currentFastestInLap, 0, currentFastestInLap.length);
                }

            }

            // update inLapCnt to next position
            inLapCnt[inLapCnt.length - 1]++;
            if (inLapCnt[inLapCnt.length - 1] > compounds[compounds.length - 1].getMax()) {
                // tbd
            }
            break;
        }

        if (fastestTime < Double.MAX_VALUE) {
            for (int index = 0; index < tireOrder.size(); index++)
                tireWithInLap.put(currentFastestInLap[index], tireOrder.get(index));

            tireOrderWithTrackTime.put(tireWithInLap, fastestTime);
        }

    }


    private double computeTrackTime(Compound[] compounds, int[] inLaps) {
        double trackTime = 0.0;

        for (int index = 0; index < compounds.length; index++)
            trackTime += compounds[index].getTrackTimes()[inLaps[index]];

        return trackTime;

    }


    private void fastestStrategyTwoCompounds(List<String> tireOrder) {
        Compound c1 = getCompound(tireOrder.get(0));
        Compound c2 = getCompound(tireOrder.get(1));
        double fastestTime = Double.MAX_VALUE;
        int curIn1 = 1;
        int curIn2 = 2;
        Map<Integer, String> tireWithInLap = new HashMap<Integer, String>();

        for (int in1 = 1; in1 < c1.getMax() - 1; in1++) {
            for (int in2 = in1 + 1; in2 < c2.getMax(); in2++) {
                if (in1 + c2.getMax() >= track.getLaps()) {
                    double cur = c1.getTrackTimes()[in1] + c2.getTrackTimes()[in2] + (stopTime.getStopTime() * 1);
                    if (fastestTime > cur) {
                        fastestTime = cur;
                        curIn1 = in1;
                        curIn2 = in2;
                    }
                }
            }
        }
        if (fastestTime != Double.MAX_VALUE) {
            tireWithInLap.put(curIn1, tireOrder.get(0));
            tireWithInLap.put(curIn2, tireOrder.get(1));

            tireOrderWithTrackTime.put(tireWithInLap, fastestTime);
        }
    }

    private void fastestStrategyThreeCompounds(List<String> tireOrder) {
        Compound c1 = getCompound(tireOrder.get(0));
        Compound c2 = getCompound(tireOrder.get(1));
        Compound c3 = getCompound(tireOrder.get(2));
        double fastestTime = Double.MAX_VALUE;
        int curIn1 = 1;
        int curIn2 = 2;
        int curIn3 = 3;
        Map<Integer, String> tireWithInLap = new HashMap<Integer, String>();

        for (int in1 = 1; in1 < c1.getMax() - 1; in1++) {
            for (int in2 = in1 + 1; in2 < c2.getMax(); in2++) {
                for (int in3 = in2 + 1; in3 < c3.getMax(); in3++) {
                    if (in1 + in2 + c3.getMax() >= track.getLaps()) {
                        double cur = c1.getTrackTimes()[in1] + c2.getTrackTimes()[in2] + c3.getTrackTimes()[in3]
                                + (stopTime.getStopTime() * 2);
                        if (fastestTime > cur) {
                            fastestTime = cur;
                            curIn1 = in1;
                            curIn2 = in2;
                            curIn3 = in3;
                        }
                    }
                }
            }
        }
        if (fastestTime != Double.MAX_VALUE) {
            tireWithInLap.put(curIn1, tireOrder.get(0));
            tireWithInLap.put(curIn2, tireOrder.get(1));
            tireWithInLap.put(curIn3, tireOrder.get(2));

            tireOrderWithTrackTime.put(tireWithInLap, fastestTime);
        }
    }

    private void fastestStrategyFourCompounds(List<String> tireOrder) {
        Compound c1 = getCompound(tireOrder.get(0));
        Compound c2 = getCompound(tireOrder.get(1));
        Compound c3 = getCompound(tireOrder.get(2));
        Compound c4 = getCompound(tireOrder.get(3));
        double fastestTime = Double.MAX_VALUE;
        int curIn1 = 1;
        int curIn2 = 2;
        int curIn3 = 3;
        int curIn4 = 4;
        Map<Integer, String> tireWithInLap = new HashMap<Integer, String>();

        for (int in1 = 1; in1 < c1.getMax() - 1; in1++) {
            for (int in2 = in1 + 1; in2 < c2.getMax(); in2++) {
                for (int in3 = in2 + 1; in3 < c3.getMax(); in3++) {
                    for (int in4 = in3 + 1; in4 < c4.getMax(); in4++) {
                        if (in1 + in2 + c3.getMax() >= track.getLaps()) {
                            double cur = c1.getTrackTimes()[in1] + c2.getTrackTimes()[in2] +
                                    c3.getTrackTimes()[in3] + c4.getTrackTimes()[in4] + (stopTime.getStopTime() * 3);
                            if (fastestTime > cur) {
                                fastestTime = cur;
                                curIn1 = in1;
                                curIn2 = in2;
                                curIn3 = in3;
                                curIn4 = in4;
                            }
                        }
                    }
                }
            }
        }
        if (fastestTime != Double.MAX_VALUE) {
            tireWithInLap.put(curIn1, tireOrder.get(0));
            tireWithInLap.put(curIn2, tireOrder.get(1));
            tireWithInLap.put(curIn3, tireOrder.get(2));
            tireWithInLap.put(curIn4, tireOrder.get(3));

            tireOrderWithTrackTime.put(tireWithInLap, fastestTime);
        }
    }

    private void fastestStrategyFiveCompounds(List<String> tireOrder) {
        Compound c1 = getCompound(tireOrder.get(0));
        Compound c2 = getCompound(tireOrder.get(1));
        Compound c3 = getCompound(tireOrder.get(2));
        Compound c4 = getCompound(tireOrder.get(3));
        Compound c5 = getCompound(tireOrder.get(4));
        double fastestTime = Double.MAX_VALUE;
        int curIn1 = 1;
        int curIn2 = 2;
        int curIn3 = 3;
        int curIn4 = 4;
        int curIn5 = 5;
        Map<Integer, String> tireWithInLap = new HashMap<Integer, String>();

        for (int in1 = 1; in1 < c1.getMax() - 1; in1++) {
            for (int in2 = in1 + 1; in2 < c2.getMax(); in2++) {
                for (int in3 = in2 + 1; in3 < c3.getMax(); in3++) {
                    for (int in4 = in3 + 1; in4 < c4.getMax(); in4++) {
                        for (int in5 = in4 + 1; in5 < c5.getMax(); in5++) {
                            if (in1 + in2 + c3.getMax() >= track.getLaps()) {
                                double cur = c1.getTrackTimes()[in1] + c2.getTrackTimes()[in2] +
                                        c3.getTrackTimes()[in3] + c4.getTrackTimes()[in4] + c5.getTrackTimes()[in5]
                                        + (stopTime.getStopTime() * 4);
                                if (fastestTime > cur) {
                                    fastestTime = cur;
                                    curIn1 = in1;
                                    curIn2 = in2;
                                    curIn3 = in3;
                                    curIn4 = in4;
                                    curIn5 = in5;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (fastestTime != Double.MAX_VALUE) {
            tireWithInLap.put(curIn1, tireOrder.get(0));
            tireWithInLap.put(curIn2, tireOrder.get(1));
            tireWithInLap.put(curIn3, tireOrder.get(2));
            tireWithInLap.put(curIn4, tireOrder.get(3));
            tireWithInLap.put(curIn5, tireOrder.get(4));

            tireOrderWithTrackTime.put(tireWithInLap, fastestTime);
        }
    }


}
