package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


import java.io.File;

public class TrafficSimulator_EditorPieceSelection extends HBox {
    private Image slot1_img;
    private ImageView slot1;

    private Image slot2_img;
    private ImageView slot2;

    private Image slot3_img;
    private ImageView slot3;

    private Image slot4_img;
    private ImageView slot4;

    private Image slot5_img;
    private ImageView slot5;

    public TrafficSimulator_EditorPieceSelection() {
        File slot1_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\1_straightRoad.png");
        File slot2_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\2_turnLeft.png");
        File slot3_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\3_turnRight.png");
        File slot4_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\4_straightRoadTrafficLeft.png");
        File slot5_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\5_straightRoadTrafficRight.png");

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());
        slot5_img = new Image(slot5_tempImg.toURI().toString());

        slot1 = new ImageView(slot1_img);
        slot1.setFitWidth(90);
        slot1.setFitHeight(90);
        slot1.setPreserveRatio(true);

        slot2 = new ImageView(slot2_img);
        slot2.setFitWidth(90);
        slot2.setFitHeight(90);
        slot2.setPreserveRatio(true);

        slot3 = new ImageView(slot3_img);
        slot3.setFitWidth(90);
        slot3.setFitHeight(90);
        slot3.setPreserveRatio(true);

        slot4 = new ImageView(slot4_img);
        slot4.setFitWidth(90);
        slot4.setFitHeight(90);
        slot4.setPreserveRatio(true);

        slot5 = new ImageView(slot5_img);
        slot5.setFitWidth(90);
        slot5.setFitHeight(90);
        slot5.setPreserveRatio(true);

        this.setSpacing(5);
        this.setPadding(new Insets(0,0,0,100));
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(slot1, slot2, slot3, slot4, slot5);
    }

    public TrafficSimulator_EditorPieceSelection getTS_EditorPiecesSelection() {
        return this;
    }

    public void setToRoadSurfacePieces() {
        File slot1_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\1_straightRoad.png");
        File slot2_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\2_turnLeft.png");
        File slot3_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\3_turnRight.png");
        File slot4_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\4_straightRoadTrafficLeft.png");
        File slot5_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\5_straightRoadTrafficRight.png");

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());
        slot5_img = new Image(slot5_tempImg.toURI().toString());

        slot1.setImage(slot1_img);
        slot2.setImage(slot2_img);
        slot3.setImage(slot3_img);
        slot4.setImage(slot4_img);
        slot5.setImage(slot5_img);
    }

    public void setToDestinationPieces() {
        File slot1_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\destinations\\1_petrolStation.png");
        File slot2_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\destinations\\2_shop.png");
        File slot3_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\destinations\\3_office.png");
        File slot4_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\destinations\\4_hospital.png");
        File slot5_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\destinations\\5_hotel.png");

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());
        slot5_img = new Image(slot5_tempImg.toURI().toString());

        slot1.setImage(slot1_img);
        slot2.setImage(slot2_img);
        slot3.setImage(slot3_img);
        slot4.setImage(slot4_img);
        slot5.setImage(slot5_img);
    }

    public void setToDecorativePieces() {
        File slot1_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\decorative\\1_tree.png");
        File slot2_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\decorative\\2_bush.png");
        File slot3_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\decorative\\3_bin.png");
        File slot4_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\decorative\\4_plant.png");
        File slot5_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\decorative\\5_plant2.png");

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());
        slot5_img = new Image(slot5_tempImg.toURI().toString());

        slot1.setImage(slot1_img);
        slot2.setImage(slot2_img);
        slot3.setImage(slot3_img);
        slot4.setImage(slot4_img);
        slot5.setImage(slot5_img);
    }

    public void setToHazardPieces() {
        File slot1_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\hazards\\1_cone.png");
        File slot2_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\hazards\\2_barrier.png");
        File slotNull_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\hazards\\3_null.png");
        /*
        File slot3_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\hazards\\3_bin.png");
        File slot4_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\hazards\\4_plant.png");
        File slot5_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\hazards\\5_plant2.png");
         */

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slotNull_tempImg.toURI().toString()); //placeholder for null - only 2 hazard objects so far: cone and barrier
        /*
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());
        slot5_img = new Image(slot5_tempImg.toURI().toString());
         */

        slot1.setImage(slot1_img);
        slot2.setImage(slot2_img);
        slot3.setImage(slot3_img);
        slot4.setImage(slot3_img);
        slot5.setImage(slot3_img);

    }
}
