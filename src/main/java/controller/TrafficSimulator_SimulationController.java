package controller;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.scene.control.ImageViewButton;
import model.*;
import view.*;

import java.io.*;
import java.util.ArrayList;

public class TrafficSimulator_SimulationController {

    private TrafficSimulator_Simulation view;

    private TrafficSimulator_SimulationMenuBar tss_menubar;

    private TrafficSimulator_EditorEditorPane tss_editorpane;

    private TrafficSimulator_SimulationBottomPane tss_bottompane;

    private ArrayList<PathTransition> PathTransitionArray;

    private AStarGraph graph;

    private int numOfDrivers;

    private int carSpawnChance;

    private int vanSpawnChance;

    private int numOfBuses;

    private ArrayList<Driver> drivers;
    private ArrayList<ImageView> trafficLights;
    private ArrayList<ImageView> hazards;
    private ArrayList<ImageView> petrolStations;


    private AnimationTimer at;

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
        drivers = new ArrayList<>();
        hazards = new ArrayList<>();
        trafficLights = new ArrayList<>();
        petrolStations = new ArrayList<>();
        // load scenario
        loadScenarioFromFile();
        readGraphNodesFromFile();
        readSimulationSettingsFromFile();
        instantiatePieceClasses();
        spawnDriversAndGeneratePath();
    }

    private void attachEventHandlers() {
        //attach an event handler to the menu item of the menu bar that shows about author info about the program
        tss_menubar.addAboutHandler(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Alert about = new Alert(Alert.AlertType.INFORMATION);
                about.setTitle("Simulation");
                about.setHeaderText("Traffic Simulator");
                about.setContentText("3 buttons below:"
                        + "\n\n"
                        + "Back button - to go back to the simulation settings screen to adjust parameters."
                        + "\n\n"
                        + "Play/pause simulation - to play or stop the simulation"
                        + "\n\n"
                        + "Statistics - open a window to display statistics");
                about.show();
            }
        });

        tss_menubar.addExitHandler(e -> System.exit(0));

        tss_menubar.addDisplayBoundingHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tss_menubar.getDisplayBoundingMenuItemName().equals("Display Driver Boundaries")) {
                    for (Driver d : drivers) {
                        d.setHBoxStyle(true);
                    }
                    tss_menubar.setDisplayBoundingMenuItemName("Hide Driver Boundaries");
                } else {
                    for (Driver d : drivers) {
                        d.setHBoxStyle(false);
                    }
                    tss_menubar.setDisplayBoundingMenuItemName("Display Driver Boundaries");
                }
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
                        at.start(); // start the animation timer
                        pause.setText("PAUSE SIMULATION");
                    }
                } else {
                    for (PathTransition pt: PathTransitionArray) {
                        pt.pause();
                        at.stop(); // stop the animation timer
                    }
                    pause.setText("PLAY SIMULATION");
                }
            }
        });

        tss_bottompane.addStatisticsHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TrafficSimulator_Statistics stats = null;
                try {
                    stats = new TrafficSimulator_Statistics();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene scene = new Scene(stats, 250, 400);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        });
    } // end of attachEventHandlers

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
                    if (n.getImage().getUrl().contains("trafficLight.png")) {
                        trafficLights.add(n);
                    } else if (n.getImage().getUrl().contains("dest_1_petrolStation.png")) {
                        petrolStations.add(n);
                    }
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

    private void readSimulationSettingsFromFile() throws IOException {
        BufferedReader bufferedReader = null;
        try {
            String CurrentLine;
            bufferedReader = new BufferedReader(new FileReader("parameters.txt"));
            while ((CurrentLine = bufferedReader.readLine()) != null) { // check if line is NOT null/empty
                String[] array = CurrentLine.split(", ");
                numOfDrivers = Integer.parseInt((array[0]));
                carSpawnChance = Integer.parseInt((array[1]));
                vanSpawnChance = Integer.parseInt((array[2]));
                numOfBuses = Integer.parseInt((array[3]));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            bufferedReader.close();
        }
    }

    /**
     * To instantiate classes for TrafficLights and PetrolStations
     */
    private void instantiatePieceClasses() {
        for (ImageView ps : petrolStations) {
            PetrolStation p = new PetrolStation();
            p.setImageView(ps);
        }
        for (ImageView ts : trafficLights) {
            TrafficLight t = new TrafficLight();
        }
    }

    private void spawnDriversAndGeneratePath() {

        System.out.println("drivers: " + numOfDrivers);
        System.out.println("car spawn chance: " + carSpawnChance);
        System.out.println("van spawn chance: " + vanSpawnChance);
        System.out.println("buses: " + numOfBuses);



        for (int i = 1; i <= numOfDrivers; i++) { // instantiate all drivers, based on the passed in number of drivers value
            Vehicle v = new Vehicle();
            v.setARandomType(0, 0);   // set the vehicle type based on the parameters of spawn chances, van or car?
            v.setColor(v.getRandomColour()); // give the vehicle a random colour



            Driver d = new Driver();    // instantiate a driver
                                        // additionally a randomized lane (left/right) string value.
                                        // starting nodes of drivers can be assigned later.
            drivers.add(d);
        }

        if (numOfBuses != 0) { // if number of buses is NOT equal to 0, meaning bus transportation is enabled
            // and we can assume there is atleast 1 or more bus drivers
            for (int i = 1; i <= numOfBuses; i++) { // instantiate all drivers, based on the passed in number of drivers value
                Driver d = new Driver();
                drivers.add(d);
            }
        }



        // create temp driver
        //Driver one = new Driver("left", graph.getRouteList(), graph.getRouteList().get(0), graph.getRouteList().get(310), true);
        //System.out.println(graph.getRouteList().get(111).getXCoordinate());
        //System.out.println(graph.getRouteList().get(111).getYCoordinate());
        //System.out.println(graph.getRouteList().get(111).getId());
        Driver two = new Driver();


        ImageView vehicle = one.getVehicle().getSpriteImageView();



        PathTransitionArray = new ArrayList<PathTransition>();
        PathTransition transition = new PathTransition();
        PathTransitionArray.add(transition);

        tss_editorpane.getChildren().add(one.getHBox());
        transition.setNode(one.getHBox());
        transition.setOrientation(PathTransition.OrientationType.NONE);

        transition.setDuration(Duration.seconds(30));
        transition.setRate(2);
        // driver one
        Path path = new Path();

        /*
        for (GraphNode node : graph.getRouteList()) {
            if (node.getId().contains("left_")) {
                System.out.println(node.getXCoordinate() + " " + node.getYCoordinate());
                LineTo l = new LineTo(node.getXCoordinate(), node.getYCoordinate()+20);
                path.getElements().add(l);
            }
        }
        path.getElements().add(new ClosePath());
         */

        ArrayList<GraphNode> p = one.autoSearch();
        path.getElements().add(new MoveTo(p.get(0).getXCoordinate(), p.get(1).getYCoordinate()+20));
        for (int i = 1; i < p.size(); i++) {
            LineTo l = new LineTo(p.get(i).getXCoordinate(), p.get(i).getYCoordinate()+20);
            path.getElements().add(l);
        }

        Label tst = new Label("A");
        tss_editorpane.getChildren().add(tst);
        tst.setTranslateX(40);
        tst.setTranslateY(100);

        transition.setPath(path);
        transition.setCycleCount(PathTransition.INDEFINITE);
        transition.play();

        System.out.println(path.getElements());

        at = new AnimationTimer() {
            @Override
            public void handle(long now) {

                // test method to check if something can intersect with the hbox, then do something
                if (one.getHBox().getBoundsInParent().intersects(tst.getBoundsInParent())) {
                    System.out.println("tst detected");
                }
                for (ImageView i : trafficLights) { // traffic light detection
                    if (one.getHBox().getBoundsInParent().intersects(i.getBoundsInParent())) {
                        System.out.println("traffic light detected");
                    }
                }
            }
        };

        at.start();

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



    } // end of spawn drivers and generate path method










    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    //debug method
    public void debugDisplayNodeLocation(GraphNode n, int num) {
        Circle circle = new Circle(n.getXCoordinate(), n.getYCoordinate(), 2.0f);
        if (num == 1) {
            circle.setFill(Color.LIGHTSKYBLUE);
        } else if (num == 2) {
            circle.setFill(Color.INDIANRED);
        }
        circle.setTranslateX(n.getXCoordinate());
        circle.setTranslateY(n.getYCoordinate());

        System.out.println(n);
        System.out.println("X coord: " + n.getXCoordinate());
        System.out.println("Y coord: " + n.getYCoordinate());
        tss_editorpane.getChildren().add(circle);
    }

} // end of class file
