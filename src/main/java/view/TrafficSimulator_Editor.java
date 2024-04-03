package view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.swing.*;

public class TrafficSimulator_Editor extends VBox {

    private TrafficSimulator_EditorMenuBar menuBar;
    private TrafficSimulator_EditorEditorPane editorPane;
    private TrafficSimulator_EditorBottomPane tsebp;

    public TrafficSimulator_Editor() {
        menuBar = new TrafficSimulator_EditorMenuBar();
        editorPane = new TrafficSimulator_EditorEditorPane();
        tsebp = new TrafficSimulator_EditorBottomPane();
        tsebp.setPadding(new Insets(0,0,15,15));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(menuBar, editorPane, tsebp);
    }

    public TrafficSimulator_EditorMenuBar getTSE_MenuBar() {
        return menuBar;
    }

    public TrafficSimulator_EditorBottomPane getTSE_BottomPane() {
        return tsebp;
    }

    public TrafficSimulator_EditorEditorPane getTSE_EditorPane() { return editorPane; }

    public void addExitHandler(EventHandler<ActionEvent> handler) {
        menuBar.getExitItem().setOnAction(handler);
    }

}
