package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class TrafficSimulator_SimulationBottomPane extends HBox {

    private Button btnBack;
    private Button btnStartAndPause;
    private Button btnStatistics;

    /**
     * Default constructor
     */
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

    /**
     * Add the event handler to the back button so that when you press back it goes to the simulation settings screen/scene.
     * @param handler EventHandler<ActionEvent>
     */
    public void addGoBackHandler(EventHandler<ActionEvent> handler) {
        btnBack.setOnAction(handler);
    }

    /**
     * Add the event handler to the pause/play simulation button so when it is pressed it does the required behaviour.
     * @param handler EventHandler<ActionEvent>
     */
    public void addPauseSimulationHandler(EventHandler<ActionEvent> handler) {
        btnStartAndPause.setOnAction(handler);
    }

    /**
     * Add an event handler to the statistics button so that it opens the statistics window when clicked.
     * @param handler EventHandler<ActionEvent>
     */
    public void addStatisticsHandler(EventHandler<ActionEvent> handler) {
        btnStatistics.setOnAction(handler);
    }


}
