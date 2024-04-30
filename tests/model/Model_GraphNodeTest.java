package model;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Model_GraphNodeTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Checking the default constructor
     */
    @Test
    void test_DefaultConstructor() {
        GraphNode n = new GraphNode(1.0, 1.0);

        assertEquals("1", n.getId(), "Default ID should be 1");
        assertEquals(1.0, n.getXCoordinate(), "X coord should be 1.0");
        assertEquals(1.0, n.getYCoordinate(), "Y coord should be 1.0");
    }

    /**
     * Test #2
     * Description: Checking the custom constructor
     */
    @Test
    void test_CustomConstructor() {
        GraphNode n = new GraphNode("new", 1.5, 2.0);

        assertEquals("new", n.getId(), "ID should be new");
        assertEquals(1.5, n.getXCoordinate(), "X coord should be 1.5");
        assertEquals(2.0, n.getYCoordinate(), "Y coord should be 2.0");
    }

    /**
     * Test #3
     * Description: Get the X coord of the GraphNode
     */
    @Test
    void test_getXCoordinate() {
        GraphNode n = new GraphNode("new", 1.5, 2.0);
        assertEquals(1.5, n.getXCoordinate(), "X coord should be 1.5");
    }

    /**
     * Test #3
     * Description: Get the Y coord of the GraphNode
     */
    @Test
    void test_getYCoordinate() {
        GraphNode n = new GraphNode("new", 1.5, 2.0);
        assertEquals(2.0, n.getYCoordinate(), "Y coord should be 2.0");
    }

    /**
     * Test #4
     * Description: Set and Get the ID of the graphnode
     */
    @Test
    void test_setAndGetId() {
        GraphNode n = new GraphNode("new", 1.5, 2.0);
        assertEquals("new", n.getId(), "ID should be new");
        n.setId("new2");
        assertEquals("new2", n.getId(), "ID should be new2");
    }

    /**
     * Test #5
     * Description: Checking the accuracy of the toString method
     */
    @Test
    void test_ToString() {
        GraphNode n = new GraphNode(5.0, 5.0);
        String toStr = n.toString();

        assertTrue( toStr.startsWith("GraphNode:[") &&
                toStr.contains(n.getId()) &&
                toStr.contains(""+n.getXCoordinate()) &&
                toStr.contains(""+n.getYCoordinate()) &&
                toStr.endsWith("]"), "The toString method should be in the standard convention format");
    }

    /**
     * Test #6
     * Description: Setting and getting the parent node
     */
    @Test
    void test_setAndGetParentNode() {
        GraphNode n = new GraphNode(5.0, 5.0);
        GraphNode n2 = new GraphNode(5.0, 10.0);

        n2.setParentNode(n);

        assertEquals(n, n2.getParentNode(), "Parent node of n2 should be GraphNode n");
    }

    /**
     * Test #7
     * Description: Setting and getting the G cost of a graphnode
     */
    @Test
    void test_setAndGetGCost() {
        GraphNode n = new GraphNode(5.0, 5.0);

        assertEquals(0.0, n.getgCost());

        n.setgCost(5.0);

        assertEquals(5.0, n.getgCost());
    }

    /**
     * Test #8
     * Description: Setting and getting the H cost of a graphnode
     */
    @Test
    void test_setAndGetHCost() {
        GraphNode n = new GraphNode(5.0, 5.0);

        assertEquals(0.0, n.gethCost());

        n.sethCost(5.0);

        assertEquals(5.0, n.gethCost());
    }

    /**
     * Test #9
     * Description: Setting and getting the F cost of a graphnode
     */
    @Test
    void test_setAndGetFCost() {
        GraphNode n = new GraphNode(5.0, 5.0);

        assertEquals(0.0, n.getfCost());

        n.setfCost(5.0);

        assertEquals(5.0, n.getfCost());
    }

    /**
     * Test #10
     * Description: getting the Start Flag value of a GraphNode
     */
    @Test
    void test_getStartValue() {
        GraphNode n = new GraphNode(5.0, 5.0);

        assertFalse(n.getStartValue(), "GraphNode n should have Start flag as false.");
    }

    /**
     * Test #11
     * Description: getting the Goal Flag value of a GraphNode
     */
    @Test
    void test_getGoalValue() {
        GraphNode n = new GraphNode(5.0, 5.0);

        assertFalse(n.getGoalValue(), "GraphNode n should have Goal flag as false.");
    }

    /**
     * Test #11
     * Description: getting the Solid Flag value of a GraphNode
     */
    @Test
    void test_getSolidValue() {
        GraphNode n = new GraphNode(5.0, 5.0);

        assertFalse(n.getSolidValue(), "GraphNode n should have Solid flag as false.");
    }

    /**
     * Test #12
     * Description: getting the Solid Flag value of a GraphNode
     */
    @Test
    void test_getOpenValue() {
        GraphNode n = new GraphNode(5.0, 5.0);

        assertFalse(n.getOpenValue(), "GraphNode n should have Open flag as false.");
    }

    /**
     * Test #13
     * Description: getting the Solid Flag value of a GraphNode
     */
    @Test
    void test_getCheckedValue() {
        GraphNode n = new GraphNode(5.0, 5.0);

        assertFalse(n.getCheckedValue(), "GraphNode n should have Checked flag as false.");
    }

    /**
     * Test #13
     * Description: Setting the start flag value of a graphnode
     */
    @Test
    void test_setGraphNodeAsStart() {
        GraphNode n = new GraphNode(5.0, 5.0);
        assertFalse(n.getStartValue(), "GraphNode n should have Start flag as false.");
        n.setAsStart();
        assertTrue(n.getStartValue(), "GraphNode n should have Start flag as true.");
    }

    /**
     * Test #14
     * Description: Setting the goal flag value of a graphnode
     */
    @Test
    void test_setGraphNodeAsGoal() {
        GraphNode n = new GraphNode(5.0, 5.0);
        assertFalse(n.getGoalValue(), "GraphNode n should have Goal flag as false.");
        n.setAsGoal();
        assertTrue(n.getGoalValue(), "GraphNode n should have Goal flag as true.");
    }

    /**
     * Test #15
     * Description: Setting the Solid flag value of a graphnode
     */
    @Test
    void test_setGraphNodeAsSolid() {
        GraphNode n = new GraphNode(5.0, 5.0);
        assertFalse(n.getSolidValue(), "GraphNode n should have Solid flag as false.");
        n.setAsSolid();
        assertTrue(n.getSolidValue(), "GraphNode n should have Solid flag as true.");
    }

    /**
     * Test #16
     * Description: Setting the Open flag value of a graphnode
     */
    @Test
    void test_setGraphNodeAsOpen() {
        GraphNode n = new GraphNode(5.0, 5.0);
        assertFalse(n.getOpenValue(), "GraphNode n should have Open flag as false.");
        n.setAsOpen();
        assertTrue(n.getOpenValue(), "GraphNode n should have Open flag as true.");
    }

    /**
     * Test #16
     * Description: Setting the Checked flag value of a graphnode
     */
    @Test
    void test_setGraphNodeAsChecked() {
        GraphNode n = new GraphNode(5.0, 5.0);
        assertFalse(n.getCheckedValue(), "GraphNode n should have Checked flag as false.");
        n.setAsChecked();
        assertTrue(n.getCheckedValue(), "GraphNode n should have Checked flag as true.");
    }

    /**
     * Test #17
     * Description: Setting the GraphNode to a default state
     * N.B: default state meaning all the GraphNodes in the routelist have no parent and 0.0 as the G, H and F cost values and all A* pathfinding related flags are false (start, goal, solid etc.)
     */
    @Test
    void test_setNodeToDefaultState() {
        GraphNode n = new GraphNode(5.0, 5.0);
        GraphNode n2 = new GraphNode(5.0, 10.0);
        n.setAsStart();
        n2.setAsGoal();
        n.setgCost(100.0);
        n.sethCost(100.0);
        n.setfCost(100.0);
        n2.setParentNode(n);

        n.setNodeToDefaultState();
        n2.setNodeToDefaultState();

        assertEquals(0.0, n.getgCost(), "G Cost should now be 0.0");
        assertEquals(0.0, n.gethCost(), "H Cost should now be 0.0");
        assertEquals(0.0, n.getfCost(), "F Cost should now be 0.0");
        assertFalse(n.getStartValue(), "Start flag should be false");
        assertFalse(n.getGoalValue(), "Goal flag should be false");
        assertFalse(n.getSolidValue(), "Solid flag should be false");
        assertFalse(n.getOpenValue(), "Open flag should be false");
        assertFalse(n.getCheckedValue(), "Checked flag should be false");

        assertEquals(0.0, n2.getgCost(), "G Cost should now be 0.0");
        assertEquals(0.0, n2.gethCost(), "H Cost should now be 0.0");
        assertEquals(0.0, n2.getfCost(), "F Cost should now be 0.0");
        assertFalse(n2.getStartValue(), "Start flag should be false");
        assertFalse(n2.getGoalValue(), "Goal flag should be false");
        assertFalse(n2.getSolidValue(), "Solid flag should be false");
        assertFalse(n2.getOpenValue(), "Open flag should be false");
        assertFalse(n2.getCheckedValue(), "Checked flag should be false");
        assertEquals(null, n2.getParentNode(), "parent node should be null");
    }
}