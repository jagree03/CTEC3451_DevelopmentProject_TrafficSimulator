package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

    private Image slot6_img;
    private ImageView slot6;

    private Image slot7_img;
    private ImageView slot7;

    public TrafficSimulator_EditorPieceSelection() {
        File slot1_tempImg = new File("img\\2_EditorScreen\\roads\\1_straightRoad.png");
        File slot2_tempImg = new File("img\\2_EditorScreen\\roads\\2_turnLeft.png");
        File slot3_tempImg = new File("img\\2_EditorScreen\\roads\\4_intersection.png");
        File slot4_tempImg = new File("img\\2_EditorScreen\\grass.png");
//        File slot5_tempImg = new File("");

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());
//        slot5_img = new Image(slot5_tempImg.toURI().toString());

        slot1 = new ImageView(slot1_img);
        slot1.setFitWidth(70);
        slot1.setFitHeight(70);
        slot1.setPreserveRatio(true);

        slot2 = new ImageView(slot2_img);
        slot2.setFitWidth(70);
        slot2.setFitHeight(70);
        slot2.setPreserveRatio(true);

        slot3 = new ImageView(slot3_img);
        slot3.setFitWidth(70);
        slot3.setFitHeight(70);
        slot3.setPreserveRatio(true);

        slot4 = new ImageView(slot4_img);
        slot4.setFitWidth(70);
        slot4.setFitHeight(70);
        slot4.setPreserveRatio(true);

        slot5 = new ImageView(slot5_img);
        slot5.setFitWidth(70);
        slot5.setFitHeight(70);
        slot5.setPreserveRatio(true);

        slot6 = new ImageView(slot6_img);
        slot6.setFitWidth(70);
        slot6.setFitHeight(70);
        slot6.setPreserveRatio(true);

        slot7 = new ImageView(slot7_img);
        slot7.setFitWidth(70);
        slot7.setFitHeight(70);
        slot7.setPreserveRatio(true);

        slot6.setVisible(false);
        slot6.setDisable(true);
        slot7.setVisible(false);
        slot7.setDisable(true);

        this.setSpacing(5);
        this.setPadding(new Insets(0,0,0,100));
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(slot1, slot2, slot3, slot4, slot5, slot6, slot7);
    }

    public TrafficSimulator_EditorPieceSelection getTS_EditorPiecesSelection() {
        return this;
    }

    public void setToRoadSurfacePieces() {
        File slot1_tempImg = new File("img\\2_EditorScreen\\roads\\1_straightRoad.png");
        File slot2_tempImg = new File("img\\2_EditorScreen\\roads\\2_turnLeft.png");
        File slot3_tempImg = new File("img\\2_EditorScreen\\roads\\4_intersection.png");
        File slot4_tempImg = new File("img\\2_EditorScreen\\grass.png");
        //File slot5_tempImg = new File("");
        //File slot6_tempImg = new File("img\\2_EditorScreen\\roads\\null.png");

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());
        //slot5_img = new Image(slot5_tempImg.toURI().toString());
        //slot6_img = new Image(slot6_tempImg.toURI().toString());

        slot1.setImage(slot1_img);
        slot2.setImage(slot2_img);
        slot3.setImage(slot3_img);
        slot4.setImage(slot4_img);
        //slot5.setImage(slot5_img);
        //slot6.setImage(slot6_img);

        slot5.setVisible(false);
        slot5.setDisable(true);

        slot6.setVisible(false);
        slot6.setDisable(true);

        slot7.setVisible(false);
        slot7.setDisable(true);
    }

    public void setToDestinationPieces() {
        File slot1_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_1_petrolStation.png");
        File slot2_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_2_shop.png");
        File slot3_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_3_office.png");
        File slot4_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_4_hospital.png");
        File slot5_tempImg = new File("img\\2_EditorScreen\\destinations\\dest_5_hotel.png");
        File slot6_tempImg = new File("img\\2_EditorScreen\\destinations\\bus_station.png");
        File slot7_tempImg = new File("img\\2_EditorScreen\\destinations\\bus_stop.png");

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());
        slot5_img = new Image(slot5_tempImg.toURI().toString());
        slot6_img = new Image(slot6_tempImg.toURI().toString());
        slot7_img = new Image(slot7_tempImg.toURI().toString());

        slot1.setImage(slot1_img);
        slot2.setImage(slot2_img);
        slot3.setImage(slot3_img);
        slot4.setImage(slot4_img);
        slot5.setImage(slot5_img);
        slot6.setImage(slot6_img);
        slot6.setVisible(true);
        slot6.setDisable(false);
        slot7.setImage(slot7_img);
        slot7.setVisible(true);
        slot7.setDisable(false);
    }

    public void setToDecorativePieces() {
        File slot1_tempImg = new File("img\\2_EditorScreen\\decorative\\1_tree.png");
        File slot2_tempImg = new File("img\\2_EditorScreen\\decorative\\2_bush.png");
        File slot3_tempImg = new File("img\\2_EditorScreen\\decorative\\3_bin.png");
        File slot4_tempImg = new File("img\\2_EditorScreen\\decorative\\4_plant.png");
        File slot5_tempImg = new File("img\\2_EditorScreen\\decorative\\5_plant2.png");
        File slot6_tempImg = new File("img\\2_EditorScreen\\decorative\\6_bench.png");

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());
        slot5_img = new Image(slot5_tempImg.toURI().toString());
        slot6_img = new Image(slot6_tempImg.toURI().toString());

        slot1.setImage(slot1_img);
        slot2.setImage(slot2_img);
        slot3.setImage(slot3_img);
        slot4.setImage(slot4_img);
        slot5.setImage(slot5_img);
        slot6.setImage(slot6_img);
        slot7.setVisible(false);
        slot7.setDisable(true);
    }

    public void setToHazardPieces() {
        File slot1_tempImg = new File("img\\2_EditorScreen\\hazards\\1_cone.png");
        File slot2_tempImg = new File("img\\2_EditorScreen\\hazards\\2_barrier.png");
        File slot3_tempImg = new File("img\\2_EditorScreen\\hazards\\2_barrier_90.png");
        File slot4_tempImg = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");

        /*
        File slot4_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\hazards\\4_plant.png");
        File slot5_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\hazards\\5_plant2.png");
         */

        slot1_img = new Image(slot1_tempImg.toURI().toString());
        slot2_img = new Image(slot2_tempImg.toURI().toString());
        slot3_img = new Image(slot3_tempImg.toURI().toString());
        slot4_img = new Image(slot4_tempImg.toURI().toString());

        slot1.setImage(slot1_img);
        slot2.setImage(slot2_img);
        slot3.setImage(slot3_img);
        slot4.setImage(slot4_img);
//        slot5.setImage(slot3_img);
//        slot6.setImage(slot3_img))
        slot4.setVisible(true);
        slot4.setDisable(false);

        slot5.setVisible(false);
        slot5.setDisable(true);
        slot6.setVisible(false);
        slot6.setDisable(true);
        slot7.setVisible(false);
        slot7.setDisable(true);
    }

    public ImageView[] getSlots() {
        ImageView[] array = new ImageView[7];
        array[0] = slot1;
        array[1] = slot2;
        array[2] = slot3;
        array[3] = slot4;
        array[4] = slot5;
        array[5] = slot6;
        array[6] = slot7;
        return array;
    }

    public void CursorOnClickHandler(EventHandler<MouseEvent> handler) {
        slot1.setOnMouseClicked(handler);
        slot2.setOnMouseClicked(handler);
        slot3.setOnMouseClicked(handler);
        slot4.setOnMouseClicked(handler);
        slot5.setOnMouseClicked(handler);
        slot6.setOnMouseClicked(handler);
        slot7.setOnMouseClicked(handler);
    }
}
