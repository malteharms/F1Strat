import de.malte.f1strat.helper.StrategyHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTyreStrategy {

    @Test
    public void testGenerateTyreOrders() {
        StrategyHelper strat = new StrategyHelper();
        List<List<String>> result = new ArrayList<>();

        int MAX_STOPS = 1;
        List<String> tireTypes = List.of("Soft", "Medium");
        Map<String, Integer> tireCounts = new HashMap<>();

        tireCounts.put("Soft", 2);
        tireCounts.put("Medium", 2);

        List<String> first = new ArrayList<>();
        first.add("Soft");
        first.add("Medium");

        List<String> sec = new ArrayList<>();
        sec.add("Medium");
        sec.add("Soft");

        result.add(first);
        result.add(sec);

        assertEquals(result, strat.generateTireOrders(MAX_STOPS, tireTypes, tireCounts));
    }




}
