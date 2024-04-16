package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TrafficSimulator_EditorBottomPane extends HBox {
    private TrafficSimulator_EditorPiecesButtonsPane tsepbp;
    private TrafficSimulator_EditorPieceSelection tseps;

    private Label CurrentRotationValue;

    private ImageView rotationImage;

    private Button Back;

    private Button Finish;

    public TrafficSimulator_EditorBottomPane() {
        tsepbp = new TrafficSimulator_EditorPiecesButtonsPane();
        tseps = new TrafficSimulator_EditorPieceSelection();

        VBox buttons = new VBox(5);

        CurrentRotationValue = new Label("Current Rotation Degrees: ");
        CurrentRotationValue.setTextFill(Color.web("#ff2003"));
        CurrentRotationValue.setPadding(new Insets(0,0,2,0));

        rotationImage = new ImageView(new WritableImage(45, 45));

        Back = new Button("Back");
        Finish = new Button("Finish");
        buttons.getChildren().addAll(CurrentRotationValue, rotationImage, Back, Finish);

        this.setSpacing(10);
        this.getChildren().addAll(tsepbp, tseps, buttons);
    }

    public TrafficSimulator_EditorPiecesButtonsPane getPiecesButtonsPane() {
        return tsepbp.getTS_EditorPiecesButtonPane();
    }

    public TrafficSimulator_EditorPieceSelection getPiecesSelection() {
        return tseps.getTS_EditorPiecesSelection();
    }

    public void setCurrentRotationValue(int degrees) {
        if (degrees == 0) {
            CurrentRotationValue.setText("Current Rotation Degrees: 0");
        } else if (degrees == 90) {
            CurrentRotationValue.setText("Current Rotation Degrees: 90");
        } else if (degrees == 180) {
            CurrentRotationValue.setText("Current Rotation Degrees: 180");
        } else if (degrees == 270) {
            CurrentRotationValue.setText("Current Rotation Degrees: 270");
        }
    }

    public Image getRotationImage() {
        return rotationImage.getImage();
    }
    public void setRotationImage(String URL) {
        String labelledURL = URL.replace(".png", "_labelled.png");
        Image img = new Image(labelledURL);
        rotationImage.setImage(img);
        rotationImage.setFitWidth(45);
        rotationImage.setFitHeight(45);
        rotationImage.setPreserveRatio(true);
    }

    public void setRotationImage(Image img) {
        rotationImage.setImage(img);
        rotationImage.setFitWidth(45);
        rotationImage.setFitHeight(45);
        rotationImage.setPreserveRatio(true);
    }

    //unused
    public void setCurrentRotationText(String value) {
        CurrentRotationValue.setText(value);
    }
    public void addGoBackHandler(EventHandler<ActionEvent> handler) {
        Back.setOnAction(handler);
    }

    public void addFinishHandler(EventHandler<ActionEvent> handler) {
        Finish.setOnAction(handler);
    }
}
