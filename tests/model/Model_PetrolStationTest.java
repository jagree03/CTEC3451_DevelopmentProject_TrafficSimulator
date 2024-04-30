package model;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import jfxtras.scene.control.ImageViewButton;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class Model_PetrolStationTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing


    /**
     * Test #1
     * Description: Check the default constructor instance of a petrol station
     */
    @Test
    void test_DefaultConstructor() {
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);
        PetrolStation p = new PetrolStation(imgb);

        assertEquals("Shell", p.getName(), "Name should be Shell by default.");
        assertEquals(20.00, p.getTotalFuel(), "Total Fuel should be 20.00L by default");
        assertEquals(1.04, p.getPricePerLitre(), "Price per litre should be £1.04 by default.");
        assertEquals(0.00, p.getSales(), "Sales should be £0.00 by default.");
        assertFalse(p.getOutOfFuelValue(), "Out of fuel should be false by default.");
    }

    /**
     * Test #2
     * Description: Check the custom constructor instance of a petrol station
     */
    @Test
    void test_CustomConstructor() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        assertEquals(uniqueID.toString(), p.getUniqueID().toString(), "UUID must match");
        assertEquals("BP", p.getName(), "Name should be BP");
        assertTrue(p.getImageViewButton().getImage().getUrl().contains("dest_1_petrolStation"), "Expected the Image URI to be the petrol station sprite - dest_1_petrolStation.png");
        assertEquals(25.00, p.getTotalFuel(), "Total fuel should be 25.00");
        assertEquals(1.05, p.getPricePerLitre(), "Price per litre should be 1.05");
        assertEquals(0.05, p.getSales(), "Sales should be 0.05");
        assertFalse(p.getOutOfFuelValue(), "Out of fuel value should be false.");

    }

    /**
     * Test #3
     * Description: Get the unique ID (UUID) of the petrol station
     */
    @Test
    void test_getUniqueID() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        assertEquals(uniqueID, p.getUniqueID(), "UUID must match");
    }

    /**
     * Test #4
     * Description: Set and get the name of the petrol station instance
     */
    @Test
    void test_setAndGetName() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        assertEquals("BP", p.getName(), "Name should be BP");
        p.setName("Shell 2");
        assertEquals("Shell 2", p.getName(), "Name should be Shell 2");
    }

    /**
     * Test #5
     * Description: Set and get the total fuel of a petrol station
     */
    @Test
    void test_setAndGetTotalFuel() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        assertEquals(25.00, p.getTotalFuel(), "Total fuel should be 25.00");
        p.setTotalFuel(10.00);
        assertEquals(10.00, p.getTotalFuel(), "Total fuel should be 10.00");
    }

    /**
     * Test #6
     * Description: Reduce the total fuel value by a specific amount
     * N.B if fuel level goes below 0.00, then fuel level is set to 0 and outOfFuel becomes true
     */
    @Test
    void test_reduceTotalFuel() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        p.reduceTotalFuel(10);
        assertEquals(15.00, p.getTotalFuel(), "Total Fuel should decrease from 25.00 to 10.00");
        p.reduceTotalFuel(16.00); // 15 - 16 = -1
        assertEquals(0.00, p.getTotalFuel(), "Total Fuel will reach negative values, but the IF condition will force it to 0.00");
        assertTrue(p.getOutOfFuelValue(), "Out Of Fuel should now equal to true.");

    }

    /**
     * Test #7
     * Description: Set and get the price per litre value
     */
    @Test
    void test_setAndGetPricePerLitre() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        assertEquals(1.05, p.getPricePerLitre(), "Price per litre should be 1.05");
        p.setPricePerLitre(2.00);
        assertEquals(2.00, p.getPricePerLitre(), "Price per litre should be 2.00");
    }

    /**
     * Test #8
     * Description: Set and get the sales value
     */
    @Test
    void test_setAndGetSales() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        assertEquals(0.05, p.getSales(), "Sales should be 0.05");
        p.setSales(0.10);
        assertEquals(0.10, p.getSales(), "Sales should be 0.10");
    }

    /**
     * Test #9
     * Description: get the sales value in String format (£ currency)
     */
    @Test
    void test_getSalesInString() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        assertEquals("£ " +p.getSales(), p.getSalesInString(), "This method should return the sales with the £ attached");
    }

    /**
     * Test #10
     * Description: Add a value to the sales value.
     */
    @Test
    void test_addSales() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        p.addSales(0.50);
        assertEquals(0.55, p.getSales(), "Sales should be 0.55");
    }

    /**
     * Test #11
     * Description: Set and get the ImageViewButton associated to the petrol station instance
     */
    @Test
    void test_setAndGetImageViewButton() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        p.setImageViewButton(imgb);
        assertEquals(imgb, p.getImageViewButton(), "Image View Button should be the same.");
    }

    /**
     * Test #12
     * Description: Set the image of the Petrol Station
     */
    @Test
    void test_setImage() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        p.setImage(petrol);
        assertEquals(petrol.getUrl(), p.getImageViewButton().getImage().getUrl(), "Image returned should have the URL of abc");

    }

    /**
     * Test #13
     * Description: Set the out of fuel value for the petrol station
     */
    @Test
    void test_setAndGetOutOfFuelValue() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);
        assertFalse(p.getOutOfFuelValue(), "Out of fuel value should be false");
        p.setOutOfFuelValue(true);
        assertTrue(p.getOutOfFuelValue(), "Out of fuel value should be true");
    }

    /**
     * Test #14
     * Description: Check the accuracy of the toString method
     */
    @Test
    void test_testToString() {
        UUID uniqueID = UUID.randomUUID();
        File file = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        Image petrol = new Image(file.toURI().toString());
        ImageViewButton imgb = new ImageViewButton();
        imgb.setImage(petrol);

        PetrolStation p = new PetrolStation(uniqueID, "BP", imgb.getImage().getUrl(), 25.00, 1.05, 0.05, false);

        String toStr = p.toString();

        assertTrue( toStr.startsWith("PetrolStation:[") &&
                toStr.contains(p.getUniqueID().toString()) &&
                toStr.contains(p.getName()) &&
                toStr.contains(""+p.getTotalFuel()) &&
                toStr.contains(""+p.getPricePerLitre()) &&
                toStr.contains(""+p.getSales()) &&
                toStr.contains(""+p.getOutOfFuelValue()) &&
                toStr.contains(p.getImageViewButton().getImage().getUrl()) &&
                toStr.endsWith("]"), "The toString method should be in the standard convention format");
    }
}