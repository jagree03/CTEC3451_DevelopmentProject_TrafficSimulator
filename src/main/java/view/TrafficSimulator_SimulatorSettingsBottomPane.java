package view;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
        numOfDriversInput = new TextField("1");
        numOfDriversInput.setPrefWidth(50);
        numOfDrivers.getChildren().addAll(numOfDriversLabel, numOfDriversInput);

        HBox carSpawnChance = new HBox(20);
        carSpawnChanceLabel = new Label("Car spawn chance: ");
        carSpawnChanceInput = new TextField("50%");
        carSpawnChanceInput.setPrefWidth(50);
        carSpawnChanceSlider = new Slider(0, 100, 50);
        carSpawnChanceSlider.setOrientation(Orientation.HORIZONTAL);
        carSpawnChance.getChildren().addAll(carSpawnChanceLabel, carSpawnChanceInput, carSpawnChanceSlider);

        HBox vanSpawnChance = new HBox(20);
        vanSpawnChanceLabel = new Label("Van spawn chance: ");
        vanSpawnChanceInput = new TextField("50%");
        vanSpawnChanceInput.setPrefWidth(50);
        vanSpawnChanceSlider = new Slider(0, 100, 50);
        vanSpawnChanceSlider.setOrientation(Orientation.HORIZONTAL);

        vanSpawnChance.getChildren().addAll(vanSpawnChanceLabel, vanSpawnChanceInput, vanSpawnChanceSlider);
        fields_1.getChildren().addAll(numOfDrivers, carSpawnChance, vanSpawnChance);


        busTransport = new CheckBox("Bus transportation");
        // bus transportation is unchecked by default
        busTransport.setSelected(false);

        HBox numOfBuses = new HBox(20);
        numOfBuseslabel = new Label("Number of buses: ");
        numOfBusesInput = new TextField("1");
        numOfBusesInput.setPrefWidth(50);

        // bus transportation is unchecked by default, so hide the input and label for it
        numOfBuseslabel.setDisable(true);
        numOfBuseslabel.setVisible(false);
        numOfBusesInput.setDisable(true);
        numOfBusesInput.setVisible(false);

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
    public void addStartHandler(EventHandler<ActionEvent> handler) {
        Start.setOnAction(handler);
    }

    public void addCarSpawnChanceSliderHandler(ChangeListener<Number> listener) {
        carSpawnChanceSlider.valueProperty().addListener(listener);
    }

    public void setCarSpawnChanceInput(int value) {
        carSpawnChanceInput.setText("" + value + "%");
        carSpawnChanceSlider.setValue(value);
    }

    public int getCarSpawnChanceSliderValue() {
        return (int) carSpawnChanceSlider.getValue();
    }

    public void addVanSpawnChanceSliderHandler(ChangeListener<Number> listener) {
        vanSpawnChanceSlider.valueProperty().addListener(listener);
    }

    public void setVanSpawnChanceInput(int value) {
        vanSpawnChanceInput.setText("" + value + "%");
        vanSpawnChanceSlider.setValue(value);
    }

    public int getVanSpawnChanceSliderValue() {
        return (int) vanSpawnChanceSlider.getValue();
    }

    public void addBusTransportCheckBoxHandler(EventHandler<ActionEvent> handler) {
        busTransport.setOnAction(handler);
    }

    public void enableNumberOfBusesInput(boolean value) {
        if (value) {
            numOfBuseslabel.setDisable(false);
            numOfBuseslabel.setVisible(true);
            numOfBusesInput.setDisable(false);
            numOfBusesInput.setVisible(true);
        } else {
            numOfBuseslabel.setDisable(true);
            numOfBuseslabel.setVisible(false);
            numOfBusesInput.setDisable(true);
            numOfBusesInput.setVisible(false);
        }
    }
    public int getNumberOfDrivers() {
        return Integer.parseInt(numOfDriversInput.getText());
    }

    public boolean getBusTransportEnabled() {
        return busTransport.isSelected();
    }

    public int getNumberOfBuses() {
        if (!this.getBusTransportEnabled()) { //if bus transport checkbox is NOT checked
            return 0;
        } else { // if it is checked
            return Integer.parseInt(numOfBusesInput.getText());
        }
    }

}
