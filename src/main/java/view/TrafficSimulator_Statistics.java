package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TrafficSimulator_Statistics extends VBox {

    private int numOfDrivers;
    private Label lblNumberOfDrivers;

    private int carSpawnChance;

    private Label lblCarSpawnChance;

    private int vanSpawnChance;

    private Label lblVanSpawnChance;

    private int numOfBuses;

    private Label lblNumberOfBuses;

    private TrafficSimulator_StatisticsDropDownMenu tsddm;
    private TrafficSimulator_StatisticsBottomPane tssbp;

    public TrafficSimulator_Statistics() throws IOException {
        readSimulationSettingsFromFile(); // first read the parameter settings

        tsddm = new TrafficSimulator_StatisticsDropDownMenu();
        tsddm.setPadding(new Insets(10,0,0,0));

        tssbp = new TrafficSimulator_StatisticsBottomPane();
        tssbp.setPadding(new Insets(2,0,0,0));

        lblNumberOfDrivers = new Label("Total Number of Drivers: " + numOfDrivers);
        lblNumberOfBuses = new Label("Total Number Of Buses: " + numOfBuses);
        lblCarSpawnChance = new Label("Car Spawn Chance: " + carSpawnChance + "%");
        lblVanSpawnChance = new Label("Van Spawn Chance: " + vanSpawnChance + "%");

        this.setSpacing(5);
        this.setAlignment(Pos.TOP_LEFT);
        this.setPadding(new Insets(0,0,0,5));
        this.getChildren().addAll(lblNumberOfDrivers, lblNumberOfBuses, lblCarSpawnChance, lblVanSpawnChance, tsddm, tssbp);
    }

    public TrafficSimulator_StatisticsDropDownMenu getTSSDDM() {
        return tsddm;
    }

    public TrafficSimulator_StatisticsBottomPane getTSSBP() {
        return tssbp;
    }

    private void readSimulationSettingsFromFile() throws IOException {
        BufferedReader bufferedReader = null;
        try {
            String CurrentLine;
            bufferedReader = new BufferedReader(new FileReader("parameters.txt"));
            while ((CurrentLine = bufferedReader.readLine()) != null) { // check if line is NOT null/empty
                String[] array = CurrentLine.split(", ");
                numOfDrivers = Integer.parseInt((array[0]));
                carSpawnChance = Integer.parseInt((array[1]));
                vanSpawnChance = Integer.parseInt((array[2]));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            bufferedReader.close();
        }
    }
}
