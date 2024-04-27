package model;

import java.util.UUID;

public class Driver_Statistics {
    // data members
    private Vehicle vehicle;
    private UUID uniqueId;
    private double rotationVal;
    private boolean hazLightsOn;
    private String lane;

    private String startNodeID;
    private String goalNodeID;
    private String currentNodeID;



    // constructors
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
    public UUID getUniqueId() {
        return uniqueId;
    }

    public double getRotationVal() {
        return rotationVal;
    }

    public boolean isHazLightsOn() {
        return hazLightsOn;
    }

    public String getLane() {
        return lane;
    }

    public String getStartNodeID() {
        return startNodeID;
    }

    public String getGoalNodeID() {
        return goalNodeID;
    }

    public String getCurrentNodeID() {
        return currentNodeID;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
