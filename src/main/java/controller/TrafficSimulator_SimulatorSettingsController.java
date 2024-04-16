package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import view.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;

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
                about.setTitle("Parameters");
                about.setHeaderText("Traffic Simulator");
                about.setContentText("Here you can tweak some parameters of the scenario"
                        + "\n"
                        + "Number of drivers - number of drivers or vehicles to spawn in the scenario"
                        + "\n"
                        + "Car spawn chance - the chance that drivers have standard coupe cars"
                        + "\n"
                        + "Van spawn chance - the chance that drivers have vans"
                        + "\n"
                        + "Number of buses - if bus transport/route system is enabled, you can adjust the number of buses to spawn.");
                about.show();
            }
        });

        tss_menubar.addExitHandler(e -> System.exit(0));

        tss_bottompane.addGoBackHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) tss_bottompane.getScene().getWindow(); // get the current window and cast to Stage
                scene = tss_bottompane.getScene(); // get the current scene
                scene.setCursor(null);
                TrafficSimulator_Editor view = new TrafficSimulator_Editor();
                TrafficSimulator_EditorController editorController = new TrafficSimulator_EditorController(view);
                scene.setOnKeyPressed(e -> { // KeyPressEvents generated in the scene, links to the TrafficSimulator_EditorController
                    // in terms of the piece rotation handling and deselecting pieces from the mouse cursor.
                    if (e.getCode() == KeyCode.R) {
                        try {
                            editorController.handleKeyPress();
                        } catch (UnsupportedAudioFileException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    else if (e.getCode() == KeyCode.F) {
                        editorController.handleKeyPressDeselect();
                    }
                });
                scene.setRoot(view); // change the root node of the current scene to the new screen
                stage.setTitle("Traffic Simulator - Build an environment");
            }
        });

        tss_bottompane.addStartHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    writeSimulatorSettingsDataToFile(); // write the parameter values to a file, so they can be read
                    // by the actual simulator scene.
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (tss_bottompane.getNumberOfDrivers() >= 1) { // if number of drivers is greater than or equal to 1 then...
                    if (tss_bottompane.getBusTransportEnabled()) { // check if bus transportation is enabled, then if it's true.. THEN..
                        if (tss_bottompane.getNumberOfBuses() >= 1) { // check if number of buses is greater than or equal to 1
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
                        } else { // if number of buses is not greater than or equal to 1, show error
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Parameter error");
                            alert.setContentText("Bus transportation is enabled, Number of buses must be 1 or more");
                            alert.show();
                        }
                    } else { // if bus transport is disabled and number of drivers is definitely greater than or equal to 1, proceed to go
                        // to the simulation screen
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
                } else { // if number of drivers is not greater than or equal to 1, then show error
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Parameter error");
                    alert.setContentText("Number of drivers must be 1 or more.");
                    alert.show();
                }
            }
        });

        tss_bottompane.addCarSpawnChanceSliderHandler(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tss_bottompane.setCarSpawnChanceInput(tss_bottompane.getCarSpawnChanceSliderValue());
                tss_bottompane.setVanSpawnChanceInput(100 - tss_bottompane.getCarSpawnChanceSliderValue());
            }
        });

        tss_bottompane.addVanSpawnChanceSliderHandler(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tss_bottompane.setVanSpawnChanceInput(tss_bottompane.getVanSpawnChanceSliderValue());
                tss_bottompane.setCarSpawnChanceInput(100 - tss_bottompane.getVanSpawnChanceSliderValue());
            }
        });

        tss_bottompane.addBusTransportCheckBoxHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Boolean value = tss_bottompane.getBusTransportEnabled();
                tss_bottompane.enableNumberOfBusesInput(value);
            }
        });
    }

    private void writeSimulatorSettingsDataToFile() throws IOException {
            FileWriter fileWriter = new FileWriter("parameters.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println(tss_bottompane.getNumberOfDrivers() + ", " +
                    tss_bottompane.getCarSpawnChanceSliderValue() + ", " +
                    tss_bottompane.getVanSpawnChanceSliderValue() + ", " +
                    tss_bottompane.getNumberOfBuses());

            printWriter.close();
            fileWriter.close();
    }
}
