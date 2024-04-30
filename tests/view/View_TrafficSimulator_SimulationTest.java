package view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.MenuBar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_SimulationTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Test if the menu bar can be returned
     */
    @Test
    void test_getTSS_MenuBar() {
        TrafficSimulator_Simulation t = new TrafficSimulator_Simulation();
        assertTrue(t.getTSS_MenuBar() instanceof TrafficSimulator_SimulationMenuBar, "Should return a menubar instance");
    }

    /**
     * Test #2
     * Description: Test if the editor pane can be returned
     */
    @Test
    void test_getTSS_EditorPane() {
        TrafficSimulator_Simulation t = new TrafficSimulator_Simulation();
        assertTrue(t.getTSS_EditorPane() instanceof TrafficSimulator_EditorEditorPane, "Should return a EditorPane instance");
    }

    /**
     * Test #3
     * Description: Test if the bottom pane can be returned
     */
    @Test
    void test_getTSS_BottomPane() {
        TrafficSimulator_Simulation t = new TrafficSimulator_Simulation();
        assertTrue(t.getTSS_BottomPane() instanceof TrafficSimulator_SimulationBottomPane, "Should return a menubar instance");
    }
}