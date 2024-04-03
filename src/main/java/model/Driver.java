package model;

import org.w3c.dom.Node;

import java.util.ArrayList;

public class Driver {
    private Vehicle vehicle;
    private ArrayList<Node> routeList;

    public Driver() { // default constructor has a driver with a red car
        this.vehicle = new Vehicle();
    }

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

}
