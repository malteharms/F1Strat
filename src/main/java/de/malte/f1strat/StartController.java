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
    public LineChart<Number, Number> lcLapTimes;
    public LineChart<Number, Number> lcStrategy;
    public NumberAxis xAxis;
    public NumberAxis yAxis;
    public NumberAxis strXAxis;
    public NumberAxis strYAxis;

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
        if (condition.equals("Trocken")) {
            TrackData onDry = switchTrack(data.dry.tracks, track);
            if (distance.equals("100%")) {
                return onDry.hundred;
            }
            return onDry.fifty;
        }
        TrackData onWet = switchTrack(data.wet.tracks, track);
        if (distance.equals("100%")) {
            return onWet.hundred;
        }
        return onWet.fifty;
    }

    private TrackData switchTrack(Tracks data, String track) {
        Track whichTrack = switch (track) {
            case "Bahrain" -> data.bahrain;
            case "Jeddah" -> data.jeddah;
            case "Australien" -> data.australia;
            case "Imola" -> data.imola;
            case "Miami" -> data.miami;
            case "Spanien" -> data.spain;
            case "Monaco" -> data.monaco;
            case "Baku" -> data.baku;
            case "Kanada" -> data.canada;
            case "Silverstone" -> data.silverstone;
            case "Österreich" -> data.austria;
            case "Frankreich" -> data.france;
            case "Ungarn" -> data.hungary;
            case "Belgien" -> data.spa;
            case "Niederlande" -> data.dutch;
            case "Monza" -> data.monza;
            case "Singapur" -> data.singapore;
            case "Japan" -> data.japan;
            case "USA" -> data.usa;
            case "Mexiko" -> data.mexico;
            case "Brasilien" -> data.brazil;
            case "Abu Dhabi" -> data.abudhabi;
            case "China" -> data.china;
            default -> data.portuguese;
        };

        return whichTrack.trackData;
    }
}