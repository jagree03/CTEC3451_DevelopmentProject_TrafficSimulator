package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TrafficSimulator_EditorEditorPane extends Pane {

    public TrafficSimulator_EditorEditorPane() {
        this.setPrefSize(800, 400);
        this.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.setStyle("-fx-background-color: aliceblue;");
    }
}
