package view;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_EditorEditorPaneTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Test if the current EditorPane instance can be retrieved.
     */
    @Test
    void test_getCurrentEditorPane() {
        TrafficSimulator_EditorEditorPane t = new TrafficSimulator_EditorEditorPane();
        assertTrue(t.getCurrentEditorPane() instanceof  TrafficSimulator_EditorEditorPane, "Should return EditorPane instance");
    }
}