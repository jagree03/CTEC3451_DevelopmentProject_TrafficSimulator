package view;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_StatisticsTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing


    /**
     * Test #1
     * Description: Test to get the DropDownMenu pane for statistics window
     */
    @Test
    void test_getTSSDDM() throws IOException {
        TrafficSimulator_Statistics t = new TrafficSimulator_Statistics();
        assertTrue(t.getTSSDDM() instanceof TrafficSimulator_StatisticsDropDownMenu, "Return an intance of the DropDownMenuPane");
    }

    /**
     * Test #2
     * Description: Test to get the BottomPane for the statistics window
     */
    @Test
    void test_getTSSBP() throws IOException {
        TrafficSimulator_Statistics t = new TrafficSimulator_Statistics();
        assertTrue(t.getTSSBP() instanceof TrafficSimulator_StatisticsBottomPane, "Return an instance of the BottomPane for the statistics window");
    }
}