package view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_EditorBottomPaneTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Test if the get and set Rotation Image works.
     * N.B - tests both setRotationImage methods, one that accepts a URI and the other that accepts an Image instance
     */
    @Test
    void test_getAndSetRotationImage() {
        TrafficSimulator_EditorBottomPane t = new TrafficSimulator_EditorBottomPane();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        t.setRotationImage(petrol.getUrl().toString());

        assertEquals(petrol.getUrl().toString(), t.getRotationImage().getUrl(), "Expected to get back the same URI.");

        File file2 = new File("img\\2_EditorScreen\\destinations\\dest_5_hotel.png");
        Image hotel = new Image(file2.toURI().toString());

        t.setRotationImage(hotel);
        assertEquals(hotel, t.getRotationImage(), "Expected to get back the same image that was assigned earlier (hotel)");

    }
}