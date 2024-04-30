package view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.MenuItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_SimulationMenuBarTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Test if the exit menu bar item can be returned
     */
    @Test
    void test_getExitItem() {
        TrafficSimulator_SimulationMenuBar m = new TrafficSimulator_SimulationMenuBar();
        assertTrue(m.getExitItem() instanceof MenuItem, "Should return the exit menu item");
    }

    /**
     * Test #2
     * Description: Test if the Display Bounding Menu Item can set and also retrieved
     */
    @Test
    void test_setAndGetDisplayBoundingMenuItemName() {
        TrafficSimulator_SimulationMenuBar m = new TrafficSimulator_SimulationMenuBar();
        m.setDisplayBoundingMenuItemName("one");
        assertEquals("one", m.getDisplayBoundingMenuItemName(), "The menu item name should be equal to one");
        m.setDisplayBoundingMenuItemName("two");
        assertEquals("two", m.getDisplayBoundingMenuItemName(), "The menu item name should be equal to two");
    }
}