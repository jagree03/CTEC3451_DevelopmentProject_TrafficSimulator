package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.*;

public class TrafficSimulator_ProceduralController {

    private TrafficSimulator_Procedural view;

    private TrafficSimulatorRootMenuBar tse_menubar;

    private Scene scene;


    public TrafficSimulator_ProceduralController(TrafficSimulator_Procedural view) {
        //initialise view and model fields
        this.view = view;

        //initialise view subcontainer fields
        //tse_menubar = view.getTSE_MenuBar();

        //attach event handlers to view using private helper method
        this.attachEventHandlers();
    }

    private void attachEventHandlers() {
        /*
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

                new TrafficSimulatorController(view);
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

         */
    }
}
