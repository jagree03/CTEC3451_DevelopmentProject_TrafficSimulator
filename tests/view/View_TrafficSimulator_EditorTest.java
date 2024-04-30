package view;

import controller.TrafficSimulator_EditorController;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class View_TrafficSimulator_EditorTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Test if the get menu bar method works
     */
    @Test
    void test_getTSE_MenuBar() {
        TrafficSimulator_Editor editor = new TrafficSimulator_Editor();
        assertTrue(editor.getTSE_MenuBar() instanceof TrafficSimulator_EditorMenuBar, "Should return the MenuBar");
    }

    /**
     * Test #2
     * Description: Test if the get bottom pane method works
     */
    @Test
    void test_getTSE_BottomPane() {
        TrafficSimulator_Editor editor = new TrafficSimulator_Editor();
        assertTrue(editor.getTSE_BottomPane() instanceof TrafficSimulator_EditorBottomPane, "Should return the BottomPane");
    }

    /**
     * Test #3
     * Description: Test if the editor pane method works
     */
    @Test
    void test_getTSE_EditorPane() {
        TrafficSimulator_Editor editor = new TrafficSimulator_Editor();
        assertTrue(editor.getTSE_EditorPane() instanceof TrafficSimulator_EditorEditorPane, "Should return the editorpane");
    }
}