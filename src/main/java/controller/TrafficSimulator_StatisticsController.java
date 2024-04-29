package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import model.Driver_Statistics;
import model.PetrolStation;
import model.Vehicle;
import view.TrafficSimulator_Statistics;
import view.TrafficSimulator_StatisticsBottomPane;
import view.TrafficSimulator_StatisticsDropDownMenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class TrafficSimulator_StatisticsController {

    //fields to be used throughout class
    private TrafficSimulator_Statistics view;
    private TrafficSimulator_StatisticsDropDownMenu tsddm;
    private TrafficSimulator_StatisticsBottomPane tssbp;

    private ArrayList<Driver_Statistics> allDrivers = new ArrayList<Driver_Statistics>(); // an arraylist to hold all driver's statistics from the scenario, based on the number of drivers in the scenario
    private ArrayList<PetrolStation> allPetrolStations = new ArrayList<PetrolStation>(); // an arraylist to hold all petrolstations in the scenario, based on the number of petrol stations in the scenario

    /**
     * This is a constructor for the Statistics Controller
     * @param view A TrafficSimulator_Statistics instance
     * @throws IOException if the text files that hold the statistics cannot be found, throw this exception
     */
    public TrafficSimulator_StatisticsController(TrafficSimulator_Statistics view) throws IOException {
        //initialise view and model fields
        this.view = view;


        //initialise view subcontainer fields
        tsddm = view.getTSSDDM();
        tssbp = view.getTSSBP();

        //attach event handlers to view using private helper method
        this.attachEventHandlers();

        // execute any other methods - reading the stats and modifying the combobox list.
        readDriverAndVehicleStats();
        readPetrolStationStats();
        modifyComboBoxList();
    }

    /**
     * This method attaches the event handlers to the respective controls in each of the statistics related view classes that form the whole
     * Statistics screen
     */
    private void attachEventHandlers() {

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

        /**
         * Refresh button event handler, clears the arraylists that hold the driver statistics and petrol stations, re-reads the stats from the files and
         * this re-adds the objects back into their respective arraylists, then update the stats depending on the chosen combobox list item
         * either driver or petrol station.
         */
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

    /**
     * This method updates the combobox list in TrafficSimulator_StatisticsDropDownMenu (tsddm)
     * so that they point to the objects from the arraylists (allDrivers and allPetrolStations).
     */
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
     * overloaded variation of the update stats method, updates the label values so they
     * match the values of the passed in object.
     * @param p PetrolStation object instance
     */
    public void updateStats(PetrolStation p) {
       tssbp.updateLabels(p);
    }

    // other methods here

    /**
     * This method reads the driver and vehicle stats, by accepting the drivers.txt file from the statistics folder
     * and instantiating objects based on those values in the file, then adding the objects to the allDrivers arraylist.
     * @throws IOException if the text file is not found or path cannot be accessed, throw this exception.
     */
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

    /**
     * This method reads the petrol station stats, by accepting the petrolstations.txt file from the statistics folder
     * and instantiating objects based on those values in the file, then adding the objects to the allPetrolStations arraylist.
     * @throws IOException if the text file is not found or path cannot be accessed, throw this exception.
     */
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


    /**
     * Finds a Driver_Statistics object in the arraylist allDrivers, based on a UUID uniqueID value.
     * @param uniqueId UUID Unique ID value
     * @return a Driver_Statistics object that matches the passed in UUID uniqueID.
     */
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

    /**
     * Finds a PetrolStation object in the arraylist allPetrolStations, based on a UUID uniqueID value.
     * @param uniqueId UUID Unique ID value
     * @return a PetrolStation object that matches the passed in UUID uniqueID.
     */
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
