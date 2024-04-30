package model;

import javafx.animation.PathTransition;
import javafx.embed.swing.JFXPanel;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class Model_DriverTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Test if the default constructor for Driver works.
     * Extra notes: UUID uniqueID is random, Vehicle, HBox and PathTransition are already created by default
     */
    @Test
    void test_DefaultConstructor() {
        Driver driver = new Driver();

        assertTrue(driver.getUniqueID() instanceof UUID);
        assertEquals("left", driver.getLane(), "default driver lane is left");
        assertFalse(driver.getHazLightsOn(), "default driver should have disabled hazard lights, hazard lights that are not enabled");
    }

    /**
     * Test #2
     * Description: Test if the custom constructor for Driver works.
     * Extra notes: display boundaries does not assign to its own private data member, so it cannot be checked.
     */
    @Test
    void test_CustomConstructor() {
        UUID uniqueId = UUID.randomUUID();
        AStarGraph graph = new AStarGraph();

        // add 4 random graphnodes to the routeList of the AStarGraph instance
        for (int i = 0; i < 4; i++) {
            Double xCoord = Math.random();
            Double yCoord = Math.random();
            GraphNode n = new GraphNode(xCoord, yCoord);
            graph.addGraphNodeToList(n);
        }

        GraphNode startingNode = graph.getRouteList().get(0);
        GraphNode destinationNode = graph.getRouteList().get(3);

        Driver driver = new Driver(uniqueId, "right", graph.getRouteList(), startingNode, destinationNode, false);

        assertEquals(uniqueId, driver.getUniqueID(), "UUID should be the same");
        assertEquals("right", driver.getLane(), "lane should be equal to right");
        assertEquals(graph.getRouteList(), driver.getRouteList(), "routeList should be the same");
        assertEquals(startingNode, driver.getStartNode(), "Starting node of the driver should be the same");
        assertEquals(destinationNode, driver.getGoalNode(), "Goal node of the driver should be the same");
    }


    /**
     * Test #3
     * Description: Test if the assigned UUID can be returned
     */
    @Test
    void test_gettingTheUniqueID() {
        UUID uniqueId = UUID.randomUUID();
        AStarGraph graph = new AStarGraph();

        // add 4 random graphnodes to the routeList of the AStarGraph instance
        for (int i = 0; i < 4; i++) {
            Double xCoord = Math.random();
            Double yCoord = Math.random();
            GraphNode n = new GraphNode(xCoord, yCoord);
            graph.addGraphNodeToList(n);
        }

        GraphNode startingNode = graph.getRouteList().get(0);
        GraphNode destinationNode = graph.getRouteList().get(3);

        Driver driver = new Driver(uniqueId, "right", graph.getRouteList(), startingNode, destinationNode, false);

        assertTrue(driver.getUniqueID() instanceof UUID);

        Driver driver2 = new Driver();
        assertTrue(driver2.getUniqueID() instanceof UUID);

    }

    /**
     * Test #4
     * Description: Test if Driver's vehicle can be retrieved.
     */
    @Test
    void test_gettingTheDriverVehicle() {
        Driver driver = new Driver(); // default constructor, hence default vehicle

        assertTrue(driver.getVehicle() instanceof Vehicle);
        assertEquals("car", driver.getVehicle().getType(), "Default driver vehicle type is car");
        assertEquals("Red", driver.getVehicle().getColor(), "Default driver vehicle colour is Red");
        assertEquals(5.00, driver.getVehicle().getFuelLevel(), "Default driver vehicle fuel level is set to 5.00");
    }

    /**
     * Test #4
     * Description: Test if Driver's vehicle can be manually set.
     */
    @Test
    void test_settingTheDriverVehicle() {
        Driver driver = new Driver();
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Green");

        assertTrue(driver.getVehicle() instanceof Vehicle);
        assertEquals("car", driver.getVehicle().getType(), "Default driver vehicle type is car");
        assertEquals("Red", driver.getVehicle().getColor(), "Default driver vehicle colour is Red");
        assertEquals(5.00, driver.getVehicle().getFuelLevel(), "Default driver vehicle fuel level is set to 5.00");

        driver.setVehicle(vehicle); // set driver's vehicle to a new vehicle that's green with 5.00 litres of fuel.
        assertEquals("car", driver.getVehicle().getType(), "Default driver vehicle type is car");
        assertEquals("Green", driver.getVehicle().getColor(), "Default driver vehicle colour is Green");
        assertEquals(5.00, driver.getVehicle().getFuelLevel(), "Default driver vehicle fuel level is set to 5.00");

    }

    /**
     * Test #5
     * Description: Test if Driver's HBox can be retrieved
     */
    @Test
    void test_gettingTheDriverHBox() {
        Driver driver = new Driver();

        assertTrue(driver.getHBox() instanceof HBox);
        assertEquals(20, driver.getHBox().getMaxWidth(), "Max width of the HBox should be 20");
        assertEquals(40, driver.getHBox().getMaxHeight(), "Max height of the HBox should be 40");

        ImageView spriteImageView = (ImageView) driver.getHBox().getChildren().get(0);

        assertEquals(true, spriteImageView.getImage().getUrl().equals(driver.getVehicle().getSpriteImageView().getImage().getUrl()), "Check if the HBox driver's sprite is equal to the Driver object's vehicle sprite");

        assertTrue(driver.getPathTransition() instanceof PathTransition);
        assertEquals(Duration.seconds(20), driver.getPathTransition().getDuration(), "default path transition duration is 20 seconds");
        assertEquals(0.9, driver.getPathTransition().getRate(), "Path Transition default rate should be 0.9");


        UUID uniqueId = UUID.randomUUID();
        AStarGraph graph = new AStarGraph();

        // add 4 random graphnodes to the routeList of the AStarGraph instance
        for (int i = 0; i < 4; i++) {
            Double xCoord = Math.random();
            Double yCoord = Math.random();
            GraphNode n = new GraphNode(xCoord, yCoord);
            graph.addGraphNodeToList(n);
        }

        GraphNode startingNode = graph.getRouteList().get(0);
        GraphNode destinationNode = graph.getRouteList().get(3);

        Driver driver2 = new Driver(uniqueId, "right", graph.getRouteList(), startingNode, destinationNode, false);

        ImageView spriteImageView2 = (ImageView) driver2.getHBox().getChildren().get(0);

        assertEquals(true, spriteImageView2.getImage().getUrl().equals(driver2.getVehicle().getSpriteImageView().getImage().getUrl()), "Check if the HBox driver 2's sprite is equal to the Driver object's vehicle sprite");

        assertTrue(driver2.getPathTransition() instanceof PathTransition);
        assertEquals(Duration.seconds(30), driver2.getPathTransition().getDuration(), "default path transition duration is 30 seconds");
        assertEquals(1.1, driver2.getPathTransition().getRate(), "Path Transition default rate should be 1.1");
    }

    /**
     * Test #6
     * Description: Test if Driver's HBox can be manually set.
     */
    @Test
    void test_settingTheDriverHBox() {
        HBox newHBox = new HBox();
        Driver driver = new Driver();

        driver.setHBox(newHBox);

        assertEquals(newHBox, driver.getHBox(), "The assigned HBox should be returned");
    }

    /**
     * Test #7
     * Description: Test if the driver's routeList can be obtained.
     */
    @Test
    void test_gettingTheDriverRouteList() {
        UUID uniqueId = UUID.randomUUID();
        AStarGraph graph = new AStarGraph();

        // add 4 random graphnodes to the routeList of the AStarGraph instance
        for (int i = 0; i < 4; i++) {
            Double xCoord = Math.random();
            Double yCoord = Math.random();
            GraphNode n = new GraphNode(xCoord, yCoord);
            graph.addGraphNodeToList(n);
        }

        GraphNode startingNode = graph.getRouteList().get(0);
        GraphNode destinationNode = graph.getRouteList().get(3);

        Driver driver = new Driver(uniqueId, "left", graph.getRouteList(), startingNode, destinationNode, false);

        assertEquals(graph.getRouteList(), driver.getRouteList(), "the AStarGraph routeList and the driver's routeList should be the same");
    }

    /**
     * Test #8
     * Description: Test if the driver's routeList nodes can be reset to a default state.
     * N.B: default state meaning all the GraphNodes in the routelist have no parent and 0.0 as the G, H and F cost values and all A* pathfinding related flags are false (start, goal, solid etc.)
     */
    @Test
    void test_settingTheDriverRouteListNodesToDefaultState() {
        UUID uniqueId = UUID.randomUUID();
        AStarGraph graph = new AStarGraph();

        GraphNode one = new GraphNode(5.0, 5.0);
        GraphNode two = new GraphNode(10.0, 10.0);
        GraphNode three = new GraphNode(15.0, 15.0);

        graph.addGraphNodeToList(one);
        graph.addGraphNodeToList(two);
        graph.addGraphNodeToList(three);

        GraphNode startingNode = graph.getRouteList().get(0);
        GraphNode destinationNode = graph.getRouteList().get(2);

        Driver driver = new Driver(uniqueId, "left", graph.getRouteList(), startingNode, destinationNode, false);
        driver.setCostOnAllNodes();
        driver.setNodesToDefaultState();

        GraphNode index1 = graph.getRouteList().get(1);

        assertEquals(0.0, index1.getgCost(), "G cost should be 0.0");
        assertEquals(0.0, index1.gethCost(), "H cost should be 0.0");
        assertEquals(0.0, index1.getfCost(), "F cost should be 0.0");
        assertNull(graph.getRouteList().get(1).getParentNode(), "parent node should be null");
        assertFalse(index1.getStartValue(), "start should be false");
        assertFalse(index1.getGoalValue(), "goal should be false");
        assertFalse(index1.getSolidValue(), "solid should be false");
        assertFalse(index1.getOpenValue(), "open should be false");
        assertFalse(index1.getCheckedValue(), "checked should be false");
    }

    /**
     * Test #9
     * Description: Test if the driver's routeList can be manually set.
     */
    @Test
    void test_settingTheDriverRouteList() {
        Driver driver = new Driver();
        AStarGraph graph = new AStarGraph();

        GraphNode one = new GraphNode(5.0, 5.0);
        GraphNode two = new GraphNode(10.0, 10.0);
        GraphNode three = new GraphNode(15.0, 15.0);

        graph.addGraphNodeToList(one);
        graph.addGraphNodeToList(two);
        graph.addGraphNodeToList(three);

        assertNull(driver.getRouteList(), "Driver should have no routeList, hence null.");

        driver.setRouteList(graph.getRouteList());

        assertEquals(3, driver.getRouteList().size(), "Driver's routeList should now be set to the new routeList of the AStarGraph instance. Hence, size should be 3.");
    }


    /**
     * Test #10
     * Description: Test if the driver's lane can be manually set.
     */
    @Test
    void test_settingTheDriverLane() {
        Driver driver = new Driver();

        //default lane is left
        assertEquals("left", driver.getLane(), "Driver's default lane should be left");

        driver.setLane("right");

        assertEquals("right", driver.getLane(), "Driver's lane should now be set to right");
    }

    /**
     * Test #10
     * Description: Test if the driver's lane can be retrieved
     */
    @Test
    void test_gettingTheDriverLane() {
        Driver driver = new Driver(); // default lane is left

        assertEquals("left", driver.getLane(), "Driver's default lane should obtained as left");
    }

    /**
     * Test #10
     * Description: Test if the driver's HBOX boundaries can be enabled
     */
    @Test
    void test_settingTheDriverHBoxStyle() {
        Driver driver = new Driver();
        driver.setHBoxStyle(true);

        String style = "-fx-padding: 0;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 1;" +
                "-fx-border-color: blue;";

        assertEquals(style, driver.getHBox().getStyle(), "HBox style should match the style string as defined above");
    }

    /**
     * Test #11
     * Description: Test if the driver's path transition can be retrieved.
     */
    @Test
    void test_gettingTheDriverPathTransition() {
        Driver driver = new Driver();

        assertTrue(driver.getPathTransition() instanceof PathTransition);
    }

    /**
     * Test #12
     * Description: Test if the driver vehicle's hazard lights status can be modified
     */
    @Test
    void test_settingTheDriverVehicleHazardLightsOn() {
        Driver driver = new Driver();

        assertFalse(driver.getHazLightsOn(), "Driver vehicle's hazard lights should be off");

        driver.setHazLightsOn(true);

        assertTrue(driver.getHazLightsOn(), "Driver vehicle hazard lights should be enabled");
    }

    /**
     * Test #13
     * Description: Test if the driver vehicle's hazard lights status can be retrieved.
     */
    @Test
    void test_gettingTheDriverHazardLightsOn() {
        Driver driver = new Driver();

        assertEquals(false, driver.getHazLightsOn(), "By default, the driver vehicle's hazard lights should be off.");
    }

    /**
     * Test #14
     * Description: Test if the driver vehicle's hazard lights can be activated.
     * N.B due to the way the Activate Hazard works, it simply switches hazard lights on and off to create the blinking light effect
     */
    @Test
    void test_activatingTheDriverVehicleHazardMode() {
        Driver driver = new Driver();

        driver.activateHazardMode();
        assertFalse(driver.getHazLightsOn(), "Driver vehicle hazard light ON status should be enabled.");
    }

    /**
     * Test #15
     * Description: Checking the accuracy of the toString method
     */
    @Test
    void test_ToString() {
        UUID uniqueId = UUID.randomUUID();
        AStarGraph graph = new AStarGraph();

        GraphNode one = new GraphNode(5.0, 5.0);
        GraphNode two = new GraphNode(10.0, 10.0);
        GraphNode three = new GraphNode(15.0, 15.0);

        graph.addGraphNodeToList(one);
        graph.addGraphNodeToList(two);
        graph.addGraphNodeToList(three);

        GraphNode startingNode = graph.getRouteList().get(0);
        GraphNode destinationNode = graph.getRouteList().get(2);

        Driver driver = new Driver(uniqueId, "left", graph.getRouteList(), startingNode, destinationNode, false);
        driver.setCostOnAllNodes();

        String toStr = driver.toString();

        assertTrue( toStr.startsWith("Driver:[") &&
                toStr.contains(driver.getUniqueID().toString()) &&
                toStr.contains(driver.getVehicle().toString()) &&
                toStr.contains(driver.getHBox().toString()) &&
                toStr.contains(driver.getLane()) &&
                toStr.contains(driver.getRouteList().toString()) &&
                toStr.contains(driver.getStartNode().toString()) &&
                toStr.contains(driver.getGoalNode().toString()) &&
                toStr.contains(driver.getCurrentNode().toString()) &&
                toStr.contains(driver.getPathTransition().toString()) &&
                toStr.endsWith("]"), "The toString method should be in the standard convention format");
    }

    /**
     * Test #16
     * Description: Setting the start node from a graphnode
     */
    @Test
    void test_settingTheStartNode() {
        GraphNode n = new GraphNode(5.0, 5.0);
        assertFalse(n.getStartValue(), "Start flag should be false");
        n.setAsStart();
        assertTrue(n.getStartValue(), "Start flag should be true");
    }

    /**
     * Test #17
     * Description: Setting the goal node from a graphnode
     */
    @Test
    void test_settingTheGoalNode() {
        GraphNode n = new GraphNode(5.0, 5.0);
        assertFalse(n.getGoalValue(), "Goal flag should be false");
        n.setAsGoal();
        assertTrue(n.getGoalValue(), "Goal flag should be true");
    }

    /**
     * Test #18
     * Description: Getting the start node test
     */
    @Test
    void test_gettingTheStartNode() {
        GraphNode n = new GraphNode(5.0, 5.0);
        Driver driver = new Driver();
        driver.setStartNode(n);

        assertEquals(driver.getStartNode(), n, "The driver should return the start node, which would be the graphnode initially set (n).");
    }

    /**
     * Test #19
     * Description: Getting the goal node test
     */
    @Test
    void test_gettingTheGoalNode() {
        GraphNode n = new GraphNode(5.0, 5.0);
        Driver driver = new Driver();
        driver.setGoalNode(n);

        assertEquals(driver.getGoalNode(), n, "The driver should return the goal node, which would be the graphnode initially set (n)");
    }

    /**
     * Test #20
     * Description: Getting the current node test
     */
    @Test
    void test_gettingTheCurrentNode() {
        GraphNode n = new GraphNode(5.0, 5.0);
        Driver driver = new Driver();
        driver.setCurrentNode(n);

        assertEquals(driver.getCurrentNode(), n, "The driver should return the current node, which would be the graphnode initially set (n)");
    }

    /**
     * Test #20
     * Description: Getting the costs of a GraphNode (G, H, F)
     * N.B: also tests the setCostOnAllNodes method.
     */
    @Test
    void test_gettingTheCost() {
        UUID uniqueId = UUID.randomUUID();
        AStarGraph graph = new AStarGraph();

        GraphNode one = new GraphNode(5.0, 5.0);  // index 0
        GraphNode two = new GraphNode(10.0, 10.0);  // index 1
        GraphNode three = new GraphNode(15.0, 15.0);  // index 2

        graph.addGraphNodeToList(one);
        graph.addGraphNodeToList(two);
        graph.addGraphNodeToList(three);

        GraphNode startingNode = graph.getRouteList().get(0);
        GraphNode destinationNode = graph.getRouteList().get(2);

        Driver driver = new Driver(uniqueId, "left", graph.getRouteList(), startingNode, destinationNode, false);
        driver.setCostOnAllNodes();

        assertEquals(0.0, graph.getRouteList().get(0).getgCost(), "G Cost should be 0.0 as starting node is the current node");
        assertEquals(20.0, graph.getRouteList().get(0).gethCost(), "H cost should be 10, as its the distance from start node to goal node, difference of 10 pixels");
        assertEquals(20.0, graph.getRouteList().get(0).getfCost(), "F cost is G + H");
    }

    /**
     * Test #21
     * Description: Testing the pathfinding of the A* algorithm with 4 GraphNodes
     * N.B also tests trackThePath() method by checking if the first node in the path is the start node and the last node in the path is the goal node.
     */
    @Test
    void test_autoSearch() {
        UUID uniqueId = UUID.randomUUID();
        AStarGraph graph = new AStarGraph();

        GraphNode one = new GraphNode("left_1", 5.0, 5.0);  // index 0
        GraphNode two = new GraphNode("left_2", 5.0, 10.0);   // index 1
        GraphNode three = new GraphNode("left_3", 5.0, 15.0);  // index 2
        GraphNode four = new GraphNode("left_4", 5.0, 20.0);  // index 3

        graph.addGraphNodeToList(one);
        graph.addGraphNodeToList(two);
        graph.addGraphNodeToList(three);
        graph.addGraphNodeToList(four);

        GraphNode startingNode = graph.getRouteList().get(0);
        GraphNode destinationNode = graph.getRouteList().get(3);

        Driver driver = new Driver(uniqueId, "left", graph.getRouteList(), startingNode, destinationNode, false);
        driver.setCostOnAllNodes();
        ArrayList<GraphNode> path = driver.autoSearch();
        System.out.println(path);

        assertEquals(path.getFirst(), startingNode, "The returned path's first node should be the start node that was provided");
        assertEquals(path.getLast(), destinationNode, "The returned path's last node should be the goal node that was provided");

    }

    /**
     * Test #22
     * Description: Testing the open node method, which is essential to the A* pathfinding autoSearch method.
     */
    @Test
    void test_openingTheNode() {
        UUID uniqueId = UUID.randomUUID();
        AStarGraph graph = new AStarGraph();

        GraphNode one = new GraphNode("left_1", 5.0, 5.0);  // index 0
        GraphNode two = new GraphNode("left_2", 5.0, 10.0);   // index 1
        GraphNode three = new GraphNode("left_3", 5.0, 15.0);  // index 2
        GraphNode four = new GraphNode("left_4", 5.0, 20.0);  // index 3

        graph.addGraphNodeToList(one);
        graph.addGraphNodeToList(two);
        graph.addGraphNodeToList(three);
        graph.addGraphNodeToList(four);

        GraphNode startingNode = graph.getRouteList().get(0);
        GraphNode destinationNode = graph.getRouteList().get(3);

        Driver driver = new Driver(uniqueId, "left", graph.getRouteList(), startingNode, destinationNode, false);
        driver.setCostOnAllNodes();
        driver.openNode(two);
        assertEquals(true, two.getOpenValue(), "graphnode N should be opened (true)");
    }

    /**
     * Test #23
     * Description: Testing the fetch node from routeList method, you can pass X and Y coordinates here
     */
    @Test
    void test_fetchingNodeWithXAndYValues() {
        AStarGraph graph = new AStarGraph();

        GraphNode one = new GraphNode("left_1", 5.0, 5.0);  // index 0
        GraphNode two = new GraphNode("left_2", 5.0, 10.0);   // index 1
        GraphNode three = new GraphNode("left_3", 5.0, 15.0);  // index 2
        GraphNode four = new GraphNode("left_4", 5.0, 20.0);  // index 3

        graph.addGraphNodeToList(one);
        graph.addGraphNodeToList(two);
        graph.addGraphNodeToList(three);
        graph.addGraphNodeToList(four);

        Driver driver = new Driver();
        driver.setRouteList(graph.getRouteList());

        GraphNode fetchedNode = driver.fetchNodeWithXAndY(5.0, 10.0, "left");

        assertEquals(two, fetchedNode, "The fetched node should have X coordinate of 5.0 and Y coordinate of 10.0.");
    }
}