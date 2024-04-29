package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TrafficSimulator_Procedural extends VBox {

    private TrafficSimulatorRootMenuBar menuBar;

    private Button Generate;
    private Button Finish;
    private Button Back;

    /**
     * Default constructor
     */
    public TrafficSimulator_Procedural() {


        menuBar = new TrafficSimulatorRootMenuBar();
        HBox buttons = new HBox(20);
        Back = new Button("Back");
        Generate = new Button("Generate Scenario");
        Finish = new Button("Finish");
        buttons.getChildren().addAll(Back, Generate, Finish);
        buttons.setPadding(new Insets(400,0,0,500));

        this.setSpacing(20);
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(menuBar, buttons);
    }

    public void addExitHandler(EventHandler<ActionEvent> handler) {
        menuBar.getExitItem().setOnAction(handler);
    }

}
