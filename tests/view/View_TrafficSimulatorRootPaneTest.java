package view;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulatorRootPaneTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing


    /**
     * Test #1
     * Description: Test if the RootMenuBar instance is returned
     */
    @Test
    void test_getTSMenuBar() {
        TrafficSimulatorRootPane t = new TrafficSimulatorRootPane();
        assertTrue(t.getTSMenuBar() instanceof TrafficSimulatorRootMenuBar, "Should return a RootMenuBar instance");
    }

    /**
     * Test #2
     * Description: Test if the RootMenuButtons instance is returned
     */
    @Test
    void test_getTSMenuButtons() {
        TrafficSimulatorRootPane t = new TrafficSimulatorRootPane();
        assertTrue(t.getTSMenuButtons() instanceof TrafficSimulatorRootMenuButtons, "Should return the RootMenuButtons instance");
    }

    /**
     * Test #3
     * Description: Test if the RootMenuBottomButtonPanel instance is returned
     */
    @Test
    void test_getTSMenuButtonsBottomButtonPanel() {
        TrafficSimulatorRootPane t = new TrafficSimulatorRootPane();
        assertTrue(t.getTSMenuButtonsBottomButtonPanel() instanceof TrafficSimulatorRootMenuBottomButtonPanel, "Should return the RootMenuBottomButtonPanel instance");
    }
}