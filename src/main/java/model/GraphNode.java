package model;

import java.io.Serializable;

public class GraphNode implements Serializable {
    private String id; // id of the graphnode
    private Double xCoordinate;
    private Double yCoordinate;

    // A* Pathfinding variables

    // To evaluate the shortest path, A* pathfinding uses 3 values; G cost; H cost, F cost.
    // It uses evaluates these costs for each node, to find the most promising node towards the goal node
    private GraphNode parent; // parent, required for backtracking
    private double gCost;  // G cost: the distance between the current node and the start node
    private double hCost;  // H cost: the distance from the current node to the goal node
    private double fCost;  // The sum of G cost and H cost (G cost + H cost) of the node, most important value
    private boolean start; // example of a flag
    private boolean goal;
    private boolean solid;
    private boolean open;
    private boolean checked;

    /**
     * Constructor for a GraphNode
     * @param xCoordinate X coordinate of the GraphNode
     * @param yCoordinate Y coordinate of the GraphNode
     */
    public GraphNode(Double xCoordinate, Double yCoordinate) {
        this.id = "1";
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    /**
     * Constructor #2 for a GraphNode
     * @param id id of the GraphNode
     * @param xCoordinate X coordinate of the GraphNode
     * @param yCoordinate Y coordinate of the GraphNode
     */
    public GraphNode(String id, Double xCoordinate, Double yCoordinate) {
        this.id = id;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    /**
     * Gets the X Coordinate of the GraphNode
     * @return Double value represent x coordinate.
     */
    public Double getXCoordinate() {
        return this.xCoordinate;
    }

    /**
     * Gets the Y Coordinate of the GraphNode
     * @return Double value represent y coordinate.
     */
    public Double getYCoordinate() {
        return this.yCoordinate;
    }

    public void setXCoordinate(Double x) {
        this.xCoordinate = x;
    }
    public void setYCoordinate(Double y) {
        this.yCoordinate = y;
    }

    /**
     * Sets the graphnode's id
     * @param id String id of which you want to set the graphnode's id to.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the graphnode's id
     * @return String id of the graphnode.
     */
    public String getId() {
        return this.id;
    }

    /**
     * ToString method, indicates the state of each of the data members of the GraphNode class
     * @return String representing states of the key variables of a GraphNode instance.
     */
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

    /**
     * Sets the parent node of a GraphNode
     * @param n GraphNode
     */
    public void setParentNode(GraphNode n) {
        this.parent = n;
    }

    /**
     * Sets the G Cost of a GraphNode
     * @param val Double value (cost)
     */
    public void setgCost(Double val) {
        this.gCost = val;
    }

    /**
     * Sets the H Cost of a GraphNode
     * @param val Double value (cost)
     */
    public void sethCost(Double val) {
        this.hCost = val;
    }

    /**
     * Sets the F Cost of a GraphNode
     * @param val Double value (cost)
     */
    public void setfCost(Double val) {
        this.fCost = val;
    }

    /**
     * Gets the parent node of a GraphNode
     * @return GraphNode
     */
    public GraphNode getParentNode() {
        return parent;
    }

    /**
     * Get the G Cost of the GraphNode
     * @return Double Value representing the G Cost
     */
    public double getgCost() {
        return gCost;
    }

    /**
     * Get the H Cost of the GraphNode
     * @return Double Value representing the G Cost
     */
    public double gethCost() {
        return hCost;
    }

    /**
     * Get the F Cost of the GraphNode
     * @return Double Value representing the G Cost
     */
    public double getfCost() {
        return fCost;
    }

    /**
     * Get the boolean flag value of Start
     * @return Boolean value to represent if the GraphNode is a Start Node or not.
     */
    public boolean getStartValue(){
        return this.start;
    }

    /**
     * Get the boolean flag value of Goal
     * @return Boolean value to represent if the GraphNode is a Goal Node or not.
     */
    public boolean getGoalValue(){
        return this.goal;
    }

    /**
     * Get the boolean flag value of Solid
     * @return Boolean value to represent if the GraphNode is a Solid Node or not.
     */
    public boolean getSolidValue(){
        return this.solid;
    }

    /**
     * Get the boolean flag value of Open
     * @return Boolean value to represent if the GraphNode is an Open Node or not.
     */
    public boolean getOpenValue(){
        return this.open;
    }

    /**
     * Get the boolean flag value of Checked
     * @return Boolean value to represent if the GraphNode is a Checked Node or not.
     */
    public boolean getCheckedValue(){
        return this.checked;
    }

    /**
     * Set the GraphNode's Start variable/flag to true, making it a Start Node
     */
    public void setAsStart() {
        start = true;
    }

    /**
     * Set the GraphNode's Goal variable/flag to true, making it a Goal Node
     */
    public void setAsGoal() {
        goal = true;
    }

    /**
     * Set the GraphNode's Solid variable/flag to true, making it a Solid Node
     */
    public void setAsSolid() {
        solid = true;
    }

    /**
     * Set the GraphNode's Open variable/flag to true, making it an Open Node
     */
    public void setAsOpen() {
        open = true;
    }

    /**
     * Set the GraphNode's Checked variable/flag to true, making it a Checked Node
     */
    public void setAsChecked() {
        checked = true;
    }

    public void setCheckedFalse() {
        checked = false;
    }

    /**
     * This method forces the GraphNode to return to a default state, where all G, H, F costs are 0.0
     * and all flags such as StartNode, GoalNode, SolidNode are set to false. etc.
     */
    public void setNodeToDefaultState() {
        this.setgCost(0.0);
        this.sethCost(0.0);
        this.setfCost(0.0);
        this.start = false;
        this.goal = false;
        this.solid = false;
        this.open = false;
        this.checked = false;
        this.parent = null;
    }

} // end of class