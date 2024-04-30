package model;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class Model_VehicleTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Checking to see if the default constructor works
     */
    @Test
    void test_DefaultConstructor() {
        Vehicle v = new Vehicle();
        assertTrue(v.getSprite().getUrl().contains("car_red.png"), "By default, a vehicle should have a red car image.");
        assertEquals("car", v.getType(), "By default, a vehicle should be of car type.");
        assertEquals("Red", v.getColor(), "By default, a vehicle should be of Red colour.");
        assertEquals(5.00, v.getFuelLevel(), "By default, a vehicle should have fuel level of 5.00 litres");
    }

    /**
     * Test #2
     * Description: Checking to see if the custom constructor works
     */
    @Test
    void test_CustomConstructor1() {
        Vehicle v = new Vehicle("van", "Grey", 5.5);
        assertTrue(v.getSprite().getUrl().contains("van_grey.png"), "vehicle should have a grey van image.");
        assertEquals("van", v.getType(), "Vehicle should be of van type.");
        assertEquals("Grey", v.getColor(), "vehicle should be of Grey colour.");
        assertEquals(5.5, v.getFuelLevel(), "vehicle should have fuel level of 5.5 litres");
    }

    /**
     * Test #3
     * Description: Checking to see if the second custom constructor works
     */
    @Test
    void test_CustomConstructor2() {
        Vehicle v = new Vehicle("car",  6.0);
        assertEquals("car", v.getType(), "Vehicle should be of van type.");
        assertEquals(6.0, v.getFuelLevel(), "vehicle should have fuel level of 5.5 litres");
    }

    /**
     * Test #4
     * Description: Checking to see if the third custom constructor works, this is specifically for the StatisticsController.
     */
    @Test
    void test_CustomConstructor_ForStatisticsController() {
        File file = new File("img\\vehicles\\car\\car_green.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        Vehicle v = new Vehicle("car", imgv.getImage().getUrl(), "Green", 4.0);
        assertEquals("car", v.getType(), "Vehicle should be of car type.");
        assertTrue(v.getSprite().getUrl().contains("car_green.png"), "the sprite should point to car_green.png");
        assertEquals("Green", v.getColor(), "vehicle should be of Green colour.");
        assertEquals(4.0, v.getFuelLevel(), "vehicle should have fuel level of 4.0 litres");
    }

    /**
     * Test #5
     * Description: getting and setting a type for the vehicle
     */
    @Test
    void test_setAndGetType() {
        Vehicle v = new Vehicle("van", "Grey", 5.5);

        assertEquals("van", v.getType(), "Vehicle should be of van type.");

        v.setType(0); // 0 for car, 1 for van

        assertEquals("car", v.getType(), "Vehicle should be of car type.");

    }

    /**
     * Test #6
     * Description: setting a RANDOM type for the vehicle (van/car)
     */
    @Test
    void test_setARandomType() {
        Vehicle v = new Vehicle();
        v.setARandomType(100, 0);
        assertEquals("car", v.getType(), "Vehicle should be of car type, due to car spawn chance being set to 100%");

        v.setARandomType(0, 100);
        assertEquals("van", v.getType(), "Vehicle should be of van type, due to van spawn chance being set to 100%");

    }


    /**
     * Test #7
     * Description: setting and getting a colour
     */
    @Test
    void test_setAndGetColor() {
        Vehicle v = new Vehicle();
        assertEquals("Red", v.getColor(), "vehicle should be of Red colour.");
        v.setColor("Black");
        assertEquals("Black", v.getColor(), "vehicle should be of Black colour.");
        v.setColor("Orange");
        assertEquals("Orange", v.getColor(), "vehicle should be of Orange colour.");
    }

    /**
     * Test #8
     * Description: Getting a random colour to be assigned to the vehicle.
     * N.B van type vehicles only have one colour "Grey".
     */
    @Test
    void test_getRandomColour() {
        Vehicle car = new Vehicle("car", 12.0);
        String colour = car.getRandomColour();

        while (!colour.equals("Orange")) {
            colour = car.getRandomColour();
        }

        assertEquals("Orange",  colour);
    }

    /**
     * Test #9
     * Description: setting and getting the sprite of the vehicle
     */
    @Test
    void test_setAndGetSprite() {
        Vehicle v = new Vehicle();
        File file = new File("img\\vehicles\\car\\car_purple.png");
        ImageView imgv = new ImageView(new Image(file.toURI().toString()));

        assertTrue(v.getSprite().getUrl().contains("car_red.png"), "Expected that a default vehicle instance should contain a url to car_red.png");

        v.setSprite(imgv.getImage());
        assertTrue(v.getSprite().getUrl().contains("car_purple.png"), "Vehicle instance should contain a url to car_purple.png");
        assertEquals(imgv.getImage(), v.getSprite(), "The initialized imageview above's image and the sprite of the vehicle should be identical.");

    }


    /**
     * Test #10
     * Description: getting the ImageView node of the vehicle
     */
    @Test
    void test_getSpriteImageView() {
        Vehicle v = new Vehicle("car", "Red", 5.0);

        assertTrue(v.getSpriteImageView() instanceof ImageView);
    }

    /**
     * Test #11
     * Description: setting and getting the fuel level of the vehicle
     */
    @Test
    void test_setAndGetFuelLevel() {
        Vehicle v = new Vehicle("car", "Red", 5.00);
        assertEquals(5.00, v.getFuelLevel(), "vehicle should have fuel level of 5.00 litres");

        v.setFuelLevel(10.00);
        assertEquals(10.00, v.getFuelLevel(), "vehicle should have fuel level of 10.00 litres");

    }

    /**
     * Test #12
     * Description: Checking the accuracy of the toString Method
     */
    @Test
    void test_ToString() {
        Vehicle v = new Vehicle();
        String toStr = v.toString();

        assertTrue( toStr.startsWith("Vehicle:[") &&
                toStr.contains(v.getType()) &&
                toStr.contains(v.getColor()) &&
                toStr.contains(v.getSpriteImageView().getImage().getUrl()) &&
                toStr.contains(""+v.getFuelLevel()) &&
                toStr.endsWith("]"), "The toString method should be in the standard convention format");
    }
}