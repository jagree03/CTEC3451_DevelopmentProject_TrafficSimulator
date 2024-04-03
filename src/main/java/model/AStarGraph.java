package model;

import java.util.ArrayList;

public class AStarGraph {

    private GraphNode startNode;
    private GraphNode goalNode;
    private GraphNode currentNode;

    private ArrayList<GraphNode> routeList;

    public AStarGraph() {
        this.routeList = new ArrayList<GraphNode>();
    }

    public void setStartNode(GraphNode n) {
        startNode = n;
        currentNode = startNode;
    }

    public void setGoalNode(GraphNode n) {
        goalNode = n;
    }

    /*
    public void setSolidNode(GraphNode n) {
        n.setAsSolid();
    }
    */

    public ArrayList<GraphNode> getRouteList() {
        return routeList;
    }

    public void addGraphNodeToList(GraphNode n) {
        routeList.add(n);
    }

    public void clearGraphNodeList() {
        routeList.clear();
    }


}
