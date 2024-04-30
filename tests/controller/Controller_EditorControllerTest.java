package controller;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import view.TrafficSimulator_Editor;

import static org.junit.jupiter.api.Assertions.*;

class Controller_EditorControllerTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Test if the set and get mute sounds functionalty works.
     */
    @Test
    void test_setAndGetMuteSounds() {
        TrafficSimulator_EditorController c = new TrafficSimulator_EditorController(new TrafficSimulator_Editor());
        c.setMuteSounds(true);
        assertTrue(c.getMuteSounds(), "Sounds should be muted.");
        c.setMuteSounds(false);
        assertFalse(c.getMuteSounds(), "Sounds should be un-muted.");
    }

    /**
     * Test #2
     * Description: Test if the get and increment piece counter functionality works.
     */
    @Test
    void test_incrementAndGetPieceCounter() {
        TrafficSimulator_EditorController c = new TrafficSimulator_EditorController(new TrafficSimulator_Editor());
        assertEquals(0, c.getPieceCounterValue(), "By default, the piece counter value should be 0.");
        c.incrementPieceCounter();
        assertEquals(1, c.getPieceCounterValue(), "The piece counter value should be 1.");
        c.incrementPieceCounter();
        assertEquals(2, c.getPieceCounterValue(), "The piece counter value should be 2.");
    }
}