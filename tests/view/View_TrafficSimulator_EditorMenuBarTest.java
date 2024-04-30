package view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.MenuItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_EditorMenuBarTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Test if the current Exit Menu Bar Item instance can be retrieved.
     */
    @Test
    void test_getExitItem() {
        TrafficSimulator_EditorMenuBar m = new TrafficSimulator_EditorMenuBar();
        assertTrue(m.getExitItem() instanceof MenuItem, "Exit item should be returned");
    }

    /**
     * Test #2
     * Description: Test if the current Clear Scenario Menu Bar Item instance can be retrieved.
     */
    @Test
    void test_getClearScenarioItem() {
        TrafficSimulator_EditorMenuBar m = new TrafficSimulator_EditorMenuBar();
        assertTrue(m.getClearScenarioItem() instanceof MenuItem, "Clear Scenario menu bar item should be returned");
    }


    /**
     * Test #3
     * Description: Test if the current Display Nodes menu item name can be both set and retrieved.
     */
    @Test
    void test_setAndGetDisplayNodesItemName() {
        TrafficSimulator_EditorMenuBar m = new TrafficSimulator_EditorMenuBar();
        m.setDisplayNodesItemName("one");
        assertEquals("one", m.getDisplayNodesItemName(), "item name returned, should be equal to one");
    }

}