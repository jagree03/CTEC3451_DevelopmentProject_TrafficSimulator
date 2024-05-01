package model;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class Model_TrafficLightTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Check if the object can be instantiated with the default constructor.
     */
    @Test
    void test_DefaultConstructor() {
        TrafficLight t = new TrafficLight();
        assertEquals("none", t.getCurrentSignal(), "Current signal should be none");
        assertFalse(t.getActivatedState(), "Should not be activated by default.");
    }

    /**
     * Test #2
     * Description: Check if the object can be instantiated with the custom constructor.
     */
    @Test
    void test_CustomConstructor() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        TrafficLight t = new TrafficLight(imgv);
        assertEquals(file.toURI().toString(), t.getImageView().getImage().getUrl().toString(), "URL's should match.");
        assertEquals("none", t.getCurrentSignal(), "Current signal should be none");
        assertFalse(t.getActivatedState(), "Should not be activated by default.");
    }

    /**
     * Test #3
     * Description: Get the image view of the traffic light
     */
    @Test
    void test_getImageView() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        TrafficLight t = new TrafficLight(imgv);
        assertEquals(imgv, t.getImageView(), "ImageView's should match.");
    }

    /**
     * Test #4
     * Description: Get the current signal of the traffic light
     */
    @Test
    void test_getCurrentSignal() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        TrafficLight t = new TrafficLight(imgv);
        assertEquals("none", t.getCurrentSignal(), "Current signal should be none");
    }

    /**
     * Test #5
     * Description: Set the current signal of the traffic light
     */
    @Test
    void test_setCurrentSignal() {
        TrafficLight t = new TrafficLight();
        assertEquals("none", t.getCurrentSignal(), "Current signal should be none");
        t.setCurrentSignal("red");
        assertEquals("red", t.getCurrentSignal(), "Current signal should be red");
    }

    /**
     * Test #6
     * Description: set and get the activated state of the traffic light
     */
    @Test
    void test_setAndGetActivatedState() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        TrafficLight t = new TrafficLight(imgv);
        t.setActivatedState(true);
        assertTrue(t.getActivatedState(), "Traffic Light should be activated.");
    }

    /**
     * Test #7
     * Description: activate the red stop signal for the traffic light
     */
    @Test
    void test_activateRedStopSignal() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        TrafficLight t = new TrafficLight(imgv);
        t.activateRedStopSignal();
        assertEquals("red", t.getCurrentSignal(), "current signal should be red");
    }

    /**
     * Test #8
     * Description: activate the amber wait signal for the traffic light
     */
    @Test
    void test_activateAmberStopSignal() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        TrafficLight t = new TrafficLight(imgv);
        t.activateAmberStopSignal();
        assertEquals("amber", t.getCurrentSignal(), "current signal should be amber");
    }

    /**
     * Test #9
     * Description: activate the green go signal for the traffic light
     */
    @Test
    void test_activateGreenStopSignal() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        TrafficLight t = new TrafficLight(imgv);
        t.activateGreenStopSignal();
        assertEquals("green", t.getCurrentSignal(), "current signal should be green");
    }

    /**
     * Test #10
     * Description: activate the traffic lights
     */
    @Test
    void test_activateTrafficLight() throws InterruptedException {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        TrafficLight t = new TrafficLight(imgv);
        t.activateTrafficLight(true);
        assertTrue(t.getActivatedState(), "Traffic light should be activated");
    }

    /**
     * Test #11
     * Description: Check the accuracy of the toString method
     */
    @Test
    void test_ToString() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        TrafficLight t = new TrafficLight(imgv);
        String toStr = t.toString();

        assertTrue( toStr.startsWith("TrafficLight:[") &&
                toStr.contains(t.getCurrentSignal()) &&
                toStr.contains(""+t.getActivatedState()) &&
                toStr.contains(t.getImageView().getImage().getUrl()) &&
                toStr.endsWith("]"), "The toString method should be in the standard convention format");
    }
}