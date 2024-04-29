package view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class TrafficSimulator_Simulation extends VBox {

    private TrafficSimulator_SimulationMenuBar menuBar;

    private TrafficSimulator_EditorEditorPane editorPane;

    private TrafficSimulator_SimulationBottomPane bottomPane;

    /**
     * Default Constructor
     */
    public TrafficSimulator_Simulation() {
        menuBar = new TrafficSimulator_SimulationMenuBar();
        editorPane = new TrafficSimulator_EditorEditorPane();
        bottomPane = new TrafficSimulator_SimulationBottomPane();
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(menuBar, editorPane, bottomPane);
    }

    public TrafficSimulator_SimulationMenuBar getTSS_MenuBar() {
        return menuBar;
    }

    public TrafficSimulator_EditorEditorPane getTSS_EditorPane() {
        return editorPane;
    }

    public TrafficSimulator_SimulationBottomPane getTSS_BottomPane() {
        return bottomPane;
    }
}
