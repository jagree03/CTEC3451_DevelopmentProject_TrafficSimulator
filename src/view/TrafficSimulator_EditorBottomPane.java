package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class TrafficSimulator_EditorBottomPane extends HBox {
    private TrafficSimulator_EditorPiecesButtonsPane tsepbp;
    private TrafficSimulator_EditorPieceSelection tseps;

    private Button Back;

    private Button Finish;

    public TrafficSimulator_EditorBottomPane() {
        tsepbp = new TrafficSimulator_EditorPiecesButtonsPane();
        tseps = new TrafficSimulator_EditorPieceSelection();

        Back = new Button("Back");
        Finish = new Button("Finish");

        this.setSpacing(5);
        this.getChildren().addAll(tsepbp, tseps, Back, Finish);
    }

    public TrafficSimulator_EditorPiecesButtonsPane getPiecesButtonsPane() {
        return tsepbp.getTS_EditorPiecesButtonPane();
    }

    public TrafficSimulator_EditorPieceSelection getPiecesSelection() {
        return tseps.getTS_EditorPiecesSelection();
    }

    public void addGoBackHandler(EventHandler<ActionEvent> handler) {
        Back.setOnAction(handler);
    }

    public void addFinishHandler(EventHandler<ActionEvent> handler) {
        Finish.setOnAction(handler);
    }
}
