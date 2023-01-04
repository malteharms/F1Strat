package de.malte.f1strat;

import de.malte.f1strat.handler.JsonHandler;
import de.malte.f1strat.logic.TyreStrategy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch();

        final String location = "f1-data.json";

        // read file and parse it to object
        JsonHandler jsonHandler = new JsonHandler();
        DataInstance data = jsonHandler.loadData(location);

        TyreStrategy soft = new TyreStrategy(data.dry.tracks.bahrain);
        soft.calculateFastestStrategy50();

    }
}