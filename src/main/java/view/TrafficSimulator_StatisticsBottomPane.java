package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Driver_Statistics;
import model.PetrolStation;
import model.Vehicle;

import java.util.Iterator;

public class TrafficSimulator_StatisticsBottomPane extends VBox {

    private Vehicle vehicle;

    private Label vehicleUUID;

    private Label vehicleType;
    private Label vehicleSpriteURL;
    private Label vehicleColour;
    private Label vehicleFuelLevel;

    private Label vehicleOutOfFuel;

    private Label driverHazardLightsOn;

    private Label driverLane;
    private Label driverStartNodeID;
    private Label driverGoalNodeID;
    private Label driverCurrentNodeID;

    private Label petrolStationUUID;

    private Label petrolStationName;

    private Label petrolStationTotalFuel;

    private Label petrolStationPricePerLitre;

    private Label petrolStationSales;

    private Label petrolStationOutOfFuelStatus;

    private Label petrolStationImageURI;

    private Button refresh;

    /**
     * Default constructor
     */
    public TrafficSimulator_StatisticsBottomPane() {
        this.setSpacing(5);
        this.setAlignment(Pos.TOP_LEFT);
        this.setPadding(new Insets(0,0,0,5));

        refresh = new Button("Refresh");
        refresh.setAlignment(Pos.BASELINE_CENTER);
    }

    public void setDriverVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getDriverVehicle() {
        return vehicle;
    }

    /**
     * Updates the labels of this layout to match the new values passed from the Driver_Statistics object
     * @param d Driver_Statistics object instance
     */
    public void updateLabels(Driver_Statistics d) {
        // Remove any other labels
        removeAllLabelsAndButtons();

        vehicleType = new Label("Vehicle Type: " + d.getVehicle().getType());
        vehicleSpriteURL = new Label("Vehicle Sprite URI: " + d.getVehicle().getSprite().getUrl());
        vehicleColour = new Label("Vehicle Colour: " + d.getVehicle().getColor());
        vehicleFuelLevel = new Label("Vehicle Fuel Level: " + d.getVehicle().getFuelLevel());

        boolean outOfFuel = false;
        if (d.getVehicle().getFuelLevel() == 0.00)
            outOfFuel = true;
        else
            outOfFuel = false;

        vehicleOutOfFuel = new Label("Vehicle Out Of Fuel: " + outOfFuel);
        driverHazardLightsOn = new Label("Hazards activated: " + d.isHazLightsOn());
        driverLane = new Label("Driver Lane: " + d.getLane());
        driverStartNodeID = new Label("Driver Start Node: " + d.getStartNodeID());
        driverGoalNodeID = new Label("Driver Goal Node: " + d.getGoalNodeID());
        driverCurrentNodeID = new Label("Driver Current Node: " + d.getCurrentNodeID());
        this.getChildren().addAll(vehicleType, vehicleSpriteURL, vehicleColour,
                                  vehicleFuelLevel, vehicleOutOfFuel, driverHazardLightsOn, driverLane,
                                  driverStartNodeID, driverGoalNodeID, driverCurrentNodeID);

        this.getChildren().add(refresh);
    }

    /**
     * Updates the labels of this layout to match the new values passed from the PetrolStation object
     * @param p PetrolStation object instance
     */
    public void updateLabels(PetrolStation p) {
        removeAllLabelsAndButtons();

        petrolStationUUID = new Label("Petrol Station ID: " + p.getUniqueID());
        petrolStationName = new Label("Petrol Station Name: " + p.getName());
        petrolStationImageURI = new Label("Petrol Station Sprite URI: " + p.getImageViewButton().getImage().getUrl());
        petrolStationTotalFuel = new Label("Petrol Station Total Fuel: " + p.getTotalFuel());
        petrolStationPricePerLitre = new Label("Petrol Station Price Per Litre: " + p.getPricePerLitre());
        petrolStationSales = new Label("Petrol Station Sales: £ " + p.getSales());
        petrolStationOutOfFuelStatus = new Label("Petrol Station Out Of Fuel: " + p.getOutOfFuelValue());

        this.getChildren().addAll(petrolStationUUID, petrolStationName, petrolStationImageURI,
                                  petrolStationTotalFuel, petrolStationPricePerLitre, petrolStationSales,
                                  petrolStationOutOfFuelStatus);

        this.getChildren().add(refresh);
    }

    /**
     * When the refresh button is pressed, it'll call this method.
     * This method removes all labels and buttons currently in this bottom pane, so they can be replaced.
     * It uses an iterator to do so, preventing the Java ConcurrentModificationException from occurring.
     */
    public void removeAllLabelsAndButtons() {
        // Remove any other labels
        Iterator<Node> iter = this.getChildren().iterator();
        while(iter.hasNext()) {
            Node n = iter.next();
            if (n instanceof Label) {
                iter.remove();
            } else if (n instanceof Button) {
                iter.remove();
            }
        }
    }

    /**
     * Add event handler to the refresh button so it updates the labels and values
     * @param handler EventHandler<ActionEvent>
     */
    public void addRefreshButtonHandler(EventHandler<ActionEvent> handler) {
        refresh.setOnAction(handler);
    }
}
