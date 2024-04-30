package model;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class Model_Driver_StatisticsTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing

    /**
     * Test #1
     * Description: Checking to see if the default constructor works
     */
    @Test
    void test_DefaultConstructor() {
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Yellow");

        UUID uniqueID = UUID.randomUUID();

        Driver_Statistics d = new Driver_Statistics(uniqueID, 90, false, "left", "left_1", "left_10", "left_1", vehicle);
        assertEquals(uniqueID.toString(), d.getUniqueId().toString(), "UUID should match");
        assertEquals(90, d.getRotationVal(), "Rotation val should be 90");
        assertFalse(d.isHazLightsOn(), "Hazard Lights of the driver's vehicle should be off.");
        assertEquals("left", d.getLane(), "Drivers lane should be left");
        assertEquals("left_1", d.getStartNodeID(), "Start Node ID should be left_1");
        assertEquals("left_10", d.getGoalNodeID(), "Goal Node ID should be left_10");
        assertEquals("left_1", d.getCurrentNodeID(), "Current Node ID should be left_1");
        assertEquals(vehicle, d.getVehicle(), "Vehicle should be the same vehicle that was initialised above.");
    }

    /**
     * Test #2
     * Description: Getting the Unique Id of a Driver_Statistics object
     */
    @Test
    void test_gettingTheUniqueId() {
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Yellow");

        UUID uniqueID = UUID.randomUUID();

        Driver_Statistics d = new Driver_Statistics(uniqueID, 90, false, "left", "left_1", "left_10", "left_1", vehicle);

        assertEquals(uniqueID.toString(), d.getUniqueId().toString(), "UUID Unique ID returned should match the UniqueID that was passed in");
    }

    /**
     * Test #3
     * Description: checking if the driver's vehicle has hazard lights on or not
     */
    @Test
    void test_isTheDriverVehicleHazardLightsOn() {
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Yellow");

        UUID uniqueID = UUID.randomUUID();

        Driver_Statistics d = new Driver_Statistics(uniqueID, 90, false, "left", "left_1", "left_10", "left_1", vehicle);

        assertFalse(d.isHazLightsOn(), "Hazard lights should be off");
    }

    /**
     * Test #4
     * Description: checking the driver's lane.
     */
    @Test
    void test_gettingTheDriverLane() {
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Yellow");

        UUID uniqueID = UUID.randomUUID();

        Driver_Statistics d = new Driver_Statistics(uniqueID, 90, false, "left", "left_1", "left_10", "left_1", vehicle);

        assertEquals("left", d.getLane(), "Driver's lane should be left");
    }

    /**
     * Test #4
     * Description: getting the start node ID of the driver_statistics object.
     */
    @Test
    void test_gettingTheStartNodeID() {
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Yellow");

        UUID uniqueID = UUID.randomUUID();

        Driver_Statistics d = new Driver_Statistics(uniqueID, 90, false, "left", "left_1", "left_10", "left_1", vehicle);

        assertEquals("left_1", d.getStartNodeID(), "Start Node ID should be left_1");
    }

    /**
     * Test #5
     * Description: getting the goal node ID of the driver_statistics object.
     */
    @Test
    void test_getttingTheGoalNodeID() {
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Yellow");

        UUID uniqueID = UUID.randomUUID();

        Driver_Statistics d = new Driver_Statistics(uniqueID, 90, false, "left", "left_1", "left_10", "left_1", vehicle);

        assertEquals("left_10", d.getGoalNodeID(), "Goal Node ID should be left_10");
    }

    /**
     * Test #6
     * Description: getting the current node ID of the driver_statistics object.
     */
    @Test
    void test_gettingTheCurrentNodeID() {
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Yellow");

        UUID uniqueID = UUID.randomUUID();

        Driver_Statistics d = new Driver_Statistics(uniqueID, 90, false, "left", "left_1", "left_10", "left_1", vehicle);

        assertEquals("left_1", d.getCurrentNodeID(), "Current Node id should be equal to left_1");
    }

    /**
     * Test #7
     * Description: getting the vehicle object of the driver_statistics object.
     */
    @Test
    void test_gettingTheDriverVehicle() {
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Yellow");

        UUID uniqueID = UUID.randomUUID();

        Driver_Statistics d = new Driver_Statistics(uniqueID, 90, false, "left", "left_1", "left_10", "left_1", vehicle);
        assertEquals(vehicle, d.getVehicle(), "The returned vehicle should be the same vehicle that was initialised above in this test class");
    }

    /**
     * Test #8
     * Description: Checking the accuracy of the toString method for Driver_Statistics
     */
    @Test
    void test_ToString() {
        Vehicle vehicle = new Vehicle("car", 5.00);
        vehicle.setColor("Yellow");

        UUID uniqueID = UUID.randomUUID();

        Driver_Statistics driver = new Driver_Statistics(uniqueID, 90, false, "left", "left_1", "left_10", "left_1", vehicle);

        String toStr = driver.toString();

        assertTrue( toStr.startsWith("Driver_Statistics:[") &&
                toStr.contains(driver.getVehicle().toString()) &&
                toStr.contains(driver.getUniqueId().toString()) &&
                toStr.contains(""+driver.getRotationVal()) &&
                toStr.contains(""+driver.isHazLightsOn()) &&
                toStr.contains(driver.getLane()) &&
                toStr.contains(driver.getStartNodeID()) &&
                toStr.contains(driver.getGoalNodeID()) &&
                toStr.contains(driver.getCurrentNodeID()) &&
                toStr.endsWith("]"), "The toString method should be in the standard convention format");
    }
}