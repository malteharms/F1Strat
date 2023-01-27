module de.malte.f1strat {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires json.simple;

    opens de.malte.f1strat to javafx.fxml;
    exports de.malte.f1strat;
    exports de.malte.f1strat.helper;
}