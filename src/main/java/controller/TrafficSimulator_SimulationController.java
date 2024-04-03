package controller;

import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.scene.control.ImageViewButton;
import model.AStarGraph;
import model.Driver;
import model.GraphNode;
import view.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TrafficSimulator_SimulationController {

    private TrafficSimulator_Simulation view;

    private TrafficSimulatorRootMenuBar tss_menubar;

    private TrafficSimulator_EditorEditorPane tss_editorpane;

    private TrafficSimulator_SimulationBottomPane tss_bottompane;

    private ArrayList<PathTransition> PathTransitionArray;

    private AStarGraph graph;

    private Scene scene;


    public TrafficSimulator_SimulationController(TrafficSimulator_Simulation view) throws IOException {

        //initialise view and model fields
        this.view = view;

        //initialise view subcontainer fields
        tss_menubar = view.getTSS_MenuBar();
        tss_editorpane = view.getTSS_EditorPane();
        tss_bottompane = view.getTSS_BottomPane();


        //attach event handlers to view using private helper method
        this.attachEventHandlers();
        graph = new AStarGraph();
        // load scenario
        loadScenarioFromFile();
        readGraphNodesFromFile();
        spawnDriversAndGeneratePath();
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

    private void readGraphNodesFromFile() throws FileNotFoundException {
        BufferedReader bufferedReader = null;
        try {
            String CurrentLine;
            bufferedReader = new BufferedReader(new FileReader("graphnodes.txt"));
            while ((CurrentLine = bufferedReader.readLine()) != null) {
                String[] array = CurrentLine.split(", ");
                String id = (array[0]);
                Double x = Double.parseDouble((array[1]));
                Double y = Double.parseDouble((array[2]));
                GraphNode n = new GraphNode(id, x, y);
                graph.addGraphNodeToList(n);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void spawnDriversAndGeneratePath() {

        // create temp driver
        Driver one = new Driver();
        Driver two = new Driver();
        ImageView vehicle = one.getVehicle().getSpriteImageView();
        ImageView vehicle2 = two.getVehicle().getSpriteImageView();
        tss_editorpane.getChildren().addAll(vehicle);
//        vehicle.setLayoutX(0);
//        vehicle.setLayoutY(0);

        PathTransitionArray = new ArrayList<PathTransition>();

        // driver one
        Path path = new Path();

//        for (GraphNode node : graph.getRouteList()) {
//            if (node.getId().contains("left_")) {
//                MoveTo m = new MoveTo(node.getXCoordinate(), node.getYCoordinate());
//                m.setAbsolute(true);
//                path.getElements().add(m);
//            }
//        }

        path.getElements().add(new MoveTo(200,300));
        path.getElements().add(new MoveTo(250, 300));
        //path.getElements().add(new VLineTo(0));

        TranslateTransition tt = new TranslateTransition(Duration.seconds(5), vehicle);
        tt.setFromX(232);
        tt.setFromY(149);
        tt.setToX(20f);
        tt.setToY(20f);
        tt.setCycleCount(TranslateTransition.INDEFINITE);
        tt.setAutoReverse(true);
        tt.play();
//        PathTransition transition = new PathTransition();
//        PathTransitionArray.add(transition);
//        transition.setNode(vehicle);
//        transition.setDuration(Duration.seconds(10));
//        transition.setPath(path);
//        transition.setCycleCount(PathTransition.INDEFINITE);
//        transition.play();

        // driver two
//        Path path2 = new Path();
//        path2.getElements().add(new MoveTo(25,0));
//        path2.getElements().add(new VLineTo(300));
//        PathTransition transition2 = new PathTransition();
//        PathTransitionArray.add(transition2);
//        transition2.setNode(vehicle2);
//        transition2.setDuration(Duration.seconds(10));
//        transition2.setPath(path2);
//        transition2.setCycleCount(PathTransition.INDEFINITE);
//        transition2.pause();
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
                TrafficSimulator_SimulatorSettings view = new TrafficSimulator_SimulatorSettings();

                try {
                    new TrafficSimulator_SimulatorSettingsController(view);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                scene.setRoot(view); // change the root node of the current scene to the new screen
                stage.setTitle("Traffic Simulator - Simulation Options");
            }
        });

        tss_bottompane.addPauseSimulationHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Button pause = tss_bottompane.getStartAndPauseButton();
                if (pause.getText().equals("PLAY SIMULATION")) {
                    for (PathTransition pt: PathTransitionArray) {
                        pt.play();
                        pause.setText("PAUSE SIMULATION");
                    }
                } else {
                    for (PathTransition pt: PathTransitionArray) {
                        pt.pause();
                    }
                    pause.setText("PLAY SIMULATION");
                }
            }
        });

        tss_bottompane.addStatisticsHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TrafficSimulator_Statistics stats = new TrafficSimulator_Statistics();
                Scene scene = new Scene(stats, 250, 400);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        });



    } // end of attachEventHandlers
} // end of class file
