package de.malte.f1strat;

import de.malte.f1strat.handler.JsonHandler;
import de.malte.f1strat.logic.TyreStrategy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("start-view.fxml"));
        stage.setTitle("F1 | Strategy");

        Parent root = fxmlLoader.load();
        StartController controller = fxmlLoader.getController();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch(args);

        JsonHandler jsonHandler = new JsonHandler();
        DataInstance di = jsonHandler.loadData("f1-data.json");
        TyreStrategy ts = new TyreStrategy(di.dry.tracks.bahrain.trackData.fifty);

        System.out.println(ts.getTireOrderWithTrackTime());
    }
}