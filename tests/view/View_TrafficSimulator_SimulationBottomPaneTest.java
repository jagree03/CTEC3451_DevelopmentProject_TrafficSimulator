package view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_SimulationBottomPaneTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Test if Play/Pause button of the simulation screen can be returned.
     */
    @Test
    void test_getStartAndPauseButton() {
        TrafficSimulator_SimulationBottomPane t = new TrafficSimulator_SimulationBottomPane();
        assertTrue(t.getStartAndPauseButton() instanceof Button, "Should return a Button node instance");
    }
}