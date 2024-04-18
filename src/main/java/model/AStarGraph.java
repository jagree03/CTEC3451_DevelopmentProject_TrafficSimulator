package model;

import java.io.Serializable;
import java.util.ArrayList;

public class AStarGraph implements Serializable {

    // FIELDS
    private ArrayList<GraphNode> routeList; // has all graph nodes in the scenario


    // CONSTRUCTORS
    public AStarGraph() {
        this.routeList = new ArrayList<GraphNode>();
    }

    // METHODS
    public ArrayList<GraphNode> getRouteList() {
        return routeList;
    }

    public void getRouteListFormatted() {
        System.out.println("===================================");
        for (GraphNode n : this.getRouteList()) {
            System.out.println(n.getId() + "  " + n.getXCoordinate() + " " + n.getYCoordinate());
        }
        System.out.println("===================================");
    }

    public void addGraphNodeToList(GraphNode n) {
        routeList.add(n);
    }

    public void clearGraphNodeList() {
        routeList.clear();
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
