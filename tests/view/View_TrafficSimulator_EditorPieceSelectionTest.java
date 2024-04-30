package view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class View_TrafficSimulator_EditorPieceSelectionTest {
    // tests focus only on functions
    private JFXPanel panel = new JFXPanel(); // create an empty JavaFX Panel instance for testing


    /**
     * Test #1
     * Description: Test if the EditorPieceSelection can return itself
     */
    @Test
    void test_getTS_EditorPiecesSelection() {
        TrafficSimulator_EditorPieceSelection t = new TrafficSimulator_EditorPieceSelection();
        assertTrue(t.getTS_EditorPiecesSelection() instanceof  TrafficSimulator_EditorPieceSelection, "Should return the PieceSelection Instance");
    }
    /**
     * Test #2
     * Description: Test if the clickable slots are set to road surface images
     */
    @Test
    void test_setToRoadSurfacePieces() {
        TrafficSimulator_EditorPieceSelection t = new TrafficSimulator_EditorPieceSelection();
        t.setToRoadSurfacePieces();
        ImageView[] slots = t.getSlots();

        File slot1_tempImg = new File("img\\2_EditorScreen\\roads\\1_straightRoad.png");
        File slot2_tempImg = new File("img\\2_EditorScreen\\roads\\2_turnLeft.png");
        File slot3_tempImg = new File("img\\2_EditorScreen\\roads\\4_intersection.png");
        File slot4_tempImg = new File("img\\2_EditorScreen\\grass.png");

        //images should match up with the file URI's above.
        assertEquals(slots[0].getImage().getUrl(), slot1_tempImg.toURI().toString());
        assertEquals(slots[1].getImage().getUrl(), slot2_tempImg.toURI().toString());
        assertEquals(slots[2].getImage().getUrl(), slot3_tempImg.toURI().toString());
        assertEquals(slots[3].getImage().getUrl(), slot4_tempImg.toURI().toString());
    }

    /**
     * Test #3
     * Description: Test if the clickable slots can be set to destination images
     */
    @Test
    void test_setToDestinationPieces() {
        TrafficSimulator_EditorPieceSelection t = new TrafficSimulator_EditorPieceSelection();
        t.setToDestinationPieces();
        ImageView[] slots = t.getSlots();

        File slot1_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        File slot2_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_2_shop.png");
        File slot3_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_3_office.png");
        File slot4_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_4_hospital.png");
        File slot5_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_5_hotel.png");

        //images should match up with the file URI's above.
        assertEquals(slots[0].getImage().getUrl(), slot1_tempImg.toURI().toString());
        assertEquals(slots[1].getImage().getUrl(), slot2_tempImg.toURI().toString());
        assertEquals(slots[2].getImage().getUrl(), slot3_tempImg.toURI().toString());
        assertEquals(slots[3].getImage().getUrl(), slot4_tempImg.toURI().toString());;
        assertEquals(slots[4].getImage().getUrl(), slot5_tempImg.toURI().toString());;
    }

    /**
     * Test #4
     * Description: Test if the clickable slots can be set to decorative images
     */
    @Test
    void test_setToDecorativePieces() {
        TrafficSimulator_EditorPieceSelection t = new TrafficSimulator_EditorPieceSelection();
        t.setToDecorativePieces();
        ImageView[] slots = t.getSlots();

        File slot1_tempImg = new File("img\\2_EditorScreen\\decorative\\1_tree.png");
        File slot2_tempImg = new File("img\\2_EditorScreen\\decorative\\2_bush.png");
        File slot3_tempImg = new File("img\\2_EditorScreen\\decorative\\3_bin.png");
        File slot4_tempImg = new File("img\\2_EditorScreen\\decorative\\4_plant.png");
        File slot5_tempImg = new File("img\\2_EditorScreen\\decorative\\5_plant2.png");
        File slot6_tempImg = new File("img\\2_EditorScreen\\decorative\\6_bench.png");

        //images should match up with the file URI's above.
        assertEquals(slots[0].getImage().getUrl(), slot1_tempImg.toURI().toString());
        assertEquals(slots[1].getImage().getUrl(), slot2_tempImg.toURI().toString());
        assertEquals(slots[2].getImage().getUrl(), slot3_tempImg.toURI().toString());
        assertEquals(slots[3].getImage().getUrl(), slot4_tempImg.toURI().toString());;
        assertEquals(slots[4].getImage().getUrl(), slot5_tempImg.toURI().toString());;
        assertEquals(slots[5].getImage().getUrl(), slot6_tempImg.toURI().toString());;
    }

    /**
     * Test #5
     * Description: Test if the clickable slots can be set to hazard images
     */
    @Test
    void test_setToHazardPieces() {
        TrafficSimulator_EditorPieceSelection t = new TrafficSimulator_EditorPieceSelection();
        t.setToHazardPieces();
        ImageView[] slots = t.getSlots();

        File slot1_tempImg = new File("img\\2_EditorScreen\\hazards\\1_cone.png");
        File slot2_tempImg = new File("img\\2_EditorScreen\\hazards\\2_barrier.png");
        File slot3_tempImg = new File("img\\2_EditorScreen\\hazards\\2_barrier_rock.png");
        File slot4_tempImg = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");

        //images should match up with the file URI's above.
        assertEquals(slots[0].getImage().getUrl(), slot1_tempImg.toURI().toString());
        assertEquals(slots[1].getImage().getUrl(), slot2_tempImg.toURI().toString());
        assertEquals(slots[2].getImage().getUrl(), slot3_tempImg.toURI().toString());
        assertEquals(slots[3].getImage().getUrl(), slot4_tempImg.toURI().toString());
    }

    /**
     * Test #6
     * Description: Test if the ImageView clickable slots can be obtained
     */
    @Test
    void test_getSlots() {
        TrafficSimulator_EditorPieceSelection t = new TrafficSimulator_EditorPieceSelection();

        assertTrue(t.getSlots() instanceof ImageView[], "Check if the slots variable returned storing an Array of ImageView nodes?");
        assertEquals(6, t.getSlots().length);
    }
}