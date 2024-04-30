package view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulatorRootMenuButtonsTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing
    /**
     * Test #1
     * Description: Test if the Editor clickable icon imageview is returned (this is the imageview that switches the scene to the main environment editor screen)
     */
    @Test
    void test_getShowEditor() {
        TrafficSimulatorRootMenuButtons t = new TrafficSimulatorRootMenuButtons();
        assertTrue(t.getShowEditor() instanceof ImageView, "Should return an ImageView instance");
    }

    /**
     * Test #2
     * Description: Test if the Procedural clickable icon imageview is returned (this is the imageview that switches the scene to the main procedural generation screen)
     */
    @Test
    void test_getShowProcedural() {
        TrafficSimulatorRootMenuButtons t = new TrafficSimulatorRootMenuButtons();
        assertTrue(t.getShowProcedural() instanceof ImageView, "Should return an ImageView instance");
    }
}