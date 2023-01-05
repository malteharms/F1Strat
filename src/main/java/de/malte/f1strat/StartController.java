package de.malte.f1strat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

public class StartController {

    @FXML
    private Button loadData;

    @FXML
    private LineChart<Number, Number> lcLapTimes;

    @FXML
    public void initialize() {
        loadData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // loadData button is clicked


            }
        });
    }

    public void fillWithData() {
        // line chart
        lcLapTimes.setTitle("Rundenzeiten pro Reifen");

        XYChart.Series<Number, Number> soft = new XYChart.Series<>();
        soft.setName("Soft");
        soft.getData().add(new XYChart.Data<>(1.0, 3.0));
        soft.getData().add(new XYChart.Data<>(2.0, 6.0));
        soft.getData().add(new XYChart.Data<>(3.0, 7.0));

        lcLapTimes.getData().add(soft);
    }
}