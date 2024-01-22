package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javax.swing.*;

public class TrafficSimulator_EditorPiecesButtonsPane extends VBox {

    private Label title;
    private Button road_surface;
    private Button destinations;
    private Button decorative;
    private Button hazards;

    public TrafficSimulator_EditorPiecesButtonsPane() {
        title = new Label("Pieces");
        title.setAlignment(Pos.CENTER);
        road_surface = new Button("Road Surface");
        destinations = new Button("Destinations");
        decorative = new Button("Decorative");
        hazards = new Button("Hazards");

        road_surface.setPrefWidth(100);
        destinations.setPrefWidth(100);
        decorative.setPrefWidth(100);
        hazards.setPrefWidth(100);
        this.getChildren().addAll(title, road_surface, destinations, decorative, hazards);
    }

    public TrafficSimulator_EditorPiecesButtonsPane getTS_EditorPiecesButtonPane() {
        return this;
    }

    public void addRoadSurfaceButtonOnClickHandler(EventHandler<MouseEvent> handler) {
        road_surface.setOnMouseClicked(handler);
    }

    public void addDestinationsButtonOnClickHandler(EventHandler<ActionEvent> handler) {
        destinations.setOnAction(handler);
    }

    public void addDecorativeButtonOnClickHandler(EventHandler<ActionEvent> handler) {
        decorative.setOnAction(handler);
    }

    public void addHazardsButtonOnClickHandler(EventHandler<ActionEvent> handler) {
        hazards.setOnAction(handler);
    }
}
