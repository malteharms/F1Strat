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
        } else {
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

        tireOrderWithTrackTime = new HashMap<>();

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
            Strategy fastestStrategy = computeFastestStrategy(tireOrder);

            if (fastestStrategy != null)
                System.out.println(fastestStrategy);
        }
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
                for (String compound : order) {
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

    public Strategy computeFastestStrategy(List<String> tireOrder) {
        // in lap with tyre type, the return value
        // example: (1, medium) (5, soft)
        Map<Integer, String> tireWithInLap = new HashMap<>();

        // the object for each tyre string
        // example: (Compound medium, Compound soft)
        Compound[] compounds = new Compound[tireOrder.size()];

        // an array which stores the current in lap order
        // example: (1, 5)
        int[] inLapCnt = new int[tireOrder.size()];

        // an array to store the fastest in lap order
        // example: (1, 5)
        int[] currentFastestInLap = new int[tireOrder.size()];

        // the fastest track time for an order
        double fastestTime = Double.MAX_VALUE;

        // array setup
        for (int index = 0; index < tireOrder.size(); index++) {
            compounds[index] = getCompound(tireOrder.get(index));
            currentFastestInLap[index] = index + 1;
            inLapCnt[index] = index + 1;
        }

        // analyse the tyre order for each in lap combination
        boolean isFinished = false;
        while (!isFinished) {

            // does the distance can be solved
            if (getCurrentDistanceForInLapOrder(inLapCnt, compounds) > track.getLaps() && isMakeable(inLapCnt, compounds)) {

                // check, if the current track time is faster than the current fastest order
                double currentTrackTime = computeTrackTime(compounds, inLapCnt);
                if (fastestTime > currentTrackTime) {
                    fastestTime = currentTrackTime;
                    System.arraycopy(inLapCnt, 0, currentFastestInLap, 0, currentFastestInLap.length);
                }

            }

            // update inLapCnt
            inLapCnt = updateInLapCnt(inLapCnt, compounds);
            if (!hasMorePossibilities(inLapCnt))
                isFinished = true;
        }

        // set tireWithInLap
        for (int index = 0; index < currentFastestInLap.length; index++) {
            if (fastestTime < Double.MAX_VALUE)
                tireWithInLap.put(currentFastestInLap[index], tireOrder.get(index));
        }

        if (fastestTime < Double.MAX_VALUE)
            return new Strategy(tireWithInLap, fastestTime);
        return null;
    }

    private boolean hasMorePossibilities(int[] inLapCnt) { // [1, 3] ... maxLaps=29
        int lastElement = inLapCnt.length - 1;

        if (inLapCnt[lastElement] == track.getLaps() - 1) {
            for (int index = lastElement - 1; index >= 0; index--) {
                if (!(inLapCnt[index] == inLapCnt[index + 1] - 1)) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }


    private int[] updateInLapCnt(int[] inLapCnt, Compound[] compounds) {  // [1,2] ; [medium, soft]
        int lastElement = inLapCnt.length - 1;

        if (inLapCnt[lastElement] >= track.getLaps() - 1) { //[1, 7, 8, 9]

            for (int indexL = lastElement - 1; indexL >= 0; indexL--) {
                if (!(inLapCnt[indexL] == inLapCnt[indexL + 1] - 1)) {  // if not one element equals next element - 1
                    inLapCnt[indexL]++;

                    // set inLaps after to correct position
                    for (int indexF = indexL + 1; indexF < inLapCnt.length; indexF++)
                        inLapCnt[indexF] = inLapCnt[indexF - 1] + 1;
                    break;
                }
            }

        } else
            inLapCnt[lastElement]++;

        // is this strategy okay? Can every tyre solve the given distance?
        if (hasMorePossibilities(inLapCnt)) {
            if (!isMakeable(inLapCnt, compounds))
                inLapCnt = updateInLapCnt(inLapCnt, compounds);
        }


        return inLapCnt;
    }

    private boolean isMakeable(int[] inLapCnt, Compound[] compounds) {

        for (int index = 0; index < inLapCnt.length - 1; index++) {
            int lapsToSolve;
            if (index == 0)
                lapsToSolve = inLapCnt[index];
            else
                lapsToSolve = inLapCnt[index + 1] - inLapCnt[index];

            if (compounds[index].getMax() < lapsToSolve)
                return false;
        }
        // check for the last tyre set
        int lastTyre = compounds.length - 1;
        return compounds[lastTyre].getMax() >= track.getLaps() - inLapCnt[lastTyre];
    }


    private int getCurrentDistanceForInLapOrder(int[] inLapCnt, Compound[] compounds) {
        return ArrayHelper.sumArray(ArrayHelper.clipArray(inLapCnt, 0, inLapCnt.length - 1)) +
                compounds[compounds.length - 1].getMax();
    }


    private double computeTrackTime(Compound[] compounds, int[] inLaps) {
        double trackTime = 0.0;
        int lastElement = compounds.length - 1;

        for (int index = 0; index < compounds.length; index++) {
            Double[] trackTimes = compounds[index].getTrackTimes();

            if (index == 0)
                trackTime += trackTimes[0];
            else if (index == lastElement)
                trackTime += trackTimes[track.getLaps() - inLaps[index]];
            else
                trackTime += trackTimes[inLaps[index + 1] - inLaps[index]];
        }

        return trackTime;
    }
}

class Strategy {
    Map<Integer, String> inLapWithTyre;
    Double trackTime;

    Strategy (Map<Integer, String> inLapWithTyre, Double trackTime) {
        this.inLapWithTyre = inLapWithTyre;
        this.trackTime = trackTime;
    }

    public String toString() {
        String s = "-----------------------\n";
        s += "InLapWithTire: " + inLapWithTyre + "\n";
        s += "TrackTime: " + trackTime + "\n";
        s += "-----------------------";

        return s;
    }
}
