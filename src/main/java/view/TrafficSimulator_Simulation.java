package view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class TrafficSimulator_Simulation extends VBox {

    private TrafficSimulatorRootMenuBar menuBar;

    private TrafficSimulator_EditorEditorPane editorPane;

    private TrafficSimulator_SimulationBottomPane bottomPane;

    public TrafficSimulator_Simulation() {
        menuBar = new TrafficSimulatorRootMenuBar();
        editorPane = new TrafficSimulator_EditorEditorPane();
        bottomPane = new TrafficSimulator_SimulationBottomPane();
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(menuBar, editorPane, bottomPane);
    }

    public TrafficSimulatorRootMenuBar getTSS_MenuBar() {
        return menuBar;
    }

    public TrafficSimulator_EditorEditorPane getTSS_EditorPane() {
        return editorPane;
    }

    public TrafficSimulator_SimulationBottomPane getTSS_BottomPane() {
        return bottomPane;
    }
}
