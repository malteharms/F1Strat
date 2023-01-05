package de.malte.f1strat;

import de.malte.f1strat.handler.JsonHandler;
import de.malte.f1strat.logic.TyreStrategy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("start-view.fxml"));
        stage.setTitle("Hello!");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Rundenzeiten");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Soft");
        //populating the series with data
        series.getData().add(new XYChart.Data<>(1, 23));
        series.getData().add(new XYChart.Data<>(2, 14));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 24));
        series.getData().add(new XYChart.Data<>(5, 34));
        series.getData().add(new XYChart.Data<>(6, 36));
        series.getData().add(new XYChart.Data<>(7, 22));
        series.getData().add(new XYChart.Data<>(8, 45));
        series.getData().add(new XYChart.Data<>(9, 43));
        series.getData().add(new XYChart.Data<>(10, 17));
        series.getData().add(new XYChart.Data<>(11, 29));
        series.getData().add(new XYChart.Data<>(12, 25));

        Scene scene = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();

        final String location = "f1-data.json";

        // read file and parse it to object
        JsonHandler jsonHandler = new JsonHandler();
        DataInstance data = jsonHandler.loadData(location);

        TyreStrategy bahrainStrategy = new TyreStrategy(data.dry.tracks.bahrain.trackData.fifty);

    }
}