package de.malte.f1strat.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StrategyHelper {

    public List<List<String>> generateTireOrders(int numLaps, List<String> tireTypes, Map<String, Integer> tireCounts) {
        List<String> allTyres = new ArrayList<>();

        // create one list with (maybe duplicate) tire types
        for (String currentTire : tireTypes) {
            // add the amount of tires to the list
            for (int laps = 0; laps < tireCounts.get(currentTire); laps++)
                allTyres.add(currentTire);
        }

        // result so far: [soft, soft, medium, medium, hard]
        // now calculate every possible combination
        List<List<String>> tireOrders = new ArrayList<>();
        List<String> currentOrder = new ArrayList<>();

        for (String s : allTyres) {

            currentOrder.add(s);
            // how many tire sets will be added
            for (int times = 1; times < allTyres.size(); times++) {
                int cntAdded = 0;
                // which tire will be added
                for (String allTyre : allTyres) {
                    if (cntAdded < times) {
                        currentOrder.add(allTyre);
                        cntAdded++;
                    } else
                        break;
                }
            }
            tireOrders.add( currentOrder );
            currentOrder.clear();

        }
        return tireOrders;
    }

}
