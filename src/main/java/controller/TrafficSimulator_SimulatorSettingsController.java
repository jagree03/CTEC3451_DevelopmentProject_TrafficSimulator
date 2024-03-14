package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.*;

import java.io.File;

public class TrafficSimulator_SimulatorSettingsController {

    private TrafficSimulator_SimulatorSettings view;

    private TrafficSimulatorRootMenuBar tss_menubar;
    private TrafficSimulator_SimulatorSettingsBottomPane tss_bottompane;

    private Scene scene;


    public TrafficSimulator_SimulatorSettingsController(TrafficSimulator_SimulatorSettings view) {
        //initialise view and model fields
        this.view = view;

        //initialise view subcontainer fields
        tss_menubar = view.getTSS_MenuBar();
        tss_bottompane = view.getTSS_BottomPane();

        //attach event handlers to view using private helper method
        this.attachEventHandlers();
    }

    private void attachEventHandlers() {
        //attach an event handler to the menu item of the menu bar that shows about author info about the program
        tss_menubar.addAboutHandler(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Alert about = new Alert(Alert.AlertType.INFORMATION);
                about.setTitle("Controls");
                about.setHeaderText("Controls for Traffic Simulator");
                about.setContentText("Left click on pieces to select them from the bottom panel, what you select is reflected on your mouse cursor. " +
                        "To place your piece, left click anywhere within the light blue panel."
                        + "\n\n"
                        + "F Key - Deselect piece"
                        + "\n\n"
                        + "R key - Rotate Piece by 90 degrees");
                about.show();
            }
        });

        tss_bottompane.addGoBackHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) tss_bottompane.getScene().getWindow(); // get the current window and cast to Stage
                scene = tss_bottompane.getScene(); // get the current scene
                scene.setCursor(null);
                TrafficSimulator_Editor view = new TrafficSimulator_Editor();

                new TrafficSimulator_EditorController(view);
                scene.setRoot(view); // change the root node of the current scene to the new screen
                stage.setTitle("Traffic Simulator - Build an environment");
            }
        });
//
//        tss_bottompane.addFinishHandler(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                Stage stage = (Stage) tse_bottompane.getScene().getWindow(); // get the current window and cast to Stage
//                scene = tse_bottompane.getScene(); // get the current scene
//                scene.setCursor(null);
//                TrafficSimulator_SimulatorSettings view = new TrafficSimulator_SimulatorSettings();
//                new TrafficSimulator_SimulatorSettingsController(view);
//                scene.setRoot(view); // change the root node of the current scene to the new screen
//                stage.setTitle("Traffic Simulator - Simulation Options");
//                stage.setScene(scene);
//            }
//        });
    }
}
