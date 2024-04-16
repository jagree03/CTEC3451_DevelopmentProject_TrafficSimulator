package model;

import org.w3c.dom.Node;

import java.util.ArrayList;

public class Driver {

    // fields
    private Vehicle vehicle;
    private ArrayList<Node> routeList;

    private String lane;
    private GraphNode startingNode;
    private Double startingPosX;
    private Double startingPosY;


    // constructors
    public Driver() { // default constructor has a driver with a red car who drives on left lane
        this.vehicle = new Vehicle();
        this.lane = "left";
    }

    // custom constructor where you can provide lane of the driver, and which node should start in the scenario
    public Driver(String lane, GraphNode startingNode) {
        this.vehicle = new Vehicle();
        this.lane = lane;
        this.startingNode = startingNode;
    }



    // methods
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ArrayList<Node> getRouteList() {
        return this.routeList;
    }

    public void setRouteList(ArrayList<Node> routeList) {
        this.routeList = routeList;
    }

    public void setStartingPosX(Double x) {
        this.startingPosX = x;
    }

    public void setStartingPosY(Double y) {
        this.startingPosY = y;
    }

    public Double getStartingPosX() {
        return startingPosX;
    }

    public Double getStartingPosY() {
        return startingPosY;
    }

    public void setStartingNode(GraphNode n) {
        this.startingNode = n;
    }

    public GraphNode getStartingNode() {
        return startingNode;
    }

    public Double getCurrentTranslateX() {
        return this.getVehicle().getSpriteImageView().getTranslateX();
    }

    public Double getCurrentTranslateY() {
        return this.getVehicle().getSpriteImageView().getTranslateY();
    }

    public Double getCurrentPositionX() {
        return this.getVehicle().getSpriteImageView().getX();
    }

    public Double getCurrentPositionY() {
        return this.getVehicle().getSpriteImageView().getY();
    }

}
