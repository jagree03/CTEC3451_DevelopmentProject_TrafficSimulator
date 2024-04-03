package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import view.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

public class TrafficSimulator_SimulatorSettingsController {

    private TrafficSimulator_SimulatorSettings view;

    private TrafficSimulatorRootMenuBar tss_menubar;

    private TrafficSimulator_EditorEditorPane tss_editorpane;
    private TrafficSimulator_SimulatorSettingsBottomPane tss_bottompane;

    private Scene scene;


    public TrafficSimulator_SimulatorSettingsController(TrafficSimulator_SimulatorSettings view) throws IOException {

        //initialise view and model fields
        this.view = view;

        //initialise view subcontainer fields
        tss_menubar = view.getTSS_MenuBar();
        tss_editorpane = view.getTSS_EditorPane();
        tss_bottompane = view.getTSS_BottomPane();

        // load scenario
        loadScenarioFromFile();

        //attach event handlers to view using private helper method
        this.attachEventHandlers();
    }

    private void loadScenarioFromFile() throws IOException {
            tss_editorpane.getChildren().removeAll();
            BufferedReader bufferedReader = null;
            try {
                String CurrentLine;
                bufferedReader = new BufferedReader(new FileReader("scenario.txt"));
                int lineCounter = 0;
                while ((CurrentLine = bufferedReader.readLine()) != null) {
                   lineCounter++;
                   if (lineCounter > 168) {
                       String[] array = CurrentLine.split(", ");
                       Double x = Double.parseDouble((array[0]));
                       Double y = Double.parseDouble((array[1]));
                       String imageURI = (array[2]);
                       ImageView n = new ImageView(imageURI);
                       n.setFitHeight(35);
                       n.setFitWidth(35);
                       n.setPreserveRatio(true);
                       n.setLayoutX(x);
                       n.setLayoutY(y);
                       n.setTranslateX(x);
                       n.setTranslateY(y);
                       tss_editorpane.getChildren().add(n);
                       continue;
                   }
                   String[] array = CurrentLine.split(", ");
                   int row = Integer.parseInt(array[0]);
                   int col = Integer.parseInt(array[1]);
                   double rotation = Double.parseDouble(array[2]);
                   String imageURI = (array[3]);
                   ImageViewButton cellButton = new ImageViewButton(new Image(imageURI));
                   cellButton.setRotate(rotation);
                   cellButton.setFitHeight(45);
                   cellButton.setFitWidth(40);
                   cellButton.setPreserveRatio(true);
                   tss_editorpane.add(cellButton, col, row);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
                bufferedReader.close();
            }
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

        tss_bottompane.addStartHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) tss_bottompane.getScene().getWindow(); // get the current window and cast to Stage
                scene = tss_bottompane.getScene(); // get the current scene
                scene.setCursor(null);
                TrafficSimulator_Simulation view = new TrafficSimulator_Simulation();
                try {
                    new TrafficSimulator_SimulationController(view);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                scene.setRoot(view); // change the root node of the current scene to the new screen
                stage.setTitle("Traffic Simulator - Simulation");
                stage.setScene(scene);
            }
        });

        tss_bottompane.addCarSpawnChanceSliderHandler(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tss_bottompane.setCarSpawnChanceInput(tss_bottompane.getCarSpawnChanceSliderValue());
            }
        });

        tss_bottompane.addVanSpawnChanceSliderHandler(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tss_bottompane.setVanSpawnChanceInput(tss_bottompane.getVanSpawnChanceSliderValue());
            }
        });
    }
}
