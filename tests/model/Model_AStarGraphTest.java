package model;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Model_AStarGraphTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing


    /**
     * Test #1
     * Description: Test if the default constructor for AStarGraph works.
     */
    @Test
    void test_DefaultConstructorTest() {
        AStarGraph graph = new AStarGraph();

        assertTrue(graph.getRouteList().isEmpty(), "routeList should be initialized but empty");
    }

    /**
     * Test #2
     * Description: Giving the routeList 4 random GraphNodes, then checking if the retrieval of the routeList is functional.
     */
    @Test
    void test_gettingTheRouteList() {
        AStarGraph graph = new AStarGraph();

        // add 4 random graphnodes to the routeList
        for (int i = 0; i < 4; i++) {
            Double xCoord = Math.random();
            Double yCoord = Math.random();
            GraphNode n = new GraphNode(xCoord, yCoord);
            graph.addGraphNodeToList(n);
        }

        System.out.println("test_gettingTheRouteList");
        System.out.println("========================");
        graph.getRouteList().stream().forEach(c -> System.out.println(c.getXCoordinate() + " " + c.getYCoordinate()));
        System.out.println("========================");

        // retrieve the list's size
        assertEquals(4, graph.getRouteList().size(), "Graph should return an arrayList routeList of size 4, containing 4 random GraphNodes.");
    }

    /**
     * Test #3
     * Description: Checking if you can add a graphNode to the routeList of an AStarGraph instance
     */
    @Test
    void test_addingAGraphNodeToList() {
        AStarGraph graph = new AStarGraph();

        GraphNode a = new GraphNode("one",5.0, 5.0);
        GraphNode b = new GraphNode ("two",10.0, 10.0);

        graph.addGraphNodeToList(a);
        graph.addGraphNodeToList(b);

        assertEquals("one", graph.getRouteList().get(0).getId(), "The first GraphNode in the routeList should have the id of one");
        assertEquals("two", graph.getRouteList().get(1).getId(), "The second GraphNode in the routeList should have the id of two");
    }

    /**
     * Test #4
     * Description: Checking if a populated routeList of an AStarGraph instance can be cleared and thus be empty.
     */
    @Test
    void test_clearingTheGraphNodeList() {
        AStarGraph graph = new AStarGraph();

        // add 100 random graphnodes to the routeList
        for (int i = 0; i < 100; i++) {
            Double xCoord = Math.random();
            Double yCoord = Math.random();
            GraphNode n = new GraphNode(xCoord, yCoord);
            graph.addGraphNodeToList(n);
        }

        assertEquals(100, graph.getRouteList().size(), "Size of the routeList in the AStarGraph instance should be 100, as it has been populated" +
                "with 100 graphnodes");

        // execute clear method
        graph.clearGraphNodeList();

        assertEquals(0, graph.getRouteList().size(), "After executing the clear method, the routeList should now be empty.");
        assertTrue(graph.getRouteList().isEmpty(), "routeList in the AStarGraph instance should be empty.");
    }

    /**
     * Test #5
     * Description: Checking the accuracy of the toString method
     */
    @Test
    void test_ToStringMethod() {
        AStarGraph graph = new AStarGraph();
        String toStr = graph.toString();

        assertTrue( toStr.startsWith("AStarGraph:[") &&
                        toStr.contains(graph.getRouteList().toString()) &&
                        toStr.endsWith("]"), "The toString method should be in the standard convention format");
    }
}