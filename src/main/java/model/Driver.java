package model;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.w3c.dom.Node;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Driver {

    // fields
    private Vehicle vehicle;
    private boolean hazLightsOn;

    private HBox h;
    private ArrayList<GraphNode> routeList;

    private String lane;

    // A* Pathfinding related variables
    private GraphNode startNode; // every driver starts somewhere
    private GraphNode goalNode; // every driver needs a destination to go to
    private GraphNode currentNode; // every driver stores what node they are currently on

    private boolean goalReached = false;
    private int step = 0;
    private ArrayList<GraphNode> openList; // is a copy of the routeList that contains all graphnodes in the scenario
    private ArrayList<GraphNode> checkedList;

    private PathTransition pt; // to play/pause movement of the driver upon the path.


    // constructors
    public Driver() { // default constructor has a driver with a red car who drives on left lane
        this.vehicle = new Vehicle();
        this.lane = "left";
        this.hazLightsOn = false;
        this.h = new HBox();
        h.setAlignment(Pos.CENTER);
        h.getChildren().add(vehicle.getSpriteImageView());
        h.setMaxSize(20, 40);

        // VEHICLE ROTATION LISTENERS
        h.translateXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!(oldValue.doubleValue() == 0.0)) {
                    if (newValue.doubleValue() > oldValue.doubleValue()) {
                        //System.out.println("initiate 90 degree rotate");
                        h.setRotate(90);
                    }
                    if (newValue.doubleValue() < oldValue.doubleValue()) {
                        h.setRotate(270);
                    }
                }
            }
        });

        h.translateYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println("entered    " + "oldVal: " + oldValue.doubleValue() + "   newVal: " + newValue.doubleValue());
                if (!(oldValue.doubleValue() == 0.0)) {
                    if (newValue.doubleValue() < oldValue.doubleValue()) {
                        //System.out.println("initiate rotation removal, so back to 0 degrees");
                        h.setRotate(0);
                    }
                    if (newValue.doubleValue() > oldValue.doubleValue()) {
                        h.setRotate(180);
                    }
                }
            }
        });

        this.pt = new PathTransition();
        pt.setNode(this.getHBox());
        pt.setOrientation(PathTransition.OrientationType.NONE);
        pt.setDuration(Duration.seconds(20));
        pt.setRate(0.9);
        pt.setCycleCount(PathTransition.INDEFINITE);
    }

    /**
     * Custom constructor where you can provide lane of the driver, and which node should start in the scenario,
     * goal node and also provide all nodes in the scenario so the driver can calculate the shortest path, as well as a parameter
     * that can let you enable or disable the view of the bounding boxes around the driver's vehicle.
     * @param lane Lane of the driver - left or right.
     * @param listOfAllNodes A list of all nodes in the scenario
     * @param startingNode The starting node, where the driver starts from.
     * @param destinationNode The goal node, where the driver needs to drive to.
     * @param displayBoundaries A debug parameter that lets you see the boundaries of the driver's vehicle, can be enabled or disabled, true or false.
     */
    public Driver(String lane, ArrayList<GraphNode> listOfAllNodes, GraphNode startingNode, GraphNode destinationNode, Boolean displayBoundaries) {
        this.vehicle = new Vehicle();
        this.hazLightsOn = false;
        this.lane = lane;
        this.setStartNode(startingNode);
        this.setGoalNode(destinationNode);
        this.routeList = listOfAllNodes; // takes the list of all graphnodes in the scenario
        this.openList = new ArrayList<GraphNode>(); // openList holds all UN-CHECKED nodes ready for evaluation
        this.checkedList = new ArrayList<GraphNode>(); // checkedList holds all CHECKED nodes, nodes checked to find
        // the most promising node from startnode to goalnode.

        this.h = new HBox();
        h.setAlignment(Pos.BOTTOM_CENTER);
        h.getChildren().add(vehicle.getSpriteImageView());
        h.setMaxSize(20, 40);

        if (displayBoundaries) {
            this.setHBoxStyle(true);
        } else {
            this.setHBoxStyle(false);
        }

        // VEHICLE ROTATION LISTENERS
        h.translateXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!(oldValue.doubleValue() == 0.0)) {
                    if (newValue.doubleValue() > oldValue.doubleValue()) {
                        //System.out.println("initiate 90 degree rotate");
                        h.setRotate(90);
                    }
                    if (newValue.doubleValue() < oldValue.doubleValue()) {
                        h.setRotate(270);
                    }
                }
            }
        });

        h.translateYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println("entered    " + "oldVal: " + oldValue.doubleValue() + "   newVal: " + newValue.doubleValue());
                if (!(oldValue.doubleValue() == 0.0)) {
                    if (newValue.doubleValue() < oldValue.doubleValue()) {
                        //System.out.println("initiate rotation removal, so back to 0 degrees");
                        h.setRotate(0);
                    }
                    if (newValue.doubleValue() > oldValue.doubleValue()) {
                        h.setRotate(180);
                    }
                }
            }
        });

        this.pt = new PathTransition();
        pt.setNode(this.getHBox());
        pt.setOrientation(PathTransition.OrientationType.NONE);
        pt.setDuration(Duration.seconds(30));
        pt.setRate(1.1);
        pt.setCycleCount(PathTransition.INDEFINITE);

        for (GraphNode n : routeList) {
            if (this.getLane().equals("left")) {
                if (n.getId().contains("right")) {
                    n.setAsSolid();
                }
            } else {
                if (n.getId().contains("left")) {
                    n.setAsSolid();
                }
            }
        }
    }



    // methods
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        ImageView img = (ImageView) this.getHBox().getChildren().get(0);
        img.setImage(vehicle.getSprite());
        h.setMaxSize(20, 40);
    }

    public HBox getHBox() {
        return h;
    }

    public void setHBox (HBox h) {
        this.h = h;
    }

    public ArrayList<GraphNode> getRouteList() {
        return this.routeList;
    }

    /**
     * Resets all nodes of the graph to a default state
     */
    public void setNodesToDefaultState() {
        for (GraphNode n : this.getRouteList()) {
            n.setNodeToDefaultState();
        }
    }


    public void setRouteList(ArrayList<GraphNode> routeList) {
        this.routeList = routeList;
    }

    public Double getCurrentTranslateX() {
        return h.getTranslateX();
    }

    public Double getCurrentTranslateY() {
        return h.getTranslateY();
    }

    public Double getCurrentPositionX() {
        return this.getVehicle().getSpriteImageView().getX();
    }

    public Double getCurrentPositionY() {
        return this.getVehicle().getSpriteImageView().getY();
    }

    public void setLane(String value) {
        this.lane = value;
    }

    public String getLane() {
        return lane;
    }

    /**
     * Displays a surrounding box around the vehicle.
     * @param value A true or false value to enable or disable the borders.
     */
    public void setHBoxStyle(Boolean value) {
        if (value) {
            h.setStyle("-fx-padding: 0;" +
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 1;" +
                    "-fx-border-color: blue;");
        } else {
            h.setStyle("-fx-padding: 0;" +
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 0;" +
                    "-fx-border-radius: 0;" +
                    "-fx-border-color: black;");
        }
    }


    /**
     * A method to return the PathTransition of a Driver
     * @return PathTransition Instance of the driver
     */
    public PathTransition getPathTransition() {
        return pt;
    }

    /**
     * Set's the path transition of this Driver instance with the passed in PathTransition
     * @param pt PathTransition object
     */
    public void setPathTransition(PathTransition pt) {
        this.pt = pt;
    }

    /**
     * Bounds listener
     * @param listener ChangeListener for boundaries of the HBox
     */
    public void addNodeBoundsListener(ChangeListener<Bounds> listener) {
        this.getHBox().boundsInParentProperty().addListener(listener);
    }

    public void setHazLightsOn(boolean value) {
        this.hazLightsOn = value;
    }

    public boolean getHazLightsOn() {
        return hazLightsOn;
    }

    public void activateHazardMode() {
        HBox h = this.getHBox();
        ImageView veh = (ImageView) h.getChildren().getFirst();
        String type = this.getVehicle().getType();
        String vehColor = this.getVehicle().getColor();

        File file_lightsOn = new File("img\\vehicles\\car\\car_" + vehColor.toLowerCase() + "_hazon.png");
        File file_lightsOff = new File("img\\vehicles\\car\\car_" + vehColor.toLowerCase() + ".png");

        File file_VanlightsOn = new File("img\\vehicles\\car\\van_grey_hazon.png");
        File file_VanlightsOff = new File("img\\vehicles\\car\\van_grey.png");


        ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);

        Runnable task1 = () -> {

            if (!type.equals("van")) {
                if (this.getHazLightsOn() == false) {
                    veh.setImage(new Image(file_lightsOn.toURI().toString()));
                    this.setHazLightsOn(true);
                } else {
                    veh.setImage(new Image(file_lightsOff.toURI().toString()));
                    this.setHazLightsOn(false);
                }
            } else {
                if (this.getHazLightsOn() == false) {
                    veh.setImage(new Image(file_VanlightsOn.toURI().toString()));
                    this.setHazLightsOn(true);
                } else {
                    veh.setImage(new Image(file_VanlightsOff.toURI().toString()));
                    this.setHazLightsOn(false);
                }
            }
        };

        //run this task after 1 second
        ses.schedule(task1, 1, TimeUnit.SECONDS);

        ses.shutdown();
    }

    /**
     * ToString method, indicates the state of each of the data members of the Driver class
     * @return String representing states of the key variables of a Driver.
     */
    @Override
    public String toString() {
        return "Driver:[Vehicle=" + vehicle + ", HBox=" + h + ", routeList=" + routeList + ", lane=" + lane +
                ", startNode=" + startNode + ", goalNode=" + goalNode + ", currentNode=" + currentNode +
                ", goalReached=" + goalReached + ", step=" + step + ", openList=" + openList +
                ", checkedList=" + checkedList + ", PathTransition=" + pt + "]";
    }


    /////////////////////////////////
    // A* pathfinding related methods
    /////////////////////////////////

    public void setStartNode(GraphNode n) {
        n.setAsStart(); // set this graphnode's StartNode identifier to true
        this.startNode = n;
        this.currentNode = startNode;
    }

    public void setGoalNode(GraphNode n) {
        n.setAsGoal(); // set this graphnode's GoalNode identifier to true
        this.goalNode = n;
    }

    public void setCurrentNode(GraphNode n) {
        this.currentNode = n;
    }

    public GraphNode getStartNode() {
        return startNode;
    }

    public GraphNode getGoalNode() {
        return goalNode;
    }

    public GraphNode getCurrentNode() {
        return currentNode;
    }





    // every driver needs to calculate its own path from start node to goal node
    public void getCost(GraphNode n) {
        // GET G COST (the distance of current node 'n' FROM the start node)
        double xDistance = Math.abs(n.getXCoordinate() - startNode.getXCoordinate());
        double yDistance = Math.abs(n.getYCoordinate() - startNode.getYCoordinate());
        n.setgCost(xDistance + yDistance); // set this node's G cost using the x and y distances.

        // GET H COST (the distance of current node 'n' TO the goal node)
        xDistance = Math.abs(n.getXCoordinate() - goalNode.getXCoordinate());
        yDistance = Math.abs(n.getYCoordinate() - goalNode.getYCoordinate());
        n.sethCost(xDistance + yDistance); // set this node's H cost using the x and y distances.

        // GET F COST (The total cost of G cost + H cost)
        n.setfCost(n.getgCost() + n.gethCost());

        // Debug method, can be commented out
//        if (n != startNode && n != goalNode) {
//            System.out.println("F: " + n.getfCost() + "   " + "G: " + n.getgCost());
//        }
    }

    public void setCostOnAllNodes() {
        boolean destgoal = false;
        for (GraphNode n : this.getRouteList()) {
            getCost(n); // for all nodes in the routelist (a.k.a, the list that contains all graphnodes in the scenario),
            // calculate the 3 costs for the A* pathfinding algorithm
            // each driver has a different startnode and goalnode, so there will be different costs calculated for each driver instance
            if (n.getId().contains("destinationGoal")) {
                destgoal = true;
            }
        }
        System.out.println("Destination goal nodes found: " + destgoal);
    }


    // every time we call the search method we set the currentnode that the driver is on, as checked
    public void search() { // this method is called every time we evaluate & check a new graphnode
        // first we check if we have reached the goal or not
        if (goalReached == false && step < 300) {
            // then get the current node's x and y positions (coordinates)
            double x = currentNode.getXCoordinate();
            double y = currentNode.getYCoordinate();

            currentNode.setAsChecked(); // set the current node as checked, checked meaning, we've already stepped on it or driven on it as a best node candidate to get the shortest path
            // so we don't really need to evaluate it anymore
            // so we add all the checked nodes to the checked list (arraylist of graphnodes)
            checkedList.add(currentNode); // add the current node to the list of checked nodes
            openList.remove(currentNode); // remove the node from the list of unchecked nodes, the open node list.

            // now we start to OPEN nodes for evaluation, for checking..

            // we only do this, when nodes are available to be opened in these directions, top, bottom, left, right
            // but there may be the case that a node may not exist there, so that is handled here.

            // open the node at the top (up)
            GraphNode nodeTop = fetchNodeWithXAndY(x, y-5, this.getLane());
            if (nodeTop.getXCoordinate() != 0.0 && nodeTop.getYCoordinate() != 0.0) { // if node's x is 0.0 and node y is 0.0, this means the node doesn't exist, in the routelist...
                // aka which is the list of all nodes in the scenario
                this.openNode(nodeTop); // if nodes coords are NOT equal to (0.0, 0.0) this means a node does exist and can be OPENED for evaluation
            }


            // open the node at the left
            GraphNode nodeLeft = fetchNodeWithXAndY(x-5, y, this.getLane());
            if (nodeLeft.getXCoordinate() != 0.0 && nodeLeft.getYCoordinate() != 0.0) {
                this.openNode(nodeLeft);
            }


            // open the node at the bottom (down)
            GraphNode nodeBottom = fetchNodeWithXAndY(x, y+5, this.getLane());
            if (nodeBottom.getXCoordinate() != 0.0 && nodeBottom.getYCoordinate() != 0.0) {
                this.openNode(nodeBottom);
            }


            // open the node at the right
            GraphNode nodeRight = fetchNodeWithXAndY(x+5, y, this.getLane());
            if (nodeRight.getXCoordinate() != 0.0 && nodeRight.getYCoordinate() != 0.0) {
                this.openNode(nodeRight);
            }

            // if nodes are available in the directions, then they will be OPENED and added to the openList.
            // so the nodes in the open list can be compared to find out which node has the best f cost or g cost.

            // FIND THE BEST NODE
            int bestNodeIndex = 0;
            double bestNodeFCost = 999.0; // initial best f cost

            for (int i = 0; i < openList.size(); i++) {
                // check if this node's F cost is better
                // we basically scan the nodes that we put in the openList, check if each node's f cost is lower than the current best node f cost.
                if (openList.get(i).getfCost() < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).getfCost(); // automatically the first node should become the best node
                    // but maybe the next node is even better
                }
                // if the node f cost is equal to the current best node f cost, we then check the g cost value.
                else if (openList.get(i).getfCost() == bestNodeFCost) {
                    if (openList.get(i).getgCost() < openList.get(bestNodeIndex).getgCost()) {
                        bestNodeIndex = i;
                    }
                }
            }
            // now when this for loop is finished, we now get the (best) node via best node index which is our next step
            currentNode = openList.get(bestNodeIndex);

            // now we got to check if this currentnode is the goal node or not
            if (currentNode == goalNode) { // if the current node is the goal node
                goalReached = true; // we've reached the goal.
            }
        }
        step++;
    }

    public ArrayList<GraphNode> autoSearch() { // a copy of the method above: search() but uses a while loop to find best node candidates until the goal is reached
        this.setNodesToDefaultState();
        setCostOnAllNodes();
        ArrayList<GraphNode> p = new ArrayList<>(); // p holds the final path for the driver from start node to goal node
        while (goalReached == false && step < 500) {
            // debug
            System.out.println("Driver's current lane: " + this.getLane());

            // then get the current node's x and y positions (coordinates)
            double x = currentNode.getXCoordinate();
            double y = currentNode.getYCoordinate();

            currentNode.setAsChecked(); // set the current node as checked, checked meaning, we've already stepped on it or driven on it as a best node candidate to get the shortest path
            // so we don't really need to evaluate it anymore
            // so we add all the checked nodes to the checked list (arraylist of graphnodes)
            checkedList.add(currentNode); // add the current node to the list of checked nodes
            openList.remove(currentNode); // remove the node from the list of unchecked nodes, the open node list.

            // now we start to OPEN nodes for evaluation, for checking..

            // we only do this, when nodes are available to be opened in these directions, top, bottom, left, right
            // but there may be the case that a node may not exist there, so that is handled here.

            // open the node at the top (up)
            GraphNode nodeTop = fetchNodeWithXAndY(x, y - 5, this.getLane());
            if (nodeTop.getXCoordinate() != 0.0 && nodeTop.getYCoordinate() != 0.0) { // if node's x is 0.0 and node y is 0.0, this means the node doesn't exist, in the routelist...
                // aka which is the list of all nodes in the scenario
                this.openNode(nodeTop); // if nodes coords are NOT equal to (0.0, 0.0) this means a node does exist and can be OPENED for evaluation
            }


            // open the node at the left
            GraphNode nodeLeft = fetchNodeWithXAndY(x - 2, y, this.getLane());
            if (nodeLeft.getXCoordinate() != 0.0 && nodeLeft.getYCoordinate() != 0.0) {
                this.openNode(nodeLeft);
            } else { // if x = 0.0 and y = 0.0 - then it could possibly be a transition to a 90 degree straight, so subtract 7 to the x value as we're going left, but keep y the same
                nodeLeft = fetchNodeWithXAndY(x - 5, y, this.getLane());
                if (nodeLeft.getXCoordinate() != 0.0 && nodeLeft.getYCoordinate() != 0.0) {
                    this.openNode(nodeLeft);
                } else {
                    nodeLeft = fetchNodeWithXAndY(x - 7, y, this.getLane());
                    if (nodeLeft.getXCoordinate() != 0.0 && nodeLeft.getYCoordinate() != 0.0) {
                        this.openNode(nodeLeft);
                    }
                }
            }


            // open the node at the bottom (down)
            GraphNode nodeBottom = fetchNodeWithXAndY(x, y + 5, this.getLane());
            if (nodeBottom.getXCoordinate() != 0.0 && nodeBottom.getYCoordinate() != 0.0) {
                this.openNode(nodeBottom);
            }


            // open the node at the right
            GraphNode nodeRight = fetchNodeWithXAndY(x + 2, y, this.getLane());
            if (nodeRight.getXCoordinate() != 0.0 && nodeRight.getYCoordinate() != 0.0) {
                this.openNode(nodeRight);
            } else { // if x = 0.0 and y = 0.0 - then it could possibly be a transition to a 90 degree straight, so add 7 to the x value as we're going right, but keep y the same
                nodeRight = fetchNodeWithXAndY(x + 5, y, this.getLane());
                if (nodeRight.getXCoordinate() != 0.0 && nodeRight.getYCoordinate() != 0.0) {
                    this.openNode(nodeRight);
                } else {
                    nodeRight = fetchNodeWithXAndY(x + 7, y, this.getLane());
                    if (nodeRight.getXCoordinate() != 0.0 && nodeRight.getYCoordinate() != 0.0) {
                        this.openNode(nodeRight);
                    }
                }
            }

            // if nodes are available in the directions, then they will be OPENED and added to the openList.
            // so the nodes in the open list can be compared to find out which node has the best f cost or g cost.

            // FIND THE BEST NODE
            int bestNodeIndex = 0;
            double bestNodeFCost = 99999.0; // initial best f cost

            for (int i = 0; i < openList.size(); i++) {
                // check if this node's F cost is better
                // we basically scan the nodes that we put in the openList, check if each node's f cost is lower than the current best node f cost.
                if (openList.get(i).getfCost() < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).getfCost(); // automatically the first node should become the best node
                    // but maybe the next node is even better
                }
                // if the node f cost is equal to the current best node f cost, we then check the g cost value.
                else if (openList.get(i).getfCost() == bestNodeFCost) {
                    if (openList.get(i).getgCost() < openList.get(bestNodeIndex).getgCost()) {
                        bestNodeIndex = i;
                    }
                }
            }
            // now when this for loop is finished, we now get the (best) node via best node index which is our next step towards the goal
            // essentially we get a new current node.
            currentNode = openList.get(bestNodeIndex);

            // debug method to print out the size of the checked lists
            System.out.println("checked: " + checkedList.size());

            // now we got to check if this currentnode is the goal node or not
            if (currentNode == goalNode) { // if the current node is the goal node
                System.out.println("goal found");
                goalReached = true; // we've reached the goal.
                p = trackThePath();
            }
            step++;
        }
        return p; // return the final path from start node to goal node for the driver to drive on
    }

    // say we're at the startnode, the beginning, the currentnode is also the startnode, so we need to evaluate
    // the adjacent nodes - when we first evaluate the node - first we change it to an OPEN state which means
    // this node is currently OPEN for evaluation and checking.
    // so we use this method.
    public void openNode(GraphNode n) {
         if (n.getOpenValue() == false && n.getCheckedValue() == false && n.getSolidValue() == false) { // if node hasn't opened yet, and it hasn't been checked
            // if the node is not opened yet, add it to the open list
             n.setAsOpen(); // we set the node to open state
             n.setParentNode(currentNode); // also we set this node's parent as the current node
             // we need this to track back the path when we reach the goal node and finally we
             // add the node to the open list.
             openList.add(n);
         }
    }

    public GraphNode fetchNodeWithXAndY(double x, double y, String lane) {
        GraphNode foundNode = new GraphNode(0.0, 0.0); // initialize a temporary graphnode
        for (GraphNode n : this.getRouteList()) {
            if (n.getXCoordinate() == x && n.getYCoordinate() == y && n.getId().contains(lane)) {
                foundNode = n; // if the node was found, update the temporary graphnode to the found node
                break; // break out of the loop
            } else if (n.getXCoordinate() == x && n.getYCoordinate() == y && n.getId().contains("d")) {  // if a node is found and contains the id "d" meaning destination
                foundNode = n;  // accept this as a found node.
                break; // and break out of the for loop
            }
        }
        return foundNode; // return the graphnode that was found from the list of all nodes in the scenario
    }

    // final method to backtrack the nodes to get the path
    public ArrayList<GraphNode> trackThePath() {
        ArrayList<GraphNode> path = new ArrayList<>();
        GraphNode current = goalNode; // we backtrack, so we start from the goalnode
        path.add(current); // add the goal node to the path

        while (current != startNode) { // while the current node is not equal to the startnode
            current = current.getParentNode(); // update this current node to the current node's assigned parent (backtrack or previous node)
            path.add(current); // add this parent node, then loop back around to get the parent's parent node etc.
            // this happens until the current node is equal to the start node, so the while loop will stop at this point.
        }

        path.add(startNode);

        Collections.reverse(path); // reverse the path
        return path; // return the reversed path as the startnode is the last node in the path and goalnode is the first,
        // but startnode should be the first, and the last node should be the goalnode.
    }
}
