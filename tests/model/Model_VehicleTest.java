package model;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Model_VehicleTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    @org.junit.Test
    // Retrieving the vehicle
    public void test_GettingTheVehicle() {
        Vehicle one = new Vehicle();
        assertEquals(true, one.getSprite().getUrl().contains("car_red.png"), "A default vehicle should have a red car sprite");
        assertEquals("Red", one.getColor(), "A default vehicle should be of red colour");
        assertEquals(5.0, one.getFuelLevel(), "A default vehicle should have 5 litres of fuel.");
        assertEquals(false, one.getLeftIndicatorStatus(), "A default vehicle should begin with their left indicator being off");
        assertEquals(false, one.getRightIndicatorStatus(), "A default vehicle should begin with their right indicator being off");
    }

    @org.junit.Test
    // Setting up the vehicle
    public void test_SettingTheVehicle() {
        Vehicle two = new Vehicle();
        two.setColor("Blue");
        assertEquals("Blue", two.getColor(), "Vehicle colour should be blue");
        two.setFuelLevel(10.0);
        assertEquals(10.0, two.getFuelLevel(), "Fuel level should be set to 10.0 Litres");
    }

    @org.junit.Test
    public void test_leftIndicator() {
        Vehicle three = new Vehicle("car", "Green", 5.0);

        three.setLeftIndicatorStatus(true);
        assertEquals(true, three.getLeftIndicatorStatus(), "Left Indicator should be enabled");
        assertEquals(true, three.getSprite().getUrl().contains("leftIndicatorOn.png"), "As left indicator is on, expected that the sprite changes to reflect that the left indicator is on.");
        three.setLeftIndicatorStatus(false);
        assertEquals(false, three.getLeftIndicatorStatus(), "Left Indicator should be disabled");
        assertEquals(false, three.getSprite().getUrl().contains("leftIndicatorOn.png"), "As left indicator is now off, expected that the sprite changes to reflect that the left indicator is off. go to default sprite based on colour");
    }

    @org.junit.Test
    public void test_rightIndicator() {
        Vehicle four = new Vehicle("car", "Purple", 5.0);

        four.setRightIndicatorStatus(true);
        assertEquals(true, four.getRightIndicatorStatus(), "Right Indicator should be enabled");
        assertEquals(true, four.getSprite().getUrl().contains("rightIndicatorOn.png"), "As right indicator is on, expected that the sprite changes to reflect that the right indicator is on.");
        four.setRightIndicatorStatus(false);
        assertEquals(false, four.getRightIndicatorStatus(), "Right Indicator should be disabled");
        assertEquals(false, four.getSprite().getUrl().contains("rightIndicatorOn.png"), "As right indicator is now off, expected that the sprite changes to reflect that the right indicator is off. go to default sprite based on colour");
    }

    @org.junit.Test
    public void test_CreatingAVan() {
        Vehicle five = new Vehicle("van", "grey", 12.0);
        assertEquals("van", five.getType(), "Type is a van");
        assertEquals(12.0, five.getFuelLevel(), "Fuel level is 12.0");
        assertEquals(true, five.getSprite().getUrl().contains("van_grey.png"), "the sprite is a grey van");
    }

    @org.junit.Test
    public void test_VanLeftIndicator() {
        Vehicle van = new Vehicle("van", "grey", 12.0);

        van.setLeftIndicatorStatus(true);
        assertEquals(true, van.getLeftIndicatorStatus(), "Left Indicator should be enabled");
        assertEquals(true, van.getSprite().getUrl().contains("van_grey_leftIndicatorOn.png"), "As left indicator is on, expected that the sprite changes to reflect that the left indicator is on.");
        van.setLeftIndicatorStatus(false);
        assertEquals(false, van.getLeftIndicatorStatus(), "Left Indicator should be disabled");
        assertEquals(false, van.getSprite().getUrl().contains("van_grey_leftIndicatorOn.png"), "As left indicator is now off, expected that the sprite changes to reflect that the left indicator is off. go to default sprite that is grey");
    }

    @org.junit.Test
    public void test_VanRightIndicator() {
        Vehicle van = new Vehicle("van", "grey", 12.0);

        van.setRightIndicatorStatus(true);
        assertEquals(true, van.getRightIndicatorStatus(), "Right Indicator should be enabled");
        assertEquals(true, van.getSprite().getUrl().contains("van_grey_rightIndicatorOn.png"), "As right indicator is on, expected that the sprite changes to reflect that the right indicator is on.");
        van.setRightIndicatorStatus(false);
        assertEquals(false, van.getRightIndicatorStatus(), "Right Indicator should be disabled");
        assertEquals(false, van.getSprite().getUrl().contains("van_grey_rightIndicatorOn.png"), "As right indicator is now off, expected that the sprite changes to reflect that the right indicator is off. go to default sprite that is grey");
    }

    @org.junit.Test
    public void test_randomColourCar() {
        Vehicle car = new Vehicle("car", 12.0);
        assertFalse(true, "unimplemented test, to do");
    }

}