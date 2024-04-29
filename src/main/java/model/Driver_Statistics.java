package model;

import java.util.UUID;

public class Driver_Statistics {
    // data members
    private Vehicle vehicle; // store the driver's vehicle
    private UUID uniqueId; // store the driver's unique id
    private double rotationVal; // store the rotation value of the driver's vehicle (HBOX)
    private boolean hazLightsOn; // vehicle's hazard lights
    private String lane; // lane of the driver

    private String startNodeID; // id of the start node
    private String goalNodeID; // id of the goal node
    private String currentNodeID; // id of the current node


    /**
     * Constructor for Driver_Statistics
     * @param uniqueId UUID of the driver
     * @param rotationVal Rotation Value of the HBox that encapsulates the vehicle
     * @param hazLightsOn Hazard Lights status of the driver's vehicle
     * @param lane Driver's lane
     * @param startNodeID ID of the start node
     * @param goalNodeID ID of the goal node
     * @param currentNodeID ID of the current node
     * @param vehicle // driver's vehicle
     */
    public Driver_Statistics(UUID uniqueId, double rotationVal, boolean hazLightsOn, String lane, String startNodeID, String goalNodeID, String currentNodeID, Vehicle vehicle) {
        this.uniqueId = uniqueId;
        this.rotationVal = rotationVal;
        this.hazLightsOn = hazLightsOn;
        this.lane = lane;
        this.startNodeID = startNodeID;
        this.goalNodeID = goalNodeID;
        this.currentNodeID = currentNodeID;
        this.vehicle = vehicle;
    }

    // methods
    /**
     * Gets the unique ID of the driver
     * @return Unique ID in the form of a UUID.
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Gets the rotation value of the driver's HBox that has encapsulated the vehicle
     * @return Rotation value in the form of a Double value.
     */
    public double getRotationVal() {
        return rotationVal;
    }

    /**
     * Gets the status of the hazard lights of the driver's vehicle
     * @return Boolean value to represent if hazard lights are on or not.
     */
    public boolean isHazLightsOn() {
        return hazLightsOn;
    }

    /**
     * Gets the driver's lane
     * @return String representing the driver's lane
     */
    public String getLane() {
        return lane;
    }

    /**
     * Gets the ID of the startNode
     * @return ID of the startNode (String)
     */
    public String getStartNodeID() {
        return startNodeID;
    }

    /**
     * Gets the ID of the goalNode
     * @return ID of the goalNode (String)
     */
    public String getGoalNodeID() {
        return goalNodeID;
    }

    /**
     * Gets the ID of the currentNode
     * @return ID of the currentNode (String)
     */
    public String getCurrentNodeID() {
        return currentNodeID;
    }

    /**
     * Gets the driver's vehicle.
     * @return Vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * ToString method, indicates the state of each of the data members of the Driver_Statistics class
     * @return String representing states of the key variables of a Driver_Statistics instance.
     */
    @Override
    public String toString() {
        return "Driver_Statistics:[vehicle=" + vehicle + ", uniqueId=" + uniqueId
                + ", rotationVal=" + rotationVal + ", hazLightsOn=" + hazLightsOn
                + ", lane=" + lane + ", startNodeID=" + startNodeID + ", goalNodeID=" + goalNodeID
                + ", currentNodeID=" + currentNodeID + "]";
    }
}
