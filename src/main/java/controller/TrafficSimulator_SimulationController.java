package controller;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.scene.control.ImageViewButton;
import model.*;
import view.*;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficSimulator_SimulationController {

    private TrafficSimulator_Simulation view;

    private TrafficSimulator_SimulationMenuBar tss_menubar;

    private TrafficSimulator_EditorEditorPane tss_editorpane;

    private TrafficSimulator_SimulationBottomPane tss_bottompane;

    private AStarGraph graph; // AStarGraph instance, holds all nodes of the scenario

    private int numOfDrivers; // holds the number of drivers as integer

    private int carSpawnChance; // holds the chance that cars will spawn as integer

    private int vanSpawnChance; // holds the chance that vans will spawn as integer

    //private int numOfBuses;

    private ArrayList<Driver> drivers; // holds all drivers in arraylist
    private ArrayList<TrafficLight> trafficLights; // holds all trafficlights in arraylist
    private ArrayList<ImageView> hazards; // holds all hazards in arraylist
    private ArrayList<PetrolStation> petrolStations; // holds all petrolstations in arraylist

    private ArrayList<GraphNode> allDestinationNodes; // A list of destination nodes in the scenario, contains nodes with id's that contain "destinationGoal".


    private AnimationTimer at; // animation timer runs when the PathTransitions are running

    private Scene scene;


    /**
     * Constructor for the SimulationController
     * @param view A TrafficSimulator_Simulation view instance
     * @throws IOException As the method loads the scenario, parameter settings and graphnodes from files, if these files don't exist - throw this exception
     * @throws InterruptedException Throw this exception if interuption occurs as the AnimationTimer and PathTransitions are running
     */
    public TrafficSimulator_SimulationController(TrafficSimulator_Simulation view) throws IOException, InterruptedException {

        //initialise view and model fields
        this.view = view;

        //initialise view subcontainer fields
        tss_menubar = view.getTSS_MenuBar();
        tss_editorpane = view.getTSS_EditorPane();
        tss_bottompane = view.getTSS_BottomPane();


        //attach event handlers to view using private helper method and initialise the private data members
        this.attachEventHandlers();
        graph = new AStarGraph();
        allDestinationNodes = new ArrayList<>();
        drivers = new ArrayList<>();
        hazards = new ArrayList<>();
        trafficLights = new ArrayList<>();
        petrolStations = new ArrayList<>();
        // load scenario with its pieces, graph nodes and the parameter settings that the user supplied.
        loadScenarioFromFile();
        readGraphNodesFromFile();
        readSimulationSettingsFromFile();
        try {
            spawnDriversAndGeneratePath(); // spawn the drivers and activate their path finding behaviour
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Scenario error");
            alert.setContentText("No destinations found");
            alert.show();
        }
        activateTrafficLights();
    }

    /**
     * This method attaches the event handlers of the controls from the simulation related view components.
     */
    private void attachEventHandlers() {

        /**
         * Attach an event handler to the About menu item, shows info about the buttons and what they do.
         */
        tss_menubar.addAboutHandler(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Alert about = new Alert(Alert.AlertType.INFORMATION);
                about.setTitle("Simulation");
                about.setHeaderText("Traffic Simulator");
                about.setContentText("3 buttons below:"
                        + "\n\n"
                        + "Back Button - To go back to the simulation settings screen to adjust parameters."
                        + "\n\n"
                        + "Play/Pause Simulation - To play or stop the simulation."
                        + "\n\n"
                        + "Statistics - Open a window to display statistics.");
                about.show();
            }
        });

        /**
         * This method attaches event handler to the exit item of the menu bar, when clicked exit the app.
         */
        tss_menubar.addExitHandler(e -> System.exit(0));

        /**
         * This method attaches event handler to the display boundaries item of the menu bar, when clicked, add styling to each HBox (driver) in the scenario
         * in order to display the borders around them, A.K.A the boundaries.
         */
        tss_menubar.addDisplayBoundingHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tss_menubar.getDisplayBoundingMenuItemName().equals("_Display Driver Boundaries")) {
                    for (Driver d : drivers) {
                        d.setHBoxStyle(true);
                    }
                    tss_menubar.setDisplayBoundingMenuItemName("_Hide Driver Boundaries");
                } else {
                    for (Driver d : drivers) {
                        d.setHBoxStyle(false);
                    }
                    tss_menubar.setDisplayBoundingMenuItemName("_Display Driver Boundaries");
                }
            }
        });

        /**
         * This method attaches event handler to the back button of the BottomPane of the Simulation Screen,
         * It stops the activity of all drivers and traffic lights in the scenario and switches the scene to the previous scene
         * Which was the Simulation Settings screen where you can tweak parameters.
         */
        tss_bottompane.addGoBackHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                at.stop();
                for (Driver d: drivers) {
                    d.getPathTransition().stop();
                    d.setNodesToDefaultState();
                }
                for (TrafficLight t : trafficLights) {
                    t.setActivatedState(false);
                }
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

        /**
         * This event handler handles the pause/play simulation button click events.
         * It checks if the text of the button equals "PLAY SIMULATION", so when it's clicked
         * The simulation should begin, this is done by getting all the drivers in the scenario
         * and starting their PathTransition animation, as well as activating the AnimationTimer that
         * checks for intersections/overlapping boundaries, then replacing the button text to "PAUSE SIMULATION"
         * So the next time the button is pressed, it will stop the PathTransition animations for all drivers and
         * de-activate the AnimationTimer, thus halting intersection/boundary checking as well as Traffic Light operation.
         */
        tss_bottompane.addPauseSimulationHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Button pause = tss_bottompane.getStartAndPauseButton();
                if (pause.getText().equals("PLAY SIMULATION")) {
                    for (Driver d: drivers) {
                        d.getPathTransition().play();
                        at.start(); // start the animation timer
                    }
                    pause.setText("PAUSE SIMULATION");
                } else {
                    for (Driver d: drivers) {
                        d.getPathTransition().pause();
                        at.stop(); // stop the animation timer
                    }
                    pause.setText("PLAY SIMULATION");
                }
            }
        });

        /**
         * This method attaches an event handler to the Statistics Button in the BottomPane, it creates a new Stage (Window)
         * that displays the statistics of the scenario, each driver, each petrolstation etc.
         */
        tss_bottompane.addStatisticsHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TrafficSimulator_Statistics stats = null;
                try {
                    stats = new TrafficSimulator_Statistics();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    TrafficSimulator_StatisticsController statsController = new TrafficSimulator_StatisticsController(stats);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene scene = new Scene(stats, 300, 450);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Statistics");
                stage.show();
            }
        });
    } // end of attachEventHandlers

    /**
     * This method loads scenario from a text file, by clearing all nodes in the EditorPane (The GridPane) and reading each line to
     * re-instantiate the ImageViewButtons based on the saved scenario to re-populate the GridPane.
     * @throws IOException If the .txt file doesn't exist or cannot be accessed, throw this exception.
     */
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
                        n.setFitWidth(20);
                        n.setFitHeight(20);
                        TrafficLight t = new TrafficLight(n);
                        trafficLights.add(t);
                    } else if (n.getImage().getUrl().contains("cone") || n.getImage().getUrl().contains("barrier")) {
                        n.setFitWidth(15);
                        n.setFitHeight(15);
                        hazards.add(n); // hazards don't require a class as they only invoke a specific behaviour.
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
                if (cellButton.getImage().getUrl().contains("dest_1_petrolStation.png")) {
                    PetrolStation ps = new PetrolStation(cellButton);
                    petrolStations.add(ps);
                }
                tss_editorpane.add(cellButton, col, row);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            bufferedReader.close();
        }
    }

    /**
     * This method reads in the graph nodes from the text file and adds each graph node to the routeList variable of the AStarGraph instance.
     * @throws FileNotFoundException If the .txt file is not found in the root directory, throw this exception.
     */
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

                if (n.getId().contains("destinationGoal")) { // if the id of the graphnode contains "destinationGoal" it's a classified destination
                    allDestinationNodes.add(n); // add this node to the list of all destination nodes.
                    graph.addGraphNodeToList(n);
                } else {
                    graph.addGraphNodeToList(n); // else, if the id is not destinationGoal and just a regular d node or left, right node - add it to the graph's routelist.
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method reads the simulation settings that the user provided in the previous screen/scene.
     * It reads each line and initializes the private data members related to the settings to the values from the text file.
     * @throws IOException If the text file cannot be accessed, throw this exception.
     */
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
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            bufferedReader.close();
        }
    }

    /**
     * This method is responsible for spawning the drivers in the scenario based on the numOfDrivers value and the CarSpawnChance / VanSpawnChance values
     * It gives each driver the routelist from the graph instance, so each driver is able to access every node in the scenario
     * It then assigns each driver a random starting position through a duplicated routeList, and each assignment of a GraphNode is deleted
     * to prevent another driver from also spawning at the same starting position.
     * Then each driver is given a random destination node. They are re-instantiated based on the constructor and then their
     * path-finding behaviour is activated.
     */
    private void spawnDriversAndGeneratePath() {

        System.out.println("drivers: " + numOfDrivers);
        System.out.println("car spawn chance: " + carSpawnChance);
        System.out.println("van spawn chance: " + vanSpawnChance);



        for (int i = 1; i <= numOfDrivers; i++) { // instantiate all drivers, based on the passed in number of drivers value
            Driver d = new Driver();    // instantiate a default driver
            d.setRouteList(graph.getRouteList()); // assign the list of all nodes to the driver
            drivers.add(d);
        }

        ///////////////////////////////////////////////////////////////////
        // RANDOM STARTING NODE ASSIGNMENT TO ALL DRIVERS IN THE SCENARIO//
        ///////////////////////////////////////////////////////////////////
        ArrayList<GraphNode> duplicatedRouteList = new ArrayList<>();
        duplicatedRouteList.addAll(graph.getRouteList()); // duplicate the route list (list of all nodes)
//        Iterator<GraphNode> iter = duplicatedRouteList.iterator();
//        while(iter.hasNext()) {
//            GraphNode n = iter.next();
//            if (n.getId().contains("destinationGoal")) {
//                iter.remove(); // remove it from duplicatedRouteList to ensure, more than 1 driver can't spawn on a destination node
//            }
//        }


        for (int ind = 0; ind < drivers.size(); ind++) {

            Random rand = new Random();
            int randomIndex = rand.nextInt(0, duplicatedRouteList.size()); // generate a random index num

            GraphNode randomNode = duplicatedRouteList.get(randomIndex);

            while (randomNode.getId().contains("d")) { // enter while loop if the random node contains 'd' meaning destination
                randomIndex = rand.nextInt(0, duplicatedRouteList.size());
                randomNode = duplicatedRouteList.get(randomIndex); // generate a new random index to get a random node, with an id that's either contains left or right.
            }

            if (randomNode.getId().contains("left")) { // read its id, set the driver's lane to the id and startnode, but if its destination don't assign.
                drivers.get(ind).setLane("left");
                drivers.get(ind).setStartNode(randomNode);
            } else if (randomNode.getId().contains("right")) {
                drivers.get(ind).setLane("right");
                drivers.get(ind).setStartNode(randomNode);
            }

            // using iterator to modify the list to prevent Java ConcurrentModification Exception
            Iterator<GraphNode> iter2 = duplicatedRouteList.iterator();
            while(iter2.hasNext()) {
                GraphNode n = iter2.next();
                if (n == randomNode) { // assuming the random node has been assigned
                    iter2.remove(); // remove it from duplicatedRouteList to ensure, more than 1 driver can't share the same start node
                }
            } // end of iterator

        } // end of FOR loop


        //////////////////////////////////////////////////////////////////////
        // RANDOM DESTINATION NODE ASSIGNMENT TO ALL DRIVERS IN THE SCENARIO//
        //////////////////////////////////////////////////////////////////////
        for (int ind = 0; ind < drivers.size(); ind++) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(0, allDestinationNodes.size()); // generate a random index num
            GraphNode randomDestination = allDestinationNodes.get(randomIndex);
            drivers.get(ind).setGoalNode(randomDestination);
        }


        /////////////////////////////////////////////////////////////
        // Place all drivers onto the scenario and generate the paths, and play!
        /////////////////////////////////////////////////////////////
        for (int b = 0; b < drivers.size(); b++) {
            UUID id = UUID.randomUUID();
            Driver d = new Driver(id, drivers.get(b).getLane(), graph.getRouteList(), drivers.get(b).getStartNode(), drivers.get(b).getGoalNode(), false); // re-instantiate the driver in the form of the constructor so other Driver fields
            drivers.set(b, d);

            Vehicle v = new Vehicle();
            v.setARandomType((double) carSpawnChance / 100, (double) vanSpawnChance / 100);   // set the vehicle type based on the parameters of spawn chances, van or car?
            v.setColor(v.getRandomColour()); // give the vehicle a random colour
            d.setVehicle(v);

            //debug
            System.out.println(d.getStartNode().getId() + "      " + d.getStartNode().getXCoordinate() + " " + d.getStartNode().getYCoordinate());
            System.out.println(d.getGoalNode().getId() + "        " + d.getGoalNode().getXCoordinate() + " " + d.getGoalNode().getYCoordinate());

            // can be initialized such as checkedList, openList etc.

            Path path = new Path();
            ArrayList<GraphNode> p = new ArrayList<>();
            p = d.autoSearch();
            path.getElements().add(new MoveTo(p.get(0).getXCoordinate(), p.get(1).getYCoordinate()+18));
            for (int i = 1; i < p.size(); i++) {
                LineTo l = new LineTo(p.get(i).getXCoordinate(), p.get(i).getYCoordinate()+18);
                path.getElements().add(l);
            }
            d.getPathTransition().setPath(path);

            d.getHBox().setMaxSize(25, 35);
            tss_editorpane.getChildren().add(d.getHBox()); // add the hboxes of all drivers to the scenario so it can be used in the simulation

            //d.setNodesToDefaultState();
            d.getPathTransition().play();
        }

        /*
        ArrayList<GraphNode> p = one.autoSearch();
        path.getElements().add(new MoveTo(p.get(0).getXCoordinate(), p.get(1).getYCoordinate()+20));
        for (int i = 1; i < p.size(); i++) {
            LineTo l = new LineTo(p.get(i).getXCoordinate(), p.get(i).getYCoordinate()+20);
            path.getElements().add(l);
        }
         */

        /* LABEL DETECTION TEST
        Label tst = new Label("A");
        tss_editorpane.getChildren().add(tst);
        tst.setTranslateX(40);
        tst.setTranslateY(100);
         */


        at = new AnimationTimer() {
            @Override
            public void handle(long now) {

                // stops the pathtransition / movement of a driver when it reaches the goal node, assign new destination
                for (int index = 0; index < drivers.size(); index++) {
                    if (drivers.get(index).getPathTransition().getCurrentTime().toMillis() > 29500) {

                        System.out.println("stopped");

                        Driver d = drivers.get(index);

                        d.getPathTransition().stop();

                        UUID id = d.getUniqueID();
                        String lane = d.getLane();
                        GraphNode currentNode = d.getGoalNode();
                        Vehicle veh = d.getVehicle();
                        HBox h = d.getHBox();
                        //d.setNodesToDefaultState();

//                        ArrayList<GraphNode> listOfAllNodes = new ArrayList<>();
//                        listOfAllNodes.addAll(d.getRouteList());


                        Random rand = new Random();
                        int randomIndex = rand.nextInt(0, allDestinationNodes.size()); // generate a random index num
                        GraphNode randomDestination = allDestinationNodes.get(randomIndex);

                        while (randomDestination.getId().equals(d.getGoalNode().getId())) {
                            randomIndex = rand.nextInt(0, allDestinationNodes.size()); // generate a random index num
                            randomDestination = allDestinationNodes.get(randomIndex);
                        }

                        d = new Driver(id, lane, graph.getRouteList(), currentNode, randomDestination, false);
                        //d.setNodesToDefaultState();
                        d.setVehicle(veh);
                        d.setHBox(h);

                        drivers.set(index, d);

                        System.out.println("new start node: " + currentNode.getId());
                        System.out.println("new goal node: " + randomDestination.getId());

                        Path path = new Path();
                        ArrayList<GraphNode> p = new ArrayList<>();
                        p = d.autoSearch();
                        path.getElements().add(new MoveTo(p.get(0).getXCoordinate(), p.get(1).getYCoordinate()+18));
                        for (int i = 1; i < p.size(); i++) {
                            LineTo l = new LineTo(p.get(i).getXCoordinate(), p.get(i).getYCoordinate()+18);
                            path.getElements().add(l);
                        }

                        d.getPathTransition().setPath(path);
                        d.getPathTransition().setNode(d.getHBox());
                        d.getPathTransition().setOrientation(PathTransition.OrientationType.NONE);
                        d.getPathTransition().setDuration(Duration.seconds(30));
                        d.getPathTransition().setRate(1.1);
                        d.getPathTransition().setCycleCount(PathTransition.INDEFINITE);

                        System.out.println(path.getElements());

                        d.getPathTransition().play();
                    }
                }

                // traffic light detection, checks if every driver's HBOX intersects with every traffic light
                for (int c = 0; c < drivers.size(); c++) {
                    for (int d = 0; d < trafficLights.size(); d++) {
                        if (drivers.get(c).getHBox().getBoundsInParent().intersects(trafficLights.get(d).getImageView().getBoundsInParent())) {
                            //System.out.println("traffic light detected");
                            if (trafficLights.get(d).getCurrentSignal().equals("red")) {
                                drivers.get(c).getPathTransition().pause();
                            } else if (trafficLights.get(d).getCurrentSignal().equals("amber")) {
                                drivers.get(c).getPathTransition().pause();
                            } else if (trafficLights.get(d).getCurrentSignal().equals("green")) {
                                drivers.get(c).getPathTransition().play();
                            }
                        }
                    }
                }

                // hazard detection, checks if every driver's HBOX intersects with every hazard
                for (int c = 0; c < drivers.size(); c++) {
                    for (int d = 0; d < hazards.size(); d++) {
                        if (drivers.get(c).getHBox().getBoundsInParent().intersects(hazards.get(d).getBoundsInParent())) {
                            //System.out.println("hazard detected");

                            drivers.get(c).getPathTransition().pause();

                            ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
                            int finalC = c;
                            Runnable task1 = () -> {
                                drivers.get(finalC).activateHazardMode();
                            };

                            //run this task after 2 seconds
                            ses.schedule(task1, 2, TimeUnit.SECONDS);

                            ses.shutdown();
                        }
                    }
                }


//                // vehicle collision test
//                for (int c = 0; c < drivers.size(); c++) {
//                    for (int d = 0; d < drivers.size(); d++) {
//                        if (!(d == c)) { // if the outer for loop value is NOT the same as the inner for loop value - this is to prevent checking if a driver intersects with itself.
//                            if (drivers.get(c).getHBox().getBoundsInParent().intersects(drivers.get(d).getHBox().getBoundsInParent())) {
//                                System.out.println("collision detected");
//                            }
//                        }
//                    }
//                }

                // FUEL LEVEL DECREASING - every driver's fuel decreases by 0.0010 when the simulation is running / animationtimer is activated
                for (Driver d : drivers) { // fuel will decrease for all cars when scenario has started
                    double fuel = d.getVehicle().getFuelLevel();
                    //System.out.println("fuel = " + fuel);
                    if (fuel == 0.00) { // when fuel is 0.0
                        d.getPathTransition().pause(); // stop the vehicle.
                    }
                    d.getVehicle().setFuelLevel(fuel - 0.0010); // decrease the fuel level by 0.01 each frame
                }

                // if near petrol station, refuel, check if every driver's HBOX intersects with every petrol station. then refuel if there's an intersection.
                for (Driver d : drivers) {
                    for (int p = 0; p < petrolStations.size(); p++) {
                        if (d.getHBox().getBoundsInParent().intersects(petrolStations.get(p).getImageViewButton().getBoundsInParent())) {
                            //System.out.println("petrol station detected");
                            double fuel = d.getVehicle().getFuelLevel(); // e.g. 4.80 L
                            if (petrolStations.get(p).getOutOfFuelValue() != true) {
                                d.getVehicle().setFuelLevel(5.00); // refuel to 5.00 L (0.20L increment)
                                double calc = 5.00 - fuel; // 5.00 - 4.80 = 0.20 L
                                petrolStations.get(p).addSales(petrolStations.get(p).getPricePerLitre() * calc);
                                petrolStations.get(p).reduceTotalFuel(calc);
                            }
                        }
                    }
                }

                // WRITING OUT STATISTICS TO TEXT FILES
                try {
                    writeDriverAndVehicleStats();
                    writePetrolStationStats();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        };
        at.start(); // start the animation timer
    } // end of spawn drivers and generate path method


    /**
     * This method is responsible for activating the traffic lights, it first checks if the trafficLights arraylist is empty, if not
     * then activate every traffic light.
     * @throws InterruptedException Traffic Lights use ScheduledExecutorService class, any interruptions will cause this exception to be thrown.
     */
    public void activateTrafficLights() throws InterruptedException {
        if (!trafficLights.isEmpty()) {
            for (TrafficLight t : trafficLights) {
                t.activateTrafficLight(true);
            }
        }
    }

    /**
     * Writes out driver and vehicle statistics and data values to a text file in the statistics folder, which is
     * read by the Statistics Controller so it can be displayed in the GUI interface for the statistics window
     * @throws IOException if the file path cannot be accessed, throw this exception.
     */
    public void writeDriverAndVehicleStats() throws IOException {
        DecimalFormat f = new DecimalFormat("##.00");
        File file = new File("statistics\\drivers.txt");
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (Driver d: drivers) {
            printWriter.println(d.getUniqueID());
            printWriter.println(d.getVehicle().getType());
            printWriter.println(d.getVehicle().getSpriteImageView().getImage().getUrl());
            printWriter.println(d.getVehicle().getColor());
            printWriter.println(f.format(d.getVehicle().getFuelLevel()));
            printWriter.println(d.getHBox().getRotate());
            printWriter.println(d.getHazLightsOn());
            printWriter.println(d.getLane());
            printWriter.println(d.getStartNode().getId());
            printWriter.println(d.getGoalNode().getId());
            printWriter.println(d.getCurrentNode().getId());
            //printWriter.print('\n');
        }

        fileWriter.close();
        printWriter.close();
    }

    /**
     * Writes out petrol station statistics and data values to a text file in the statistics folder, which is
     * read by the Statistics Controller so it can be displayed in the GUI interface for the statistics window
     * @throws IOException if the filepath cannot be accessed, throw this exception.
     */
    public void writePetrolStationStats() throws IOException {
        DecimalFormat df = new DecimalFormat("0.00"); // easy rounding of double numbers to 2 decimal places
        df.setRoundingMode(RoundingMode.UP); // round up values

        File file = new File("statistics\\petrolstations.txt");
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (PetrolStation ps : petrolStations) {
            printWriter.println(ps.getUniqueID());
            printWriter.println(ps.getName());
            printWriter.println(ps.getImageViewButton().getImage().getUrl());
            printWriter.println(df.format(ps.getTotalFuel()));
            printWriter.println(ps.getPricePerLitre());
            printWriter.println(df.format(ps.getSales()));
            printWriter.println(ps.getOutOfFuelValue());
            //printWriter.print('\n');
        }

        fileWriter.close();
        printWriter.close();
    }


    /**
     * This is a debug method that displays a location of a GraphNode by adding a circle (or marker) to its position
     * @param n GraphNode
     * @param num Integer 1 or 2, 1 for Light Sky Blur coloured circle, or 2 for Indian Red Colour
     */
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
