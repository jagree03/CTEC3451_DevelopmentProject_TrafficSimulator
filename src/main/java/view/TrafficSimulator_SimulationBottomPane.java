package view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;

public class TrafficSimulator_SimulationBottomPane extends HBox {

    private Button btnBack;
    private Button btnStartAndPause;
    private Button btnStatistics;

    public TrafficSimulator_SimulationBottomPane() {
        btnBack = new Button("BACK");
        btnStartAndPause = new Button("PAUSE SIMULATION");
        btnStatistics = new Button("STATISTICS");
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(btnBack, btnStartAndPause, btnStatistics);
    }

    public Button getStartAndPauseButton() {
        return btnStartAndPause;
    }

    public void addGoBackHandler(EventHandler<ActionEvent> handler) {
        btnBack.setOnAction(handler);
    }

    public void addPauseSimulationHandler(EventHandler<ActionEvent> handler) {
        btnStartAndPause.setOnAction(handler);
    }

    public void addStatisticsHandler(EventHandler<ActionEvent> handler) {
        btnStatistics.setOnAction(handler);
    }


}
