package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TrafficSimulator_SimulatorSettingsBottomPane extends VBox {

    private Label numOfDriversLabel;
    private TextField numOfDriversInput;

    private Label carSpawnChanceLabel;
    private TextField carSpawnChanceInput;
    private Slider carSpawnChanceSlider;

    private Label vanSpawnChanceLabel;
    private TextField vanSpawnChanceInput;
    private Slider vanSpawnChanceSlider;

    private CheckBox busTransport;
    private Label numOfBuseslabel;
    private TextField numOfBusesInput;

    private Button Start;
    private Button Back;


    public TrafficSimulator_SimulatorSettingsBottomPane() {
        HBox overall = new HBox(30); // overall layout container, contains 2 Vbox's, each of these
        // vboxes has the fields, which are themselves stored in Hbox's.

        VBox fields_1 = new VBox(5);
        VBox fields_2 = new VBox(5);

        HBox numOfDrivers = new HBox(20);
        numOfDriversLabel = new Label("Number of drivers: ");
        numOfDriversInput = new TextField("0");
        numOfDrivers.getChildren().addAll(numOfDriversLabel, numOfDriversInput);

        HBox carSpawnChance = new HBox(20);
        carSpawnChanceLabel = new Label("Car spawn chance: ");
        carSpawnChanceInput = new TextField("50%");
        carSpawnChanceSlider = new Slider(0, 100, 50);
        carSpawnChanceSlider.setOrientation(Orientation.HORIZONTAL);
        carSpawnChance.getChildren().addAll(carSpawnChanceLabel, carSpawnChanceInput, carSpawnChanceSlider);

        HBox vanSpawnChance = new HBox(20);
        vanSpawnChanceLabel = new Label("Van spawn chance: ");
        vanSpawnChanceInput = new TextField("50%");
        vanSpawnChanceSlider = new Slider(0, 100, 50);
        vanSpawnChanceSlider.setOrientation(Orientation.HORIZONTAL);
        vanSpawnChance.getChildren().addAll(vanSpawnChanceLabel, vanSpawnChanceInput, vanSpawnChanceSlider);
        fields_1.getChildren().addAll(numOfDrivers, carSpawnChance, vanSpawnChance);


        busTransport = new CheckBox("Bus transport/route system");
        HBox numOfBuses = new HBox(20);
        numOfBuseslabel = new Label("Number of buses: ");
        numOfBusesInput = new TextField("-");
        numOfBuses.getChildren().addAll(numOfBuseslabel, numOfBusesInput);
        fields_2.getChildren().addAll(busTransport, numOfBuses);

        Start = new Button("Start Simulation");
        Back = new Button("Back");
        Back.setPrefWidth(100);

        VBox fields_3 = new VBox(5);
        fields_3.getChildren().addAll(Start, Back);

        overall.getChildren().addAll(fields_1, fields_2, fields_3);
        this.setPadding(new Insets(320,0,0,0));
        this.getChildren().add(overall);

    }

    public void addGoBackHandler(EventHandler<ActionEvent> handler) {
        Back.setOnAction(handler);
    }
}
