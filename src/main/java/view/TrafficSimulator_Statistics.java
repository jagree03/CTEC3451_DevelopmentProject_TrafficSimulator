package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TrafficSimulator_Statistics extends VBox {

    private Label numberOfDrivers;

    public TrafficSimulator_Statistics() {
        numberOfDrivers = new Label("Total Number of Drivers: ");
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().add(numberOfDrivers);
    }
}
