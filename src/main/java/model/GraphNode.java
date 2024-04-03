package model;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class GraphNode {
    private Node parent;

    private String id;
    private Double xCoordinate;
    private Double yCoordinate;
    private int gCost;
    private int hCost;
    private int fCost;
    private boolean start;
    private boolean goal;
    private boolean solid;
    private boolean open;
    private boolean checked;

    private StackPane stack;

    private Circle circle;

    private Text text;

    public GraphNode(Double xCoordinate, Double yCoordinate) {
        this.id = "1";
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        //this.circle = new Circle(xCoordinate, yCoordinate, 50.0f);
    }

    public GraphNode(String id, Double xCoordinate, Double yCoordinate) {
        this.id = id;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void setAsStart() {
        start = true;
    }

    public void setAsGoal() {
        goal = true;
    }

//    public void setAsSolid() {
//        solid = true;
//    }

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


}
