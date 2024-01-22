package view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class TrafficSimulator_SimulatorSettings extends VBox {

    private TrafficSimulatorRootMenuBar menuBar;
    private TrafficSimulator_SimulatorSettingsBottomPane tsssbp;

    public TrafficSimulator_SimulatorSettings() {
        menuBar = new TrafficSimulatorRootMenuBar();
        tsssbp = new TrafficSimulator_SimulatorSettingsBottomPane();

        this.setSpacing(5);
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(menuBar, tsssbp);
    }
}
