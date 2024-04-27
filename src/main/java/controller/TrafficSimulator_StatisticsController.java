package controller;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import model.Driver;
import model.Driver_Statistics;
import model.PetrolStation;
import model.Vehicle;
import view.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class TrafficSimulator_StatisticsController {

    //fields to be used throughout class
    private Scene scene;
    private TrafficSimulator_Statistics view;
    private TrafficSimulator_StatisticsDropDownMenu tsddm;
    private TrafficSimulator_StatisticsBottomPane tssbp;

    private ArrayList<Driver_Statistics> allDrivers = new ArrayList<Driver_Statistics>();
    private ArrayList<PetrolStation> allPetrolStations = new ArrayList<PetrolStation>();

    public TrafficSimulator_StatisticsController(TrafficSimulator_Statistics view) throws IOException {
        //initialise view and model fields
        this.view = view;


        //initialise view subcontainer fields
        tsddm = view.getTSSDDM();
        tssbp = view.getTSSBP();

        //attach event handlers to view using private helper method
        this.attachEventHandlers();

        // execute any other methods
        readDriverAndVehicleStats();
        readPetrolStationStats();
        modifyComboBoxList();
    }

    //helper method - used to attach event handlers
    private void attachEventHandlers() {
        //attach event handlers here

        /**
         * ChangeListener that checks if the combobox item has been modified,
         * then update the displayed statistics.
         */
        tsddm.addComboBoxListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.contains("Driver")) {
                    UUID uniqueID = UUID.fromString(newValue.substring(7));
                    Driver_Statistics driver = findDriverBasedOnUUID(uniqueID);
                    tsddm.setIcon(driver.getVehicle().getSprite());
                    updateStats(driver);
                } else if (newValue.contains("PetrolStation")) {
                    UUID uniqueID = UUID.fromString(newValue.substring(14));
                    PetrolStation p = findPetrolStationBasedOnUUID(uniqueID);
                    tsddm.setIcon(p.getImageViewButton().getImage());
                    updateStats(p);
                }
            }
        });

        tssbp.addRefreshButtonHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    allDrivers.clear();
                    allPetrolStations.clear();
                    readDriverAndVehicleStats();
                    readPetrolStationStats();
                    String item = tsddm.getSelectedItem().toString();
                    if (item.contains("Driver")) {
                        UUID uniqueId = UUID.fromString(item.substring(7));
                        Driver_Statistics driver = findDriverBasedOnUUID(uniqueId);
                        tsddm.setIcon(driver.getVehicle().getSprite());
                        updateStats(driver);
                    } else if (item.contains("PetrolStation")) {
                        UUID uniqueId = UUID.fromString(item.substring(14));
                        PetrolStation p = findPetrolStationBasedOnUUID(uniqueId);
                        tsddm.setIcon(p.getImageViewButton().getImage());
                        updateStats(p);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    public void modifyComboBoxList() {
        ObservableList<String> options =
                FXCollections.observableArrayList();
        for (Driver_Statistics d : allDrivers) {
            options.add("Driver " + d.getUniqueId());
        }
        for (PetrolStation p : allPetrolStations) {
            options.add("PetrolStation " + p.getUniqueID());
        }
        tsddm.modifyComboBoxItems(options);
    }

    /**
     * Updates the label values so they match the values of the passed in object.
     * @param driver Driver_Statistics object instance
     */
    public void updateStats(Driver_Statistics driver) {
        tssbp.updateLabels(driver);
    }

    /**
     * overloaded variation of the update stats method, basically updates the label values so they
     * match the values of the passed in object.
     * @param p PetrolStation object instance
     */
    public void updateStats(PetrolStation p) {
       tssbp.updateLabels(p);
    }

    // other methods here
    public void readDriverAndVehicleStats() throws IOException {
        File file = new File("statistics\\drivers.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();

        while (line != null) {
            UUID uniqueID = UUID.fromString(line); // driver's unique id

            line = bufferedReader.readLine(); // go to next line
            String vehicleType = line;

            line = bufferedReader.readLine(); // go to next line
            String imageURI = line;

            line = bufferedReader.readLine(); // go to next line
            String colour = line;

            line = bufferedReader.readLine(); // go to next line
            Double fuelLevel = Double.parseDouble(line);

            line = bufferedReader.readLine(); // go to next line
            Double rotationVal = Double.parseDouble(line);

            line = bufferedReader.readLine(); // go to next line
            Boolean hazLightsOn = Boolean.parseBoolean(line);

            line = bufferedReader.readLine(); // go to next line
            String lane = line;

            line = bufferedReader.readLine(); // go to next line
            String startNodeID = line;

            line = bufferedReader.readLine(); // go to next line
            String goalNodeID = line;

            line = bufferedReader.readLine(); // go to next line
            String currentNodeID = line;

            Vehicle vehicle = new Vehicle(vehicleType, imageURI, colour, fuelLevel);
            Driver_Statistics driver = new Driver_Statistics(uniqueID, rotationVal, hazLightsOn, lane, startNodeID, goalNodeID, currentNodeID, vehicle);
            allDrivers.add(driver);

            line = bufferedReader.readLine(); // go to next line
        }
        bufferedReader.close();
    }

    public void readPetrolStationStats() throws IOException {
        File file = new File("statistics\\petrolstations.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();

        while (line != null) {
            UUID uniqueID = UUID.fromString(line); // petrol station's unique id

            line = bufferedReader.readLine(); // go to next line
            String name = line;

            line = bufferedReader.readLine(); // go to next line
            String imageURI = line;

            line = bufferedReader.readLine(); // go to next line
            Double totalFuel = Double.parseDouble(line);

            line = bufferedReader.readLine(); // go to next line
            Double pricePerLitre = Double.parseDouble(line);

            line = bufferedReader.readLine(); // go to next line
            Double sales = Double.parseDouble(line);

            line = bufferedReader.readLine(); // go to next line
            boolean outOfFuel = Boolean.parseBoolean(line);

            PetrolStation p = new PetrolStation(uniqueID, name, imageURI, totalFuel, pricePerLitre, sales, outOfFuel);
            allPetrolStations.add(p);

            line = bufferedReader.readLine();
        }
        bufferedReader.close();
    }


    public Driver_Statistics findDriverBasedOnUUID(UUID uniqueId) {
        String uniqueID = uniqueId.toString();
        Driver_Statistics found = null;
        for (Driver_Statistics d : allDrivers) {
            if (d.getUniqueId().toString().equals(uniqueID)) {
                found = d;
            }
        }
        return found;
    }

    public PetrolStation findPetrolStationBasedOnUUID(UUID uniqueId) {
        String uniqueID = uniqueId.toString();
        PetrolStation found = null;
        for (PetrolStation p : allPetrolStations) {
            if (p.getUniqueID().toString().equals(uniqueID)) {
                found = p;
            }
        }
        return found;
    }


}
