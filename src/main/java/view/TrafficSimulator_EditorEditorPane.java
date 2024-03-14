package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import jfxtras.scene.control.ImageViewButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;

public class TrafficSimulator_EditorEditorPane extends GridPane {

    public TrafficSimulator_EditorEditorPane() {
//        this.setPrefSize(800, 400);
//        this.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        this.setStyle("-fx-background-color: aliceblue;");
        for(int col = 0; 24 > col; col++){
            for(int row = 0; 7 > row; row++){
                var cellButton = new ImageViewButton();
                cellButton.setFitHeight(45);
                cellButton.setFitWidth(40);
                cellButton.setPreserveRatio(true);
                cellButton.setImage(new Image(new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\hazards\\3_null.png").toURI().toString()));
                cellButton.setOnMouseClicked(event->{
                    cellButton.setImage(new Image(new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\1_straightRoad.png").toURI().toString()));
                });
                this.add(cellButton, col, row);
            }
        }
    }

    public void add_BuildHandler(EventHandler<ActionEvent> handler) {
        this.setOnMouseClicked(handler);
    }

    // moved to TrafficSimulatorController as Key Press Events are generated in the scene, not in the layout manager i.e. this pane.
//    public void rotate_PieceHandler(EventHandler<KeyEvent> handler) {
//        this.sceneProperty().getValue().setOnKeyPressed(handler);
//    }

}
