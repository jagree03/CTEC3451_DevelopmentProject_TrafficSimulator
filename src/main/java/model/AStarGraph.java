package model;

import java.io.Serializable;
import java.util.ArrayList;

public class AStarGraph implements Serializable {

    private ArrayList<GraphNode> routeList; // has all graph nodes in the scenario


    public AStarGraph() {
        this.routeList = new ArrayList<GraphNode>();
    }


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

}
