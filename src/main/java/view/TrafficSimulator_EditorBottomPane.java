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

    /**
     * Default constructor
     */
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

    /**
     * Set the current rotation value and update the UI
     * @param degrees Integer representing rotation of degrees.
     */
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

    /**
     * Set the icon in the bottom pane to reflect the rotated image via URI
     * @param URL Image URL
     */
    public void setRotationImage(String URL) {
        Image img = new Image(URL);
        rotationImage.setImage(img);
        rotationImage.setFitWidth(45);
        rotationImage.setFitHeight(45);
        rotationImage.setPreserveRatio(true);
    }

    /**
     * Set the icon in the bottom pane to reflect the rotated image via Image Instance
     * @param img Image Instance
     */
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

    /**
     * Add a handler to the back button
     * @param handler EventHandler<ActionEvent>
     */
    public void addGoBackHandler(EventHandler<ActionEvent> handler) {
        Back.setOnAction(handler);
    }

    /**
     * Add a handler to the finish button
     * @param handler EventHandler<ActionEvent>
     */
    public void addFinishHandler(EventHandler<ActionEvent> handler) {
        Finish.setOnAction(handler);
    }
}
