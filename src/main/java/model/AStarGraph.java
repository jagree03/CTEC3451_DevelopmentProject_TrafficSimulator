package model;

import java.io.Serializable;
import java.util.ArrayList;

public class AStarGraph implements Serializable {

    // FIELDS
    private ArrayList<GraphNode> routeList; // has all graph nodes in the scenario


    // CONSTRUCTORS

    /**
     * Default constructor
     * Creates instance of routeList (ArrayList)
     */
    public AStarGraph() {
        this.routeList = new ArrayList<GraphNode>();
    }

    // METHODS

    /**
     * This method returns the routeList
     * @return routeList of type ArrayList<GraphNode>
     */
    public ArrayList<GraphNode> getRouteList() {
        return routeList;
    }

    /**
     * A debug method, prints out the routeList nodes in a readable format to the console.
     */
    public void getRouteListFormatted() {
        System.out.println("===================================");
        for (GraphNode n : this.getRouteList()) {
            System.out.println(n.getId() + "  " + n.getXCoordinate() + " " + n.getYCoordinate());
        }
        System.out.println("===================================");
    }

    /**
     * This method adds a passed in GraphNode to the routeList.
     * @param n GraphNode
     */
    public void addGraphNodeToList(GraphNode n) {
        routeList.add(n);
    }

    /**
     * This method clears the routeList so that it becomes empty.
     */
    public void clearGraphNodeList() {
        routeList.clear();
    }

    /**
     * This method returns a toString representation of the object
     * @return String representation of the AStarGraph
     */
    @Override
    public String toString() {
        return "AStarGraph:[routeList=" + routeList + "]";
    }


    //////////////////////
    // A* Pathfinding related methods
    //////////////////////

    // this method may not be necessary to be used
    // not in Driver class, as it doesn't make sense. A driver should only have a start and a goal (destination)
    public void setSolidNode(GraphNode n) { // passing a graphnode, sets it as a solid
        n.setAsSolid();
    }

}
