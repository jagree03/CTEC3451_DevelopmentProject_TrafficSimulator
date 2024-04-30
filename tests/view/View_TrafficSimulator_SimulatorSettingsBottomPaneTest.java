package view;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_SimulatorSettingsBottomPaneTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing


    /**
     * Test #1
     * Description: Test if the number of drivers can be returned from the input box as an integer value
     */
    @Test
    void test_getNumberOfDrivers() {
        TrafficSimulator_SimulatorSettingsBottomPane t = new TrafficSimulator_SimulatorSettingsBottomPane();
        assertEquals(1, t.getNumberOfDrivers(), "By default, number of drivers should be 1");
    }

    /**
     * Test #2
     * Description: Test if the fuel level value provided by the user in the input box can be retrieved as a double value
     */
    @Test
    void test_getUserFuelLevel() {
        TrafficSimulator_SimulatorSettingsBottomPane t = new TrafficSimulator_SimulatorSettingsBottomPane();
        assertEquals(5.0, t.getUserFuelLevel(), "By default, fuel level should be 5.0");
    }

    /**
     * Test #3
     * Description: Test if the default slider value for the Car Spawn chance is equal to 50%
     */
    @Test
    void test_getCarSpawnChanceSliderValue() {
        TrafficSimulator_SimulatorSettingsBottomPane t = new TrafficSimulator_SimulatorSettingsBottomPane();
        assertEquals(50, t.getCarSpawnChanceSliderValue(), "By default, car spawn chance should be set to 50%");
    }

    /**
     * Test #4
     * Description: Test if the default slider value for the Van Spawn chance is equal to 50%
     */
    @Test
    void test_getVanSpawnChanceSliderValue() {
        TrafficSimulator_SimulatorSettingsBottomPane t = new TrafficSimulator_SimulatorSettingsBottomPane();
        assertEquals(50, t.getVanSpawnChanceSliderValue(), "By default, van spawn chance should be set to 50%");
    }
}