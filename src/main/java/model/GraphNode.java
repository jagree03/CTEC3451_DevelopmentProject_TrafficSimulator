package model;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.Serializable;

public class GraphNode implements Serializable {
    private String id;
    private Double xCoordinate;
    private Double yCoordinate;

    // A* Pathfinding variables

    // To evaluate the shortest path, A* pathfinding uses 3 values; G cost; H cost, F cost.
    // It uses evaluates these costs for each node, to find the most promising node towards the goal node
    private GraphNode parent;
    private double gCost;  // G cost: the distance between the current node and the start node
    private double hCost;  // H cost: the distance from the current node to the goal node
    private double fCost;  // The sum of G cost and H cost (G cost + H cost) of the node, most important value
    private boolean start;
    private boolean goal;
    private boolean solid;
    private boolean open;
    private boolean checked;

    public GraphNode(Double xCoordinate, Double yCoordinate) {
        this.id = "1";
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public GraphNode(String id, Double xCoordinate, Double yCoordinate) {
        this.id = id;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public Double getXCoordinate() {
        return this.xCoordinate;
    }

    public Double getYCoordinate() {
        return this.yCoordinate;
    }

    public void setXCoordinate(Double x) {
        this.xCoordinate = x;
    }
    public void setYCoordinate(Double y) {
        this.yCoordinate = y;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "GraphNode:[id=" + id + ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate +
                ", parent=" + parent + ", gCost=" + gCost + ", hCost=" + hCost + ", fCost=" + fCost +
                ", start=" + start + ", goal=" + goal + ", solid=" + solid + ", open=" + open +
                ", checked=" + checked + "]";
    }

    ///////////////////////////////////////////
    // A* Pathfinding algorithm related methods
    ///////////////////////////////////////////

    public void setParentNode(GraphNode n) {
        this.parent = n;
    }

    public void setgCost(Double val) {
        this.gCost = val;
    }
    public void sethCost(Double val) {
        this.hCost = val;
    }
    public void setfCost(Double val) {
        this.fCost = val;
    }

    public GraphNode getParentNode() {
        return parent;
    }

    public double getgCost() {
        return gCost;
    }
    public double gethCost() {
        return hCost;
    }
    public double getfCost() {
        return fCost;
    }

    public boolean getStartValue(){
        return this.start;
    }

    public boolean getGoalValue(){
        return this.goal;
    }

    public boolean getSolidValue(){
        return this.solid;
    }

    public boolean getOpenValue(){
        return this.open;
    }

    public boolean getCheckedValue(){
        return this.checked;
    }

    public void setAsStart() {
        start = true;
    }

    public void setAsGoal() {
        goal = true;
    }

    public void setAsSolid() {
        solid = true;
    }

    public void setAsOpen() {
        open = true;
    }

    public void setAsChecked() {
        if (start == false && goal == false) {
        }
        checked = true;
    }

    public void setCheckedFalse() {
        checked = false;
    }

    public void setAsPath() {}

} // end of class