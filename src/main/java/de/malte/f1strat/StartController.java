package de.malte.f1strat;

import de.malte.f1strat.handler.JsonHandler;
import de.malte.f1strat.helper.Converter;
import de.malte.f1strat.structure.*;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

import java.util.HashMap;
import java.util.Map;

public class StartController {

    final double LINE_CHART_MARGIN_TOP = 0.6;
    final double LINE_CHART_MARGIN_RIGHT = 0.2;
    final double LINE_CHART_MARGIN_BOTTOM = 0.1;


    int xMin = 0;
    int xMax = Integer.MIN_VALUE;
    double yMin = Double.MAX_VALUE;
    double yMax = Double.MIN_VALUE;

    final String LINE_CHART_TITLE = "Rundenzeiten pro Reifenmischung";
    final String SOFT = "Soft";
    final String MEDIUM = "Medium";
    final String HARD = "Hart";

    final String locationOfData = "f1-data.json";
    DataInstance data = new JsonHandler().loadData(locationOfData);

    @FXML
    public ChoiceBox<String> cbDistance;
    public ChoiceBox<String> cbTrack;
    public ChoiceBox<String> cbCondition;
    public CheckBox cbSoft;
    public CheckBox cbMedium;
    public CheckBox cbHard;

    @FXML
    private Button loadData;

    @FXML
    private LineChart<Number, Number> lcLapTimes;
    public NumberAxis xAxis;
    public NumberAxis yAxis;

    @FXML
    public void initialize() {

        loadDataListener();
        cbSoftListener();
        cbMediumListener();
        cbHardListener();

    }


    private void cbHardListener() {
        cbHard.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Distance neededData = getNeededData();
            ObservableValue<Boolean> ov = (ObservableValue<Boolean>) observable;
            Boolean isChecked = ov.getValue();

            if (isChecked) {
                addLineToLineChart(getLapTimeMap(neededData.hard), HARD);
            } else {
                lcLapTimes.getData().clear();
                if (cbSoft.selectedProperty().getValue())
                    addLineToLineChart(getLapTimeMap(neededData.soft), SOFT);
                if (cbMedium.selectedProperty().getValue())
                    addLineToLineChart(getLapTimeMap(neededData.medium), MEDIUM);
            }
        });
    }

    private void cbMediumListener() {
        cbMedium.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Distance neededData = getNeededData();
            ObservableValue<Boolean> ov = (ObservableValue<Boolean>) observable;
            Boolean isChecked = ov.getValue();

            if (isChecked) {
                addLineToLineChart(getLapTimeMap(neededData.medium), MEDIUM);
            } else {
                lcLapTimes.getData().clear();
                if (cbSoft.selectedProperty().getValue())
                    addLineToLineChart(getLapTimeMap(neededData.soft), SOFT);
                if (cbHard.selectedProperty().getValue())
                    addLineToLineChart(getLapTimeMap(neededData.hard), HARD);
            }
        });
    }

    private void cbSoftListener() {
        cbSoft.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Distance neededData = getNeededData();
            ObservableValue<Boolean> ov = (ObservableValue<Boolean>) observable;
            Boolean isChecked = ov.getValue();

            if (isChecked) {
                addLineToLineChart(getLapTimeMap(neededData.soft), SOFT);
            } else {
                lcLapTimes.getData().clear();
                if (cbMedium.selectedProperty().getValue())
                    addLineToLineChart(getLapTimeMap(neededData.medium), MEDIUM);
                if (cbHard.selectedProperty().getValue())
                    addLineToLineChart(getLapTimeMap(neededData.hard), HARD);
            }
        });
    }

    private void loadDataListener() {
        loadData.setOnAction(actionEvent -> {

            lcLapTimes.getData().clear();
            lcLapTimes.setTitle(LINE_CHART_TITLE);

            xAxis.setAutoRanging(false);
            yAxis.setAutoRanging(false);

            Distance neededData = getNeededData();

            addLineToLineChart(getLapTimeMap(neededData.soft), SOFT);
            addLineToLineChart(getLapTimeMap(neededData.medium), MEDIUM);
            addLineToLineChart(getLapTimeMap(neededData.hard), HARD);

            xAxis.setLowerBound( xMin );
            xAxis.setUpperBound( xMax + LINE_CHART_MARGIN_RIGHT);
            yAxis.setLowerBound( yMin );
            yAxis.setUpperBound( yMax + LINE_CHART_MARGIN_TOP );

        });
    }


    private Distance getNeededData() {
        String distance = cbDistance.getValue();
        String track = cbTrack.getValue();
        String condition = cbCondition.getValue();

        return getDistanceObject(data, distance, track, condition);
    }


    private void addLineToLineChart(Map<Integer, Double> data, String nameInChart) {
        XYChart.Series<Number, Number> line = new XYChart.Series<>();
        line.setName(nameInChart);

        for (int i = 0; i < data.size(); i++)
            line.getData().add(new XYChart.Data<>(i + 1, data.get(i + 1)));
        lcLapTimes.getData().add(line);
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