package view;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_SimulatorSettingsTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing


    /**
     * Test #1
     * Description: Test if the menu bar instance can be returned
     */
    @Test
    void test_getTSS_MenuBar() {
        TrafficSimulator_SimulatorSettings t = new TrafficSimulator_SimulatorSettings();
        assertTrue(t.getTSS_MenuBar() instanceof TrafficSimulatorRootMenuBar,"Should return the root menu bar instance");
    }

    /**
     * Test #2
     * Description: Test if the bottom pane instance can be returned
     */
    @Test
    void test_getTSS_BottomPane() {
        TrafficSimulator_SimulatorSettings t = new TrafficSimulator_SimulatorSettings();
        assertTrue(t.getTSS_BottomPane() instanceof TrafficSimulator_SimulatorSettingsBottomPane,"Should return the bottompane instance");
    }

    /**
     * Test #3
     * Description: Test if the editor pane instance can be returned
     */
    @Test
    void test_getTSS_EditorPane() {
        TrafficSimulator_SimulatorSettings t = new TrafficSimulator_SimulatorSettings();
        assertTrue(t.getTSS_EditorPane() instanceof TrafficSimulator_EditorEditorPane,"Should return the Editor Pane instance");
    }
}