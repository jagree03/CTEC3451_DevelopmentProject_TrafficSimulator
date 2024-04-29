package model;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Model_VehicleTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing


    /**
     * Test #1
     * Description:
     */
    @Test
    public void test_GettingTheVehicle() {
        Vehicle one = new Vehicle();
        assertEquals(true, one.getSprite().getUrl().contains("car_red.png"), "A default vehicle should have a red car sprite");
        assertEquals("Red", one.getColor(), "A default vehicle should be of red colour");
        assertEquals(5.0, one.getFuelLevel(), "A default vehicle should have 5 litres of fuel.");
    }

    @Test
    public void test_SettingTheVehicle() {
        Vehicle two = new Vehicle();
        two.setColor("Blue");
        assertEquals("Blue", two.getColor(), "Vehicle colour should be blue");
        two.setFuelLevel(10.0);
        assertEquals(10.0, two.getFuelLevel(), "Fuel level should be set to 10.0 Litres");
    }

    @Test
    public void test_CreatingAVan() {
        Vehicle five = new Vehicle("van", "grey", 12.0);
        assertEquals("van", five.getType(), "Type is a van");
        assertEquals(12.0, five.getFuelLevel(), "Fuel level is 12.0");
        assertEquals(true, five.getSprite().getUrl().contains("van_grey.png"), "the sprite is a grey van");
    }


    @Test
    public void test_randomColourCar() {
        Vehicle car = new Vehicle("car", 12.0);
        String colour = car.getRandomColour();

        while (!colour.equals("Orange")) {
            colour = car.getRandomColour();
        }

        assertEquals("Orange",  colour);
    }

}