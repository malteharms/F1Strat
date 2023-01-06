package de.malte.f1strat;

import de.malte.f1strat.handler.JsonHandler;
import de.malte.f1strat.helper.Converter;
import de.malte.f1strat.structure.*;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.util.HashMap;
import java.util.Map;

public class StartController {

    final int LINE_CHART_MARGIN = 1;

    int xMin = 0;
    int xMax = Integer.MIN_VALUE;
    int yMin = Integer.MAX_VALUE;
    int yMax = Integer.MIN_VALUE;

    final String LINECHART_TITLE = "Rundenzeiten pro Reifenmischung";
    final String SOFT = "Soft";
    final String MEDIUM = "Medium";
    final String HARD = "Hart";

    final String locationOfData = "f1-data.json";

    DataInstance data = new JsonHandler().loadData(locationOfData);

    @FXML
    public ChoiceBox<String> cbDistance;
    public ChoiceBox<String> cbTrack;
    public ChoiceBox<String> cbCondition;

    @FXML
    private Button loadData;

    @FXML
    private LineChart<Number, Number> lcLapTimes;
    public NumberAxis xAxis;
    public NumberAxis yAxis;

    @FXML
    public void initialize() {
        loadData.setOnAction(actionEvent -> {

            lcLapTimes.getData().clear();

            String distance = cbDistance.getValue();
            String track = cbTrack.getValue();
            String condition = cbCondition.getValue();

            Distance neededData = getDistanceObject(data, distance, track, condition);
            setLineChart(getLapTimeMap(neededData.soft),
                    getLapTimeMap(neededData.medium), getLapTimeMap(neededData.hard));


            xAxis.setAutoRanging(false);
            yAxis.setAutoRanging(false);

            xAxis.setLowerBound( xMin );
            xAxis.setUpperBound( xMax + LINE_CHART_MARGIN );
            yAxis.setLowerBound( yMin - LINE_CHART_MARGIN );
            yAxis.setUpperBound( yMax + LINE_CHART_MARGIN );

        });
    }


    private void setLineChart(Map<Integer, Double> s, Map<Integer, Double> m, Map<Integer, Double> h) {
        XYChart.Series<Number, Number> soft = new XYChart.Series<>();
        XYChart.Series<Number, Number> medium = new XYChart.Series<>();
        XYChart.Series<Number, Number> hard = new XYChart.Series<>();

        lcLapTimes.getXAxis().setAutoRanging(false);
        lcLapTimes.getYAxis().setAutoRanging(false);

        lcLapTimes.setTitle(LINECHART_TITLE);
        soft.setName(SOFT);
        medium.setName(MEDIUM);
        hard.setName(HARD);

        // get soft lap time data
        for (int i = 0; i < s.size(); i++)
            soft.getData().add(new XYChart.Data<>(i + 1, s.get(i + 1)));

        lcLapTimes.getData().add(soft);

        // get medium lap time data
        for (int i = 0; i < m.size(); i++)
            medium.getData().add(new XYChart.Data<>( i + 1, m.get(i + 1)));
        lcLapTimes.getData().add(medium);

        // get hard lap time data
        for (int i = 0; i < h.size(); i++)
            hard.getData().add(new XYChart.Data<>( i + 1, h.get(i + 1)));
        lcLapTimes.getData().add(hard);

        System.out.println("XMAX: " + xMax + "\tYMAX: " + yMax + "\tYMIN: " + yMin);

    }


    private Map<Integer, Double> getLapTimeMap(Compound compound) {
        Map<Integer, Double> res = new HashMap<>();
        String[] lapTime = compound.getLapTime();

        for (int x = 0; x < lapTime.length; x++) {
            double y = Converter.lapTimeStringToDouble(lapTime[x]);
            res.put(x + 1, y);

            if (y < yMin)
                yMin = (int) y;
            else if (y > yMax)
                yMax = (int) y;

            if (x + 1 > xMax)
                xMax = x + 1;
        }

        return res;
    }


    private Distance getDistanceObject(DataInstance data, String distance, String track, String condition) {
        switch (condition) {
            case "Trocken":
                TrackData onDry = switchTrack(data.dry.tracks, track);
                switch (distance) {
                    case "100%":
                        return onDry.hundred;
                    default:
                        return onDry.fifty;
                }

            default:
                TrackData onWet = switchTrack(data.wet.tracks, track);
                switch (distance) {
                    case "100%":
                        return onWet.hundred;
                    default:
                        return onWet.fifty;
                }
        }
    }

    private TrackData switchTrack(Tracks data, String track) {
        Track whichTrack;

        switch (track) {
            case "Bahrain":
                whichTrack = data.bahrain;
                break;
            case "Jeddah":
                whichTrack = data.jeddah;
                break;
            case "Australien":
                whichTrack = data.australia;
                break;
            case "Imola":
                whichTrack = data.imola;
                break;
            case "Miami":
                whichTrack = data.miami;
                break;
            case "Spanien":
                whichTrack = data.spain;
                break;
            case "Monaco":
                whichTrack = data.monaco;
                break;
            case "Baku":
                whichTrack = data.baku;
                break;
            case "Kanada":
                whichTrack = data.canada;
                break;
            case "Silverstone":
                whichTrack = data.silverstone;
                break;
            case "Österreich":
                whichTrack = data.austria;
                break;
            case "Frankreich":
                whichTrack = data.france;
                break;
            case "Ungarn":
                whichTrack = data.hungary;
                break;
            case "Belgien":
                whichTrack = data.spa;
                break;
            case "Niederlande":
                whichTrack = data.dutch;
                break;
            case "Monza":
                whichTrack = data.monza;
                break;
            case "Singapur":
                whichTrack = data.singapore;
                break;
            case "Japan":
                whichTrack = data.japan;
                break;
            case "USA":
                whichTrack = data.usa;
                break;
            case "Mexiko":
                whichTrack = data.mexico;
                break;
            case "Brasilien":
                whichTrack = data.brazil;
                break;
            case "Abu Dhabi":
                whichTrack = data.abudhabi;
                break;
            case "China":
                whichTrack = data.china;
                break;
            default:
                whichTrack = data.portuguese;
        }

        return whichTrack.trackData;
    }
}