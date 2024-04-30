package view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.MenuItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulatorRootMenuBarTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing
    /**
     * Test #1
     * Description: Test if the Exit menu item from the RootMenuBar is returned successfully
     */
    @Test
    void test_getExitItem() {
        TrafficSimulatorRootMenuBar t = new TrafficSimulatorRootMenuBar();
        assertTrue(t.getExitItem() instanceof MenuItem, "Should return the Exit Item which is of MenuItem type");
    }
}