package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.StudentProfile;
import view.*;

public class TrafficSimulator_EditorController {

    private StudentProfile model;
    private TrafficSimulator_Editor view;

    private TrafficSimulatorRootMenuBar tse_menubar;
    private TrafficSimulator_EditorBottomPane tse_bottompane;

    private Scene scene;


    public TrafficSimulator_EditorController(StudentProfile model, TrafficSimulator_Editor view) {
        //initialise view and model fields
        this.view = view;
        this.model = model;

        //initialise view subcontainer fields
        tse_menubar = view.getTSE_MenuBar();
        tse_bottompane = view.getTSE_BottomPane();

        //attach event handlers to view using private helper method
        this.attachEventHandlers();
    }

    private void attachEventHandlers() {
        tse_bottompane.getPiecesButtonsPane().addRoadSurfaceButtonOnClickHandler(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tse_bottompane.getPiecesSelection().setToRoadSurfacePieces();
            }
        });

        tse_bottompane.getPiecesButtonsPane().addDestinationsButtonOnClickHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tse_bottompane.getPiecesSelection().setToDestinationPieces();
            }
        });

        tse_bottompane.getPiecesButtonsPane().addDecorativeButtonOnClickHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tse_bottompane.getPiecesSelection().setToDecorativePieces();
            }
        });

        tse_bottompane.getPiecesButtonsPane().addHazardsButtonOnClickHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tse_bottompane.getPiecesSelection().setToHazardPieces();
            }
        });

        tse_bottompane.addGoBackHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) tse_bottompane.getScene().getWindow(); // get the current window and cast to Stage
                scene = tse_bottompane.getScene(); // get the current scene
                TrafficSimulatorRootPane view = new TrafficSimulatorRootPane();
                StudentProfile model = new StudentProfile();
                new TrafficSimulatorController(model, view);
                scene.setRoot(view); // change the root node of the current scene to the new screen
                stage.setTitle("Traffic Simulator - Environment Options");
            }
        });

        tse_bottompane.addFinishHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) tse_bottompane.getScene().getWindow(); // get the window from one of the Layout managers,
                TrafficSimulator_SimulatorSettings settingsView = new TrafficSimulator_SimulatorSettings();
                Scene scene = new Scene(settingsView);
                stage.setMinWidth(900);
                stage.setMinHeight(550);
                stage.setTitle("Traffic Simulator - Simulation Options");
                stage.setScene(scene);
            }
        });
    }
}
