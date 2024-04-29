package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class TrafficSimulator_SimulatorSettings extends VBox {

    private TrafficSimulatorRootMenuBar menuBar;

    private TrafficSimulator_EditorEditorPane editorPane;
    private TrafficSimulator_SimulatorSettingsBottomPane tssbp;

    /**
     * Default Constructor
     */
    public TrafficSimulator_SimulatorSettings() {
        menuBar = new TrafficSimulatorRootMenuBar();
        editorPane = new TrafficSimulator_EditorEditorPane();
        tssbp = new TrafficSimulator_SimulatorSettingsBottomPane();
        tssbp.setPadding(new Insets(10,0,10,5));

        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(menuBar, editorPane, tssbp);
    }

    public TrafficSimulatorRootMenuBar getTSS_MenuBar() {
        return menuBar;
    }

    public TrafficSimulator_SimulatorSettingsBottomPane getTSS_BottomPane() {
        return tssbp;
    }

    public TrafficSimulator_EditorEditorPane getTSS_EditorPane() {return editorPane;}
}
