package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import model.GraphNode;
import model.AStarGraph;
import view.*;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class TrafficSimulator_EditorController {

    private TrafficSimulator_Editor view;

    private TrafficSimulator_EditorMenuBar tse_menubar;
    private TrafficSimulator_EditorBottomPane tse_bottompane;

    private TrafficSimulator_EditorEditorPane tseep;

    private AStarGraph graph; // AStarGraph to store all graphnodes of the scenario

    private int CurrentRotationDegrees = 0; // current rotation value
    private String currentPieceSelected = ""; // a string to store the URI of the selected piece from the bottom pane

    private String pieceType = ""; // a string to store the type of the piece, e.g. hazard, decorative, road etc.

    private int pieceCounter = 0; // a counter to reflect how many nodes have been added to the scenario, does NOT include miscPieces such as decorative or hazard pieces.
    private boolean muteSounds = false; // a boolean variable to enable / disable sounds

    private int nodesDisplayed = 0; // an integer variable to represent the activation and de-activation of displaying the nodes of the roads in the scenario

    private boolean isScenarioEmpty; // a boolean variable that represents whether the scenario is empty or not.

    private Scene currentScene;

    private Scene scene;


    /**
     * Constructor for the Controller of the Environment Editor feature
     * It accepts an instance of the editor scene and initializes all the necessary fields and methods
     * Such as attaching the event handlers to each of the controls of the Editor View components
     * @param view An instance of the TrafficSimulator_Editor scene
     */
    public TrafficSimulator_EditorController(TrafficSimulator_Editor view) {
        //initialise view and model fields
        this.view = view;

        //initialise view subcontainer fields
        tse_menubar = view.getTSE_MenuBar();
        tse_bottompane = view.getTSE_BottomPane();
        tseep = view.getTSE_EditorPane();

        tse_bottompane.setCurrentRotationValue(CurrentRotationDegrees);

        //attach event handlers to view using private helper method
        //and instantiate the A* graph
        this.attachEventHandlers();
        graph = new AStarGraph();
        setCurrentScene(tseep.getScene());
    }

    /**
     * This method attaches the event handlers to the respective controls in each of the editor related view classes that form the whole
     * Environment Editor screen
     */
    private void attachEventHandlers() {

        /**
         * This method attaces an event handler to the about menu item of the menu bar that shows info about the controls for the app.
         */
        tse_menubar.addAboutHandler(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Alert about = new Alert(Alert.AlertType.INFORMATION);
                about.setTitle("Controls");
                about.setHeaderText("Controls for Traffic Simulator");
                about.setContentText("Left click on pieces to select them from the bottom panel."
                                + '\n' +
                        "To place your piece, left click anywhere within the scenario panel."
                        + "\n\n"
                        + "F Key - Deselect Piece"
                        + "\n\n"
                        + "V key - Invert Lanes (only for turn pieces)"
                        + "\n\n"
                        + "R key - Rotate Piece By 90 Degrees"
                        + "\n\n"
                        + "When rotating road surfaces, an icon will appear at the bottom right corner to display the rotated piece."
                        + "\n\n"
                        + "GREEN arrow represents the LEFT lane, whereas the YELLOW arrow represents the RIGHT lane.");
                about.show();
            }
        });

        /**
         * Attach an event handler to the menu bar's exit item that closes the application
         */
        tse_menubar.addExitHandler(e -> System.exit(0));

        /**
         * Attach an event handler to the menu bar's save item that allows the user to save the current scenario
         */
        tse_menubar.addSaveScenarioHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt*"));
                Stage stage = (Stage) tse_bottompane.getScene().getWindow();
                try {
                    File scenarioPath = fileChooser.showSaveDialog(stage);
                    if (!(scenarioPath.getPath().contains(".txt"))) {
                        scenarioPath = new File(scenarioPath.getPath() + ".txt");
                    }
                    writeScenarioDataToFile(scenarioPath);
                    writeGraphObjectToFile(scenarioPath);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText("Scenario has been saved.");
                    alert.setContentText("Scenario file saved at: " + scenarioPath);
                    alert.showAndWait();
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        });

        /**
         * Attach an event handler to the menu bar's load item, so it can load a scenario from a text file.
         */
        tse_menubar.addLoadScenarioHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tse_menubar.getClearScenarioItem().fire(); // clear the scenario before loading in another one
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt*"));
                Stage stage = (Stage) tse_bottompane.getScene().getWindow();
                try {
                    File scenarioPath = fileChooser.showOpenDialog(stage);
                    loadScenarioFromFile(scenarioPath);
                    readGraphObjectFromFile(scenarioPath);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText("Scenario has been loaded.");
                    alert.setContentText("Scenario file loaded from: " + scenarioPath);
                    alert.showAndWait();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        /**
         * Attach an event handler to the Mute Sounds menu bar item, so the sounds can be disabled at will by the user
         * Such sounds disabled include: rotation sound, build/place piece sound and the invert lanes sound
         */
        tse_menubar.addMuteSoundsHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getMuteSounds() == true) {
                    setMuteSounds(false);
                    tse_menubar.setMuteItemName("_Mute Sounds");
                } else {
                    setMuteSounds(true);
                    tse_menubar.setMuteItemName("_Unmute Sounds");
                }
            }
        });

        /**
         * Attach an event handler to the Clear Scenario item to reset the scenario to a default state.
         * This default state is 7 x 24 grid with all grass pieces
         */
        tse_menubar.addClearScenarioHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane editorPane = tseep.getCurrentEditorPane();
                ArrayList<Node> miscPiecesToRemove = new ArrayList<Node>();
                for (Node node: editorPane.getChildren()) {
                    if (node instanceof ImageViewButton) { // if the node can be casted to ImageViewButton, and is of that type
                        File grass = new File("img\\2_EditorScreen\\grass.png");
                        ((ImageViewButton) node).setImage(new Image(grass.toURI().toString()));
                        ((ImageViewButton) node).setFitHeight(45);
                        ((ImageViewButton) node).setFitWidth(40);
                        ((ImageViewButton) node).setPreserveRatio(true);
                    } else {
                        if (node instanceof ImageView) {
                            miscPiecesToRemove.add(node);
                        }
                    }
                }
                tseep.getChildren().removeAll(miscPiecesToRemove);
                currentPieceSelected = "";
                pieceType = "";
                CurrentRotationDegrees = 0;
                graph.clearGraphNodeList();
                resetSlotSelection();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Scenario has been cleared.");
                alert.showAndWait();
            }
        });

        /**
         * Attach the event handler to the Display Nodes menu bar item that displays all nodes in the scenario
         */
        tse_menubar.addDisplayAllNodesHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tse_menubar.getDisplayNodesItemName().equals("_Display All Nodes")){
                    tse_menubar.setDisplayNodesItemName("_Hide All Nodes");
                    nodesDisplayed = 1;
                    for (GraphNode n : graph.getRouteList()) {
                        Circle circle = new Circle(n.getXCoordinate(), n.getYCoordinate(), 2.0f);
                        circle.setFill(Color.ORANGE);
                        circle.setTranslateX(n.getXCoordinate());
                        circle.setTranslateY(n.getYCoordinate());
                        tseep.getChildren().add(circle);
                    }
                } else {
                    Iterator<Node> iter = tseep.getChildren().iterator();
                    while(iter.hasNext()) {
                        Node n = iter.next();
                        if (n instanceof Circle) {
                            iter.remove();
                        }
                    }
                    nodesDisplayed = 0;
                    tse_menubar.setDisplayNodesItemName("_Display All Nodes");
                }
            }
        });

        /**
         * Attach an event handler to the 4 button group (ButtonsPane), specifically to the Road Surface button so that it
         * changes the pieces to the road pieces in the PieceSelection panel.
         */
        tse_bottompane.getPiecesButtonsPane().addRoadSurfaceButtonOnClickHandler(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pieceType = "road";
                ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
                for (int i = 0; i < arrayOfSlots.length; i++) {
                    arrayOfSlots[i].setDisable(false);
                    arrayOfSlots[i].setVisible(true);
                }
                tse_bottompane.getPiecesSelection().setToRoadSurfacePieces();
                Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));

            }
        });

        /**
         * Attach an event handler to the 4 button group (ButtonsPane), specifically the destinations button so that
         * when it is clicked, display all the destination related pieces in the PieceSelection panel.
         */
        tse_bottompane.getPiecesButtonsPane().addDestinationsButtonOnClickHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pieceType = "destination";
                tse_bottompane.getPiecesSelection().setToDestinationPieces();

                ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
                for (int i = 0; i < arrayOfSlots.length-1; i++) {
                    arrayOfSlots[i].setDisable(false);
                    arrayOfSlots[i].setVisible(true);
                }

                Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
                tse_bottompane.setRotationImage(new WritableImage(45, 45));
            }
        });

        /**
         * Attach an event handler to the 4 button group (ButtonsPane) such that the Decorative Button, when
         * clicked, will display all the decorative related pieces in the PieceSelection panel.
         */
        tse_bottompane.getPiecesButtonsPane().addDecorativeButtonOnClickHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pieceType = "decorative";
                tse_bottompane.getPiecesSelection().setToDecorativePieces();

                ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
                for (int i = 0; i < arrayOfSlots.length-1; i++) {  //-1 applied so Bus Stop doesn't appear in 'decorative'
                    arrayOfSlots[i].setDisable(false);
                    arrayOfSlots[i].setVisible(true);
                }

                Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
                tse_bottompane.setRotationImage(new WritableImage(45, 45));
            }
        });

        /**
         * Attach an event handler to the 4 button group (ButtonsPane) such that the Hazards button, when clicked,
         * will display all the hazard related pieces in the PieceSelection panel.
         */
        tse_bottompane.getPiecesButtonsPane().addHazardsButtonOnClickHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pieceType = "hazard";
                tse_bottompane.getPiecesSelection().setToHazardPieces();

                ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
                for (int i = 4; i < arrayOfSlots.length; i++) {
                    arrayOfSlots[i].setDisable(true);
                    arrayOfSlots[i].setVisible(false);
                }

                Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
                tse_bottompane.setRotationImage(new WritableImage(45, 45));
            }
        });

        /**
         * Attach an event handler to the back button in the BottomPane, so that it swaps the scene of the current stage
         * (Window) to the previous scene, which is the main menu where you can choose between Creating a scenario and Procedurally generating a scenario.
         */
        tse_bottompane.addGoBackHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) tse_bottompane.getScene().getWindow(); // get the current window and cast to Stage
                scene = tse_bottompane.getScene(); // get the current scene
                scene.setCursor(null);
                TrafficSimulatorRootPane view = new TrafficSimulatorRootPane();

                new TrafficSimulatorController(view);
                scene.setRoot(view); // change the root node of the current scene to the new screen
                stage.setTitle("Traffic Simulator - Environment Options");
            }
        });

        /**
         * Attach an event handler to the finish button in the BottomPane, so that it swaps the scene of the current stage
         * (Window) to the next scene, which is the simulation settings screen where you can tweak parameters.
         */
        tse_bottompane.addFinishHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    writeScenarioDataToFile(new File(""));
                    writeGraphNodesToFile();
                    isScenarioEmpty = false;
                    checkIfScenarioIsEmpty();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassCastException e) {}
                if (!isScenarioEmpty && nodesDisplayed != 1) {
                    Stage stage = (Stage) tse_bottompane.getScene().getWindow(); // get the current window and cast to Stage
                    scene = tse_bottompane.getScene(); // get the current scene
                    scene.setCursor(null);
                    TrafficSimulator_SimulatorSettings view = new TrafficSimulator_SimulatorSettings();
                    try {
                        new TrafficSimulator_SimulatorSettingsController(view);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    scene.setRoot(view); // change the root node of the current scene to the new screen
                    stage.setTitle("Traffic Simulator - Simulation Options");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Scenario error");
                    alert.setContentText("Scenario is unmodified, no roads or destinations found, or display nodes is enabled.");
                    alert.show();
                }
            }
        });

        /**
         * Attach an event handler to each ImageView slot in the PieceSelection pane, which is a part of the BottomPane of the editor.
         * This allows it such that when the click event of the slot is fired, it retrieves the source of the event, which is the specific
         * ImageView control that was clicked and retrieving the image held in that ImageView and setting it as the 'selected' piece that
         * will be placed, whenever the user places it in the scenario.
         */
        tse_bottompane.getPiecesSelection().CursorOnClickHandler(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CurrentRotationDegrees = 0;
                tse_bottompane.setCurrentRotationValue(CurrentRotationDegrees);
                ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
                Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
                ImageView obj = (ImageView) event.getSource();
                currentPieceSelected = obj.getImage().getUrl();
                tse_bottompane.setRotationImage(currentPieceSelected);

                // Debugging purposes
                // ***************
                // System.out.println(currentPieceSelected);
                // ***************

                ColorAdjust colorAdjust_pressed = new ColorAdjust();
                //Setting the brightness value and apply effect to ImageView from event
                colorAdjust_pressed.setBrightness(0.75);
                obj.setEffect(colorAdjust_pressed);

            }
        });

        /**
         * This method calls the add_BuildHandler class and provides an EventHandler declaration in the
         * current editor pane instance of the application, Attaching an event handler to each SLOT of the 7 x 24 EditorPane (GridPane) of ImageViewButtons.
         *
         * First it plays a sound effect, but it must check if sounds are muted, if so - then the sound won't play.
         * Then check if a piece is selected, if not then show error message and handle the exceptions.
         * The piece selected, should NOT BE OF DECORATIVE OR HAZARD TYPE, as these are free-form placed nodes handled seperately.
         *
         * It accepts the generated event from a node (clickable ImageViewButton in the editor pane) that is
         * specifically a mouse event. It retrieves the source of the event, which is the specific ImageViewButton in the grid, and
         * replaces the grass.png image it contains or whatever image it contains to the new one - using the URI from the currentPieceSelected variable but before that - if the piece beforehand
         * contained an image that is not grass, it means it was either a road or destination, thus nodes were previously there. So using an iterator
         * and bounds from the top left corner to the bottom right corner of the ImageViewButton, the nodes are consecutively removed.
         *
         * The currentPieceSelected variable, which is a String that holds the selected piece URI, is checked and the waypoints/nodes
         * are specifically and accurately mapped to the piece by retrieving the default X and Y bounds of the ImageViewButton and manually
         * tweaking the X and Y coordinates so the nodes are situated in the correct location. Each piece has nodes with an interval distance of 5 pixels between them.
         * However, some pieces have an interval distance of 2 pixels.
         *
         */
        tseep.add_BuildHandler(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                incrementPieceCounter();
                File file = new File("sfx\\build.wav");
                AudioInputStream sfx = null;
                try {
                    sfx = AudioSystem.getAudioInputStream(file);
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!getMuteSounds()) {
                    try {
                        Clip clip = AudioSystem.getClip();
                        clip.open(sfx);
                        clip.start();
                        //clip.close();
                    } catch (Exception e) {}
                }
                setCurrentScene(tseep.getScene());
                try {
                    if (!(pieceType.equals("decorative") || pieceType.equals("hazard"))) { // ensure the piece type is not equal to decorative type or hazard type
                        // as those pieces should strictly be placed in free-form mode only and should not be placed as an ImageViewButton type.
                        ImageViewButton obj = (ImageViewButton) event.getSource();

                        // remove any graphnodes in the selected ImageViewButton if any exist
                        if (!(obj.getImage().getUrl().contains("grass.png"))) {  // if the ImageViewButton in question DOES NOT contain grass.png
                            // meaning, it is not a grass piece thus it can only be a road piece or a destination piece, thus graphnodes exist in this cell
                            Bounds b = obj.getBoundsInParent(); // get the bounds or coordinates of the ImageViewButton in scope of the parent, that is the VBox layout

                            // these 2 graph nodes are for debugging and define the top left corner coordinate and bottom right corner coordinate for EVERY piece
                            GraphNode topLeftCornerOfCell = new GraphNode(b.getMinX(), b.getMinY()-23);  // x1, y1
                            GraphNode bottomRightCornerOfCell = new GraphNode(b.getMinX()+32, b.getMinY()+23); // x2, y2

                            ArrayList<GraphNode> listOfCurrentGraphNodes = graph.getRouteList();


                            // using iterator to modify the graph node list to prevent Java ConcurrentModification Exception
                            Iterator<GraphNode> iter = listOfCurrentGraphNodes.iterator();
                            while(iter.hasNext()) {
                                GraphNode n = iter.next();
                                // if there are any nodes in between the points defined between the top left corner of the piece and the bottom right corner of the piece then...
                                if ( (n.getXCoordinate() >= b.getMinX() && n.getYCoordinate() >= b.getMinY()-23) && (n.getXCoordinate() <= b.getMinX()+32 && n.getYCoordinate() <= b.getMinY()+23) ) {
                                    iter.remove(); // Removes the 'current' item
                                }
                            }
                        }
                        obj.setImage(new Image(currentPieceSelected));
                        obj.setFitHeight(45);
                        obj.setFitWidth(40);
                        obj.setPreserveRatio(true);
                        Bounds bounds = tseep.getBoundsInParent();
                        Bounds b = obj.getBoundsInParent(); // gets the bounds/coordinates of each imageviewbutton/cellbutton inside the pane which is inside the entire VBox layout.


                        /**
                         * Example of node mapping to the piece image, using bounds and tweaking X and Y coordinates.
                         */
                        // if the currentPiece that was added is a straight road piece THEN
                        if (currentPieceSelected.contains("1_straightRoad.png")) {
                            // create a set of graph nodes, originally all placed at a default position of each ImageViewButton, then manually positioned using addition/subtraction
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+10, b.getMinY()-20);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+25, b.getMinY()+20);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode rightLane_5 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode rightLane_6 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode rightLane_7 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode rightLane_8 = new GraphNode(b.getMinX()+25, b.getMinY()-20);

                            leftLane_0.setId("left_"+(getPieceCounterValue()));
                            leftLane_1.setId("left_"+(getPieceCounterValue()));
                            leftLane_2.setId("left_"+(getPieceCounterValue()));
                            leftLane_3.setId("left_"+(getPieceCounterValue()));
                            leftLane_4.setId("left_"+(getPieceCounterValue()));
                            leftLane_5.setId("left_"+(getPieceCounterValue()));
                            leftLane_6.setId("left_"+(getPieceCounterValue()));
                            leftLane_7.setId("left_"+(getPieceCounterValue()));
                            leftLane_8.setId("left_"+(getPieceCounterValue()));

                            rightLane_0.setId("right_"+(getPieceCounterValue()));
                            rightLane_1.setId("right_"+(getPieceCounterValue()));
                            rightLane_2.setId("right_"+(getPieceCounterValue()));
                            rightLane_3.setId("right_"+(getPieceCounterValue()));
                            rightLane_4.setId("right_"+(getPieceCounterValue()));
                            rightLane_5.setId("right_"+(getPieceCounterValue()));
                            rightLane_6.setId("right_"+(getPieceCounterValue()));
                            rightLane_7.setId("right_"+(getPieceCounterValue()));
                            rightLane_8.setId("right_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                            graph.addGraphNodeToList(rightLane_5);
                            graph.addGraphNodeToList(rightLane_6);
                            graph.addGraphNodeToList(rightLane_7);
                            graph.addGraphNodeToList(rightLane_8);
                        }

                        // if the currentPiece that was added is a straight road piece rotated to 90 degrees THEN
                        if (currentPieceSelected.contains("1_straightRoad_90.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+0, b.getMinY()-5);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode rightLane_5 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode rightLane_6 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode rightLane_7 = new GraphNode(b.getMinX()+0, b.getMinY()+5);

                            leftLane_0.setId("left_"+(getPieceCounterValue()));
                            leftLane_1.setId("left_"+(getPieceCounterValue()));
                            leftLane_2.setId("left_"+(getPieceCounterValue()));
                            leftLane_3.setId("left_"+(getPieceCounterValue()));
                            leftLane_4.setId("left_"+(getPieceCounterValue()));
                            leftLane_5.setId("left_"+(getPieceCounterValue()));
                            leftLane_6.setId("left_"+(getPieceCounterValue()));
                            leftLane_7.setId("left_"+(getPieceCounterValue()));

                            rightLane_0.setId("right_"+(getPieceCounterValue()));
                            rightLane_1.setId("right_"+(getPieceCounterValue()));
                            rightLane_2.setId("right_"+(getPieceCounterValue()));
                            rightLane_3.setId("right_"+(getPieceCounterValue()));
                            rightLane_4.setId("right_"+(getPieceCounterValue()));
                            rightLane_5.setId("right_"+(getPieceCounterValue()));
                            rightLane_6.setId("right_"+(getPieceCounterValue()));
                            rightLane_7.setId("right_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                            graph.addGraphNodeToList(rightLane_5);
                            graph.addGraphNodeToList(rightLane_6);
                            graph.addGraphNodeToList(rightLane_7);
                        }

                        // if the currentPiece that was added is a straight road piece rotated to 180 degrees THEN
                        // do this.. Also to point out that the left lanes and right lanes are flipped, from the image
                        // the rightmost lane is now the Left Lane, cars should travel from Top to Bottom
                        // the leftmost lane is now the right lane, cars should travel from Bottom to Top.
                        if (currentPieceSelected.contains("1_straightRoad_180.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+25, b.getMinY()-20);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+25, b.getMinY()+20);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode rightLane_5 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode rightLane_6 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode rightLane_7 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode rightLane_8 = new GraphNode(b.getMinX()+10, b.getMinY()-20);

                            leftLane_0.setId("left_"+(getPieceCounterValue()));
                            leftLane_1.setId("left_"+(getPieceCounterValue()));
                            leftLane_2.setId("left_"+(getPieceCounterValue()));
                            leftLane_3.setId("left_"+(getPieceCounterValue()));
                            leftLane_4.setId("left_"+(getPieceCounterValue()));
                            leftLane_5.setId("left_"+(getPieceCounterValue()));
                            leftLane_6.setId("left_"+(getPieceCounterValue()));
                            leftLane_7.setId("left_"+(getPieceCounterValue()));
                            leftLane_8.setId("left_"+(getPieceCounterValue()));

                            rightLane_0.setId("right_"+(getPieceCounterValue()));
                            rightLane_1.setId("right_"+(getPieceCounterValue()));
                            rightLane_2.setId("right_"+(getPieceCounterValue()));
                            rightLane_3.setId("right_"+(getPieceCounterValue()));
                            rightLane_4.setId("right_"+(getPieceCounterValue()));
                            rightLane_5.setId("right_"+(getPieceCounterValue()));
                            rightLane_6.setId("right_"+(getPieceCounterValue()));
                            rightLane_7.setId("right_"+(getPieceCounterValue()));
                            rightLane_8.setId("right_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                            graph.addGraphNodeToList(rightLane_5);
                            graph.addGraphNodeToList(rightLane_6);
                            graph.addGraphNodeToList(rightLane_7);
                            graph.addGraphNodeToList(rightLane_8);
                        }

                        // if the currentPiece that was added is a straight road piece rotated to 270 degrees THEN
                        // map the nodes
                        if (currentPieceSelected.contains("1_straightRoad_270.png")) { // +35 and +5
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+0, b.getMinY()+5);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+0, b.getMinY()-5);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode rightLane_5 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode rightLane_6 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode rightLane_7 = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            leftLane_0.setId("left_"+(getPieceCounterValue()));
                            leftLane_1.setId("left_"+(getPieceCounterValue()));
                            leftLane_2.setId("left_"+(getPieceCounterValue()));
                            leftLane_3.setId("left_"+(getPieceCounterValue()));
                            leftLane_4.setId("left_"+(getPieceCounterValue()));
                            leftLane_5.setId("left_"+(getPieceCounterValue()));
                            leftLane_6.setId("left_"+(getPieceCounterValue()));
                            leftLane_7.setId("left_"+(getPieceCounterValue()));

                            rightLane_0.setId("right_"+(getPieceCounterValue()));
                            rightLane_1.setId("right_"+(getPieceCounterValue()));
                            rightLane_2.setId("right_"+(getPieceCounterValue()));
                            rightLane_3.setId("right_"+(getPieceCounterValue()));
                            rightLane_4.setId("right_"+(getPieceCounterValue()));
                            rightLane_5.setId("right_"+(getPieceCounterValue()));
                            rightLane_6.setId("right_"+(getPieceCounterValue()));
                            rightLane_7.setId("right_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                            graph.addGraphNodeToList(rightLane_5);
                            graph.addGraphNodeToList(rightLane_6);
                            graph.addGraphNodeToList(rightLane_7);
                        }

                        // if the currentPiece that was added is a turn left piece with no rotation applied THEN
                        // map the nodes
                        if (currentPieceSelected.contains("2_turnLeft.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+0, b.getMinY()-5);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode leftLaneTurnRight = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode leftLane_9 = new GraphNode(b.getMinX()+25, b.getMinY()+20);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode rightLaneTurnLeft = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+0, b.getMinY()+5);

                            leftLaneTurnRight.setId("left_"+getPieceCounterValue());
                            rightLaneTurnLeft.setId("right_"+getPieceCounterValue());

                            leftLane_0.setId("left_"+(getPieceCounterValue()));
                            leftLane_1.setId("left_"+(getPieceCounterValue()));
                            leftLane_2.setId("left_"+(getPieceCounterValue()));
                            leftLane_3.setId("left_"+(getPieceCounterValue()));
                            leftLane_4.setId("left_"+(getPieceCounterValue()));
                            leftLane_5.setId("left_"+(getPieceCounterValue()));
                            leftLane_6.setId("left_"+(getPieceCounterValue()));
                            leftLane_7.setId("left_"+(getPieceCounterValue()));
                            leftLane_8.setId("left_"+(getPieceCounterValue()));
                            leftLane_9.setId("left_"+(getPieceCounterValue()));

                            rightLane_0.setId("right_"+(getPieceCounterValue()));
                            rightLane_1.setId("right_"+(getPieceCounterValue()));
                            rightLane_2.setId("right_"+(getPieceCounterValue()));
                            rightLane_3.setId("right_"+(getPieceCounterValue()));
                            rightLane_4.setId("right_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLaneTurnRight);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);
                            graph.addGraphNodeToList(leftLane_9);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLaneTurnLeft);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                        }

                        // if the currentPiece that was added is a turn left piece with no rotation applied but inversion is applied THEN
                        // map the nodes
                        if (currentPieceSelected.contains("2_turnLeft_inv.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+0, b.getMinY()-5);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode leftLaneTurnRight = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode leftLane_9 = new GraphNode(b.getMinX()+25, b.getMinY()+20);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode rightLaneTurnLeft = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+0, b.getMinY()+5);

                            leftLaneTurnRight.setId("right_"+getPieceCounterValue());
                            rightLaneTurnLeft.setId("left_"+getPieceCounterValue());

                            leftLane_0.setId("right_"+(getPieceCounterValue()));
                            leftLane_1.setId("right_"+(getPieceCounterValue()));
                            leftLane_2.setId("right_"+(getPieceCounterValue()));
                            leftLane_3.setId("right_"+(getPieceCounterValue()));
                            leftLane_4.setId("right_"+(getPieceCounterValue()));
                            leftLane_5.setId("right_"+(getPieceCounterValue()));
                            leftLane_6.setId("right_"+(getPieceCounterValue()));
                            leftLane_7.setId("right_"+(getPieceCounterValue()));
                            leftLane_8.setId("right_"+(getPieceCounterValue()));
                            leftLane_9.setId("right_"+(getPieceCounterValue()));

                            rightLane_0.setId("left_"+(getPieceCounterValue()));
                            rightLane_1.setId("left_"+(getPieceCounterValue()));
                            rightLane_2.setId("left_"+(getPieceCounterValue()));
                            rightLane_3.setId("left_"+(getPieceCounterValue()));
                            rightLane_4.setId("left_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLaneTurnRight);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);
                            graph.addGraphNodeToList(leftLane_9);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLaneTurnLeft);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                        }

                        // if the currentPiece that was added is a turn left piece with 90 degrees rotation applied THEN
                        // map the nodes
                        if (currentPieceSelected.contains("2_turnLeft_90.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+25, b.getMinY()-20);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode leftLaneTurnRight = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode leftLane_9 = new GraphNode(b.getMinX()+0, b.getMinY()+5);


                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+10, b.getMinY()-20);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode rightLaneTurnLeft = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+0, b.getMinY()-5);


                            leftLaneTurnRight.setId("left_"+getPieceCounterValue());
                            rightLaneTurnLeft.setId("right_"+getPieceCounterValue());

                            leftLane_0.setId("left_"+(getPieceCounterValue()));
                            leftLane_1.setId("left_"+(getPieceCounterValue()));
                            leftLane_2.setId("left_"+(getPieceCounterValue()));
                            leftLane_3.setId("left_"+(getPieceCounterValue()));
                            leftLane_4.setId("left_"+(getPieceCounterValue()));
                            leftLane_5.setId("left_"+(getPieceCounterValue()));
                            leftLane_6.setId("left_"+(getPieceCounterValue()));
                            leftLane_7.setId("left_"+(getPieceCounterValue()));
                            leftLane_8.setId("left_"+(getPieceCounterValue()));
                            leftLane_9.setId("left_"+(getPieceCounterValue()));

                            rightLane_0.setId("right_"+(getPieceCounterValue()));
                            rightLane_1.setId("right_"+(getPieceCounterValue()));
                            rightLane_2.setId("right_"+(getPieceCounterValue()));
                            rightLane_3.setId("right_"+(getPieceCounterValue()));
                            rightLane_4.setId("right_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLaneTurnRight);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);
                            graph.addGraphNodeToList(leftLane_9);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLaneTurnLeft);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                        }

                        // if the currentPiece that was added is a turn left piece with 90 degrees rotation applied and there's inversion THEN
                        // map the nodes
                        if (currentPieceSelected.contains("2_turnLeft_90_inv.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+25, b.getMinY()-20);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode leftLaneTurnRight = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode leftLane_9 = new GraphNode(b.getMinX()+0, b.getMinY()+5);


                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+10, b.getMinY()-20);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode rightLaneTurnLeft = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+0, b.getMinY()-5);


                            leftLaneTurnRight.setId("right_"+getPieceCounterValue());
                            rightLaneTurnLeft.setId("left_"+getPieceCounterValue());

                            leftLane_0.setId("right_"+(getPieceCounterValue()));
                            leftLane_1.setId("right_"+(getPieceCounterValue()));
                            leftLane_2.setId("right_"+(getPieceCounterValue()));
                            leftLane_3.setId("right_"+(getPieceCounterValue()));
                            leftLane_4.setId("right_"+(getPieceCounterValue()));
                            leftLane_5.setId("right_"+(getPieceCounterValue()));
                            leftLane_6.setId("right_"+(getPieceCounterValue()));
                            leftLane_7.setId("right_"+(getPieceCounterValue()));
                            leftLane_8.setId("right_"+(getPieceCounterValue()));
                            leftLane_9.setId("right_"+(getPieceCounterValue()));

                            rightLane_0.setId("left_"+(getPieceCounterValue()));
                            rightLane_1.setId("left_"+(getPieceCounterValue()));
                            rightLane_2.setId("left_"+(getPieceCounterValue()));
                            rightLane_3.setId("left_"+(getPieceCounterValue()));
                            rightLane_4.setId("left_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLaneTurnRight);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);
                            graph.addGraphNodeToList(leftLane_9);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLaneTurnLeft);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                        }

                        // if the currentPiece that was added is a turn left piece with 180 degrees rotation applied THEN
                        // map the nodes
                        if (currentPieceSelected.contains("2_turnLeft_180.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode leftLaneTurnRight = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode leftLane_9 = new GraphNode(b.getMinX()+10, b.getMinY()-20);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+25, b.getMinY()-20);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode rightLaneTurnLeft = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            leftLaneTurnRight.setId("left_"+getPieceCounterValue());
                            rightLaneTurnLeft.setId("right_"+getPieceCounterValue());

                            leftLane_0.setId("left_"+(getPieceCounterValue()));
                            leftLane_1.setId("left_"+(getPieceCounterValue()));
                            leftLane_2.setId("left_"+(getPieceCounterValue()));
                            leftLane_3.setId("left_"+(getPieceCounterValue()));
                            leftLane_4.setId("left_"+(getPieceCounterValue()));
                            leftLane_5.setId("left_"+(getPieceCounterValue()));
                            leftLane_6.setId("left_"+(getPieceCounterValue()));
                            leftLane_7.setId("left_"+(getPieceCounterValue()));
                            leftLane_8.setId("left_"+(getPieceCounterValue()));
                            leftLane_9.setId("left_"+(getPieceCounterValue()));

                            rightLane_0.setId("right_"+(getPieceCounterValue()));
                            rightLane_1.setId("right_"+(getPieceCounterValue()));
                            rightLane_2.setId("right_"+(getPieceCounterValue()));
                            rightLane_3.setId("right_"+(getPieceCounterValue()));
                            rightLane_4.setId("right_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLaneTurnRight);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);
                            graph.addGraphNodeToList(leftLane_9);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLaneTurnLeft);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                        }

                        // if the currentPiece that was added is a turn left piece with 180 degrees rotation applied and there's inversion THEN
                        // map the nodes
                        if (currentPieceSelected.contains("2_turnLeft_180_inv.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode leftLaneTurnRight = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode leftLane_9 = new GraphNode(b.getMinX()+10, b.getMinY()-20);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+25, b.getMinY()-20);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode rightLaneTurnLeft = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            leftLaneTurnRight.setId("right_"+getPieceCounterValue());
                            rightLaneTurnLeft.setId("left_"+getPieceCounterValue());

                            leftLane_0.setId("right_"+(getPieceCounterValue()));
                            leftLane_1.setId("right_"+(getPieceCounterValue()));
                            leftLane_2.setId("right_"+(getPieceCounterValue()));
                            leftLane_3.setId("right_"+(getPieceCounterValue()));
                            leftLane_4.setId("right_"+(getPieceCounterValue()));
                            leftLane_5.setId("right_"+(getPieceCounterValue()));
                            leftLane_6.setId("right_"+(getPieceCounterValue()));
                            leftLane_7.setId("right_"+(getPieceCounterValue()));
                            leftLane_8.setId("right_"+(getPieceCounterValue()));
                            leftLane_9.setId("right_"+(getPieceCounterValue()));

                            rightLane_0.setId("left_"+(getPieceCounterValue()));
                            rightLane_1.setId("left_"+(getPieceCounterValue()));
                            rightLane_2.setId("left_"+(getPieceCounterValue()));
                            rightLane_3.setId("left_"+(getPieceCounterValue()));
                            rightLane_4.setId("left_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLaneTurnRight);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);
                            graph.addGraphNodeToList(leftLane_9);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLaneTurnLeft);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                        }

                        // if the currentPiece that was added is a turn left piece with 270 degrees rotation applied THEN
                        // map the nodes
                        if (currentPieceSelected.contains("2_turnLeft_270.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode leftLaneTurnRight = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode leftLane_9 = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode rightLaneTurnLeft = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+25, b.getMinY()+20);

                            leftLaneTurnRight.setId("left_"+getPieceCounterValue());
                            rightLaneTurnLeft.setId("right_"+getPieceCounterValue());

                            leftLane_0.setId("left_"+(getPieceCounterValue()));
                            leftLane_1.setId("left_"+(getPieceCounterValue()));
                            leftLane_2.setId("left_"+(getPieceCounterValue()));
                            leftLane_3.setId("left_"+(getPieceCounterValue()));
                            leftLane_4.setId("left_"+(getPieceCounterValue()));
                            leftLane_5.setId("left_"+(getPieceCounterValue()));
                            leftLane_6.setId("left_"+(getPieceCounterValue()));
                            leftLane_7.setId("left_"+(getPieceCounterValue()));
                            leftLane_8.setId("left_"+(getPieceCounterValue()));
                            leftLane_9.setId("left_"+(getPieceCounterValue()));

                            rightLane_0.setId("right_"+(getPieceCounterValue()));
                            rightLane_1.setId("right_"+(getPieceCounterValue()));
                            rightLane_2.setId("right_"+(getPieceCounterValue()));
                            rightLane_3.setId("right_"+(getPieceCounterValue()));
                            rightLane_4.setId("right_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLaneTurnRight);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);
                            graph.addGraphNodeToList(leftLane_9);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLaneTurnLeft);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                        }

                        // if the currentPiece that was added is a turn left piece with 270 degrees rotation applied THEN
                        // map the nodes
                        if (currentPieceSelected.contains("2_turnLeft_270_inv.png")) {
                            GraphNode leftLane_0 = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode leftLane_1 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode leftLane_2 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode leftLane_3 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode leftLane_4 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode leftLaneTurnRight = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode leftLane_5 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode leftLane_6 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode leftLane_7 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode leftLane_8 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode leftLane_9 = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            GraphNode rightLane_0 = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode rightLane_1 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode rightLaneTurnLeft = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode rightLane_2 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode rightLane_3 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode rightLane_4 = new GraphNode(b.getMinX()+25, b.getMinY()+20);

                            leftLaneTurnRight.setId("right_"+getPieceCounterValue());
                            rightLaneTurnLeft.setId("left_"+getPieceCounterValue());

                            leftLane_0.setId("right_"+(getPieceCounterValue()));
                            leftLane_1.setId("right_"+(getPieceCounterValue()));
                            leftLane_2.setId("right_"+(getPieceCounterValue()));
                            leftLane_3.setId("right_"+(getPieceCounterValue()));
                            leftLane_4.setId("right_"+(getPieceCounterValue()));
                            leftLane_5.setId("right_"+(getPieceCounterValue()));
                            leftLane_6.setId("right_"+(getPieceCounterValue()));
                            leftLane_7.setId("right_"+(getPieceCounterValue()));
                            leftLane_8.setId("right_"+(getPieceCounterValue()));
                            leftLane_9.setId("right_"+(getPieceCounterValue()));

                            rightLane_0.setId("left_"+(getPieceCounterValue()));
                            rightLane_1.setId("left_"+(getPieceCounterValue()));
                            rightLane_2.setId("left_"+(getPieceCounterValue()));
                            rightLane_3.setId("left_"+(getPieceCounterValue()));
                            rightLane_4.setId("left_"+(getPieceCounterValue()));

                            incrementPieceCounter();

                            graph.addGraphNodeToList(leftLane_0);
                            graph.addGraphNodeToList(leftLane_1);
                            graph.addGraphNodeToList(leftLane_2);
                            graph.addGraphNodeToList(leftLane_3);
                            graph.addGraphNodeToList(leftLane_4);
                            graph.addGraphNodeToList(leftLaneTurnRight);
                            graph.addGraphNodeToList(leftLane_5);
                            graph.addGraphNodeToList(leftLane_6);
                            graph.addGraphNodeToList(leftLane_7);
                            graph.addGraphNodeToList(leftLane_8);
                            graph.addGraphNodeToList(leftLane_9);

                            graph.addGraphNodeToList(rightLane_0);
                            graph.addGraphNodeToList(rightLane_1);
                            graph.addGraphNodeToList(rightLaneTurnLeft);
                            graph.addGraphNodeToList(rightLane_2);
                            graph.addGraphNodeToList(rightLane_3);
                            graph.addGraphNodeToList(rightLane_4);
                        }

                        // if the currentPiece that was added is an intersection piece THEN
                        // map the nodes
                        if (currentPieceSelected.contains("4_intersection.png")) {
                            GraphNode int1_entrance = new GraphNode(b.getMinX()+0, b.getMinY()-5);
                            GraphNode int1_0 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode int1_1 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode int1_2 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode int1_3 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode int1_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int1_5 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode int1_exit = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            GraphNode int6_entrance = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode int6_0 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode int6_1 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int6_2 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode int6_3 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode int6_4 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode int6_5 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode int6_exit = new GraphNode(b.getMinX()+0, b.getMinY()+5);

                            GraphNode int3_entrance = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode int3_0 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode int3_1 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode int3_2 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode int3_3 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode int3_4 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode int3_5 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode int3_6 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode int3_exit = new GraphNode(b.getMinX()+10, b.getMinY()-20);

                            GraphNode int4_entrance = new GraphNode(b.getMinX()+25, b.getMinY()-20);
                            GraphNode int4_0 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode int4_1 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode int4_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int4_3 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode int4_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int4_5 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode int4_6 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode int4_exit = new GraphNode(b.getMinX()+25, b.getMinY()+20);

                            int1_entrance.setId("left_"+(getPieceCounterValue()));
                            int1_0.setId("left_"+(getPieceCounterValue()));
                            int1_1.setId("left_"+(getPieceCounterValue()));
                            int1_2.setId("left_"+(getPieceCounterValue()));
                            int1_3.setId("left_"+(getPieceCounterValue()));
                            int1_4.setId("left_"+(getPieceCounterValue()));
                            int1_5.setId("left_"+(getPieceCounterValue()));
                            int1_exit.setId("left_"+(getPieceCounterValue()));

                            int6_entrance.setId("right_"+(getPieceCounterValue()));
                            int6_0.setId("right_"+(getPieceCounterValue()));
                            int6_1.setId("right_"+(getPieceCounterValue()));
                            int6_2.setId("right_"+(getPieceCounterValue()));
                            int6_3.setId("right_"+(getPieceCounterValue()));
                            int6_4.setId("right_"+(getPieceCounterValue()));
                            int6_5.setId("right_"+(getPieceCounterValue()));
                            int6_exit.setId("right_"+(getPieceCounterValue()));

                            int3_entrance.setId("right_"+(getPieceCounterValue()));
                            int3_0.setId("right_"+(getPieceCounterValue()));
                            int3_1.setId("right_"+(getPieceCounterValue()));
                            int3_2.setId("right_"+(getPieceCounterValue()));
                            int3_3.setId("right_"+(getPieceCounterValue()));
                            int3_4.setId("right_"+(getPieceCounterValue()));
                            int3_5.setId("right_"+(getPieceCounterValue()));
                            int3_6.setId("right_"+(getPieceCounterValue()));
                            int3_exit.setId("right_"+(getPieceCounterValue()));

                            int4_entrance.setId("left_"+(getPieceCounterValue()));
                            int4_0.setId("left_"+(getPieceCounterValue()));
                            int4_1.setId("left_"+(getPieceCounterValue()));
                            int4_2.setId("left_"+(getPieceCounterValue()));
                            int4_3.setId("left_"+(getPieceCounterValue()));
                            int4_4.setId("left_"+(getPieceCounterValue()));
                            int4_5.setId("left_"+(getPieceCounterValue()));
                            int4_6.setId("left_"+(getPieceCounterValue()));
                            int4_exit.setId("left_"+(getPieceCounterValue()));

                            graph.addGraphNodeToList(int1_entrance);
                            graph.addGraphNodeToList(int1_0);
                            graph.addGraphNodeToList(int1_1);
                            graph.addGraphNodeToList(int1_2);
                            graph.addGraphNodeToList(int1_3);
                            graph.addGraphNodeToList(int1_4);
                            graph.addGraphNodeToList(int1_5);
                            graph.addGraphNodeToList(int1_exit);

                            graph.addGraphNodeToList(int6_entrance);
                            graph.addGraphNodeToList(int6_0);
                            graph.addGraphNodeToList(int6_1);
                            graph.addGraphNodeToList(int6_2);
                            graph.addGraphNodeToList(int6_3);
                            graph.addGraphNodeToList(int6_4);
                            graph.addGraphNodeToList(int6_5);
                            graph.addGraphNodeToList(int6_exit);

                            graph.addGraphNodeToList(int3_entrance);
                            graph.addGraphNodeToList(int3_0);
                            graph.addGraphNodeToList(int3_1);
                            graph.addGraphNodeToList(int3_2);
                            graph.addGraphNodeToList(int3_3);
                            graph.addGraphNodeToList(int3_4);
                            graph.addGraphNodeToList(int3_5);
                            graph.addGraphNodeToList(int3_6);
                            graph.addGraphNodeToList(int3_exit);

                            graph.addGraphNodeToList(int4_entrance);
                            graph.addGraphNodeToList(int4_0);
                            graph.addGraphNodeToList(int4_1);
                            graph.addGraphNodeToList(int4_2);
                            graph.addGraphNodeToList(int4_3);
                            graph.addGraphNodeToList(int4_4);
                            graph.addGraphNodeToList(int4_5);
                            graph.addGraphNodeToList(int4_6);
                            graph.addGraphNodeToList(int4_exit);

                            incrementPieceCounter();
                        }

                        // if the currentPiece that was added is an intersection piece with 90 degrees rotation THEN
                        // map the nodes
                        if (currentPieceSelected.contains("4_intersection_90.png")) {
                            GraphNode int1_entrance = new GraphNode(b.getMinX()+0, b.getMinY()-5);
                            GraphNode int1_0 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode int1_1 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode int1_2 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode int1_3 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode int1_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int1_5 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode int1_exit = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            GraphNode int6_entrance = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode int6_0 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode int6_1 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int6_2 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode int6_3 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode int6_4 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode int6_5 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode int6_exit = new GraphNode(b.getMinX()+0, b.getMinY()+5);

                            GraphNode int3_entrance = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode int3_0 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode int3_1 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode int3_2 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode int3_3 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode int3_4 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode int3_5 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode int3_6 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode int3_exit = new GraphNode(b.getMinX()+10, b.getMinY()-20);

                            GraphNode int4_entrance = new GraphNode(b.getMinX()+25, b.getMinY()-20);
                            GraphNode int4_0 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode int4_1 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode int4_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int4_3 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode int4_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int4_5 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode int4_6 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode int4_exit = new GraphNode(b.getMinX()+25, b.getMinY()+20);

                            int1_entrance.setId("left_"+(getPieceCounterValue()));
                            int1_0.setId("left_"+(getPieceCounterValue()));
                            int1_1.setId("left_"+(getPieceCounterValue()));
                            int1_2.setId("left_"+(getPieceCounterValue()));
                            int1_3.setId("left_"+(getPieceCounterValue()));
                            int1_4.setId("left_"+(getPieceCounterValue()));
                            int1_5.setId("left_"+(getPieceCounterValue()));
                            int1_exit.setId("left_"+(getPieceCounterValue()));

                            int6_entrance.setId("right_"+(getPieceCounterValue()));
                            int6_0.setId("right_"+(getPieceCounterValue()));
                            int6_1.setId("right_"+(getPieceCounterValue()));
                            int6_2.setId("right_"+(getPieceCounterValue()));
                            int6_3.setId("right_"+(getPieceCounterValue()));
                            int6_4.setId("right_"+(getPieceCounterValue()));
                            int6_5.setId("right_"+(getPieceCounterValue()));
                            int6_exit.setId("right_"+(getPieceCounterValue()));

                            int3_entrance.setId("left_"+(getPieceCounterValue()));
                            int3_0.setId("left_"+(getPieceCounterValue()));
                            int3_1.setId("left_"+(getPieceCounterValue()));
                            int3_2.setId("left_"+(getPieceCounterValue()));
                            int3_3.setId("left_"+(getPieceCounterValue()));
                            int3_4.setId("left_"+(getPieceCounterValue()));
                            int3_5.setId("left_"+(getPieceCounterValue()));
                            int3_6.setId("left_"+(getPieceCounterValue()));
                            int3_exit.setId("left_"+(getPieceCounterValue()));

                            int4_entrance.setId("right_"+(getPieceCounterValue()));
                            int4_0.setId("right_"+(getPieceCounterValue()));
                            int4_1.setId("right_"+(getPieceCounterValue()));
                            int4_2.setId("right_"+(getPieceCounterValue()));
                            int4_3.setId("right_"+(getPieceCounterValue()));
                            int4_4.setId("right_"+(getPieceCounterValue()));
                            int4_5.setId("right_"+(getPieceCounterValue()));
                            int4_6.setId("right_"+(getPieceCounterValue()));
                            int4_exit.setId("right_"+(getPieceCounterValue()));

                            graph.addGraphNodeToList(int1_entrance);
                            graph.addGraphNodeToList(int1_0);
                            graph.addGraphNodeToList(int1_1);
                            graph.addGraphNodeToList(int1_2);
                            graph.addGraphNodeToList(int1_3);
                            graph.addGraphNodeToList(int1_4);
                            graph.addGraphNodeToList(int1_5);
                            graph.addGraphNodeToList(int1_exit);

                            graph.addGraphNodeToList(int6_entrance);
                            graph.addGraphNodeToList(int6_0);
                            graph.addGraphNodeToList(int6_1);
                            graph.addGraphNodeToList(int6_2);
                            graph.addGraphNodeToList(int6_3);
                            graph.addGraphNodeToList(int6_4);
                            graph.addGraphNodeToList(int6_5);
                            graph.addGraphNodeToList(int6_exit);

                            graph.addGraphNodeToList(int3_entrance);
                            graph.addGraphNodeToList(int3_0);
                            graph.addGraphNodeToList(int3_1);
                            graph.addGraphNodeToList(int3_2);
                            graph.addGraphNodeToList(int3_3);
                            graph.addGraphNodeToList(int3_4);
                            graph.addGraphNodeToList(int3_5);
                            graph.addGraphNodeToList(int3_6);
                            graph.addGraphNodeToList(int3_exit);

                            graph.addGraphNodeToList(int4_entrance);
                            graph.addGraphNodeToList(int4_0);
                            graph.addGraphNodeToList(int4_1);
                            graph.addGraphNodeToList(int4_2);
                            graph.addGraphNodeToList(int4_3);
                            graph.addGraphNodeToList(int4_4);
                            graph.addGraphNodeToList(int4_5);
                            graph.addGraphNodeToList(int4_6);
                            graph.addGraphNodeToList(int4_exit);

                            incrementPieceCounter();
                        }

                        // if the currentPiece that was added is an intersection piece with 180 degrees rotation THEN
                        // map the nodes
                        if (currentPieceSelected.contains("4_intersection_180.png")) {
                            GraphNode int1_entrance = new GraphNode(b.getMinX()+0, b.getMinY()-5);
                            GraphNode int1_0 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode int1_1 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode int1_2 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode int1_3 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode int1_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int1_5 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode int1_exit = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            GraphNode int6_entrance = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode int6_0 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode int6_1 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int6_2 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode int6_3 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode int6_4 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode int6_5 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode int6_exit = new GraphNode(b.getMinX()+0, b.getMinY()+5);

                            GraphNode int3_entrance = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode int3_0 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode int3_1 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode int3_2 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode int3_3 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode int3_4 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode int3_5 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode int3_6 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode int3_exit = new GraphNode(b.getMinX()+10, b.getMinY()-20);

                            GraphNode int4_entrance = new GraphNode(b.getMinX()+25, b.getMinY()-20);
                            GraphNode int4_0 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode int4_1 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode int4_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int4_3 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode int4_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int4_5 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode int4_6 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode int4_exit = new GraphNode(b.getMinX()+25, b.getMinY()+20);

                            int1_entrance.setId("right_"+(getPieceCounterValue()));
                            int1_0.setId("right_"+(getPieceCounterValue()));
                            int1_1.setId("right_"+(getPieceCounterValue()));
                            int1_2.setId("right_"+(getPieceCounterValue()));
                            int1_3.setId("right_"+(getPieceCounterValue()));
                            int1_4.setId("right_"+(getPieceCounterValue()));
                            int1_5.setId("right_"+(getPieceCounterValue()));
                            int1_exit.setId("right_"+(getPieceCounterValue()));

                            int6_entrance.setId("left_"+(getPieceCounterValue()));
                            int6_0.setId("left_"+(getPieceCounterValue()));
                            int6_1.setId("left_"+(getPieceCounterValue()));
                            int6_2.setId("left_"+(getPieceCounterValue()));
                            int6_3.setId("left_"+(getPieceCounterValue()));
                            int6_4.setId("left_"+(getPieceCounterValue()));
                            int6_5.setId("left_"+(getPieceCounterValue()));
                            int6_exit.setId("left_"+(getPieceCounterValue()));

                            int3_entrance.setId("right_"+(getPieceCounterValue()));
                            int3_0.setId("right_"+(getPieceCounterValue()));
                            int3_1.setId("right_"+(getPieceCounterValue()));
                            int3_2.setId("right_"+(getPieceCounterValue()));
                            int3_3.setId("right_"+(getPieceCounterValue()));
                            int3_4.setId("right_"+(getPieceCounterValue()));
                            int3_5.setId("right_"+(getPieceCounterValue()));
                            int3_6.setId("right_"+(getPieceCounterValue()));
                            int3_exit.setId("right_"+(getPieceCounterValue()));

                            int4_entrance.setId("left_"+(getPieceCounterValue()));
                            int4_0.setId("left_"+(getPieceCounterValue()));
                            int4_1.setId("left_"+(getPieceCounterValue()));
                            int4_2.setId("left_"+(getPieceCounterValue()));
                            int4_3.setId("left_"+(getPieceCounterValue()));
                            int4_4.setId("left_"+(getPieceCounterValue()));
                            int4_5.setId("left_"+(getPieceCounterValue()));
                            int4_6.setId("left_"+(getPieceCounterValue()));
                            int4_exit.setId("left_"+(getPieceCounterValue()));

                            graph.addGraphNodeToList(int1_entrance);
                            graph.addGraphNodeToList(int1_0);
                            graph.addGraphNodeToList(int1_1);
                            graph.addGraphNodeToList(int1_2);
                            graph.addGraphNodeToList(int1_3);
                            graph.addGraphNodeToList(int1_4);
                            graph.addGraphNodeToList(int1_5);
                            graph.addGraphNodeToList(int1_exit);

                            graph.addGraphNodeToList(int6_entrance);
                            graph.addGraphNodeToList(int6_0);
                            graph.addGraphNodeToList(int6_1);
                            graph.addGraphNodeToList(int6_2);
                            graph.addGraphNodeToList(int6_3);
                            graph.addGraphNodeToList(int6_4);
                            graph.addGraphNodeToList(int6_5);
                            graph.addGraphNodeToList(int6_exit);

                            graph.addGraphNodeToList(int3_entrance);
                            graph.addGraphNodeToList(int3_0);
                            graph.addGraphNodeToList(int3_1);
                            graph.addGraphNodeToList(int3_2);
                            graph.addGraphNodeToList(int3_3);
                            graph.addGraphNodeToList(int3_4);
                            graph.addGraphNodeToList(int3_5);
                            graph.addGraphNodeToList(int3_6);
                            graph.addGraphNodeToList(int3_exit);

                            graph.addGraphNodeToList(int4_entrance);
                            graph.addGraphNodeToList(int4_0);
                            graph.addGraphNodeToList(int4_1);
                            graph.addGraphNodeToList(int4_2);
                            graph.addGraphNodeToList(int4_3);
                            graph.addGraphNodeToList(int4_4);
                            graph.addGraphNodeToList(int4_5);
                            graph.addGraphNodeToList(int4_6);
                            graph.addGraphNodeToList(int4_exit);

                            incrementPieceCounter();
                        }

                        // if the currentPiece that was added is an intersection piece with 270 degrees rotation THEN
                        // map the nodes
                        if (currentPieceSelected.contains("4_intersection_270.png")) {
                            GraphNode int1_entrance = new GraphNode(b.getMinX()+0, b.getMinY()-5);
                            GraphNode int1_0 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode int1_1 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode int1_2 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode int1_3 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode int1_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int1_5 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode int1_exit = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            GraphNode int6_entrance = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode int6_0 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode int6_1 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int6_2 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode int6_3 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode int6_4 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode int6_5 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode int6_exit = new GraphNode(b.getMinX()+0, b.getMinY()+5);

                            GraphNode int3_entrance = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode int3_0 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode int3_1 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode int3_2 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode int3_3 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode int3_4 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode int3_5 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode int3_6 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode int3_exit = new GraphNode(b.getMinX()+10, b.getMinY()-20);

                            GraphNode int4_entrance = new GraphNode(b.getMinX()+25, b.getMinY()+20);
                            GraphNode int4_0 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode int4_1 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode int4_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int4_3 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode int4_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int4_5 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode int4_6 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode int4_exit = new GraphNode(b.getMinX()+25, b.getMinY()-20);

                            int1_entrance.setId("right_"+(getPieceCounterValue()));
                            int1_0.setId("right_"+(getPieceCounterValue()));
                            int1_1.setId("right_"+(getPieceCounterValue()));
                            int1_2.setId("right_"+(getPieceCounterValue()));
                            int1_3.setId("right_"+(getPieceCounterValue()));
                            int1_4.setId("right_"+(getPieceCounterValue()));
                            int1_5.setId("right_"+(getPieceCounterValue()));
                            int1_exit.setId("right_"+(getPieceCounterValue()));

                            int6_entrance.setId("left_"+(getPieceCounterValue()));
                            int6_0.setId("left_"+(getPieceCounterValue()));
                            int6_1.setId("left_"+(getPieceCounterValue()));
                            int6_2.setId("left_"+(getPieceCounterValue()));
                            int6_3.setId("left_"+(getPieceCounterValue()));
                            int6_4.setId("left_"+(getPieceCounterValue()));
                            int6_5.setId("left_"+(getPieceCounterValue()));
                            int6_exit.setId("left_"+(getPieceCounterValue()));

                            int3_entrance.setId("left_"+(getPieceCounterValue()));
                            int3_0.setId("left_"+(getPieceCounterValue()));
                            int3_1.setId("left_"+(getPieceCounterValue()));
                            int3_2.setId("left_"+(getPieceCounterValue()));
                            int3_3.setId("left_"+(getPieceCounterValue()));
                            int3_4.setId("left_"+(getPieceCounterValue()));
                            int3_5.setId("left_"+(getPieceCounterValue()));
                            int3_6.setId("left_"+(getPieceCounterValue()));
                            int3_exit.setId("left_"+(getPieceCounterValue()));

                            int4_entrance.setId("right_"+(getPieceCounterValue()));
                            int4_0.setId("right_"+(getPieceCounterValue()));
                            int4_1.setId("right_"+(getPieceCounterValue()));
                            int4_2.setId("right_"+(getPieceCounterValue()));
                            int4_3.setId("right_"+(getPieceCounterValue()));
                            int4_4.setId("right_"+(getPieceCounterValue()));
                            int4_5.setId("right_"+(getPieceCounterValue()));
                            int4_6.setId("right_"+(getPieceCounterValue()));
                            int4_exit.setId("right_"+(getPieceCounterValue()));

                            graph.addGraphNodeToList(int1_entrance);
                            graph.addGraphNodeToList(int1_0);
                            graph.addGraphNodeToList(int1_1);
                            graph.addGraphNodeToList(int1_2);
                            graph.addGraphNodeToList(int1_3);
                            graph.addGraphNodeToList(int1_4);
                            graph.addGraphNodeToList(int1_5);
                            graph.addGraphNodeToList(int1_exit);

                            graph.addGraphNodeToList(int6_entrance);
                            graph.addGraphNodeToList(int6_0);
                            graph.addGraphNodeToList(int6_1);
                            graph.addGraphNodeToList(int6_2);
                            graph.addGraphNodeToList(int6_3);
                            graph.addGraphNodeToList(int6_4);
                            graph.addGraphNodeToList(int6_5);
                            graph.addGraphNodeToList(int6_exit);

                            graph.addGraphNodeToList(int3_entrance);
                            graph.addGraphNodeToList(int3_0);
                            graph.addGraphNodeToList(int3_1);
                            graph.addGraphNodeToList(int3_2);
                            graph.addGraphNodeToList(int3_3);
                            graph.addGraphNodeToList(int3_4);
                            graph.addGraphNodeToList(int3_5);
                            graph.addGraphNodeToList(int3_6);
                            graph.addGraphNodeToList(int3_exit);

                            graph.addGraphNodeToList(int4_entrance);
                            graph.addGraphNodeToList(int4_0);
                            graph.addGraphNodeToList(int4_1);
                            graph.addGraphNodeToList(int4_2);
                            graph.addGraphNodeToList(int4_3);
                            graph.addGraphNodeToList(int4_4);
                            graph.addGraphNodeToList(int4_5);
                            graph.addGraphNodeToList(int4_6);
                            graph.addGraphNodeToList(int4_exit);

                            incrementPieceCounter();
                        }

                        // if the currentPiece that was added is a general destination piece THEN
                        // map the node

                        if (currentPieceSelected.contains("dest_")) {
                            GraphNode dest1_entrance = new GraphNode(b.getMinX()+0, b.getMinY()-5);
                            GraphNode dest1_0 = new GraphNode(b.getMinX()+5, b.getMinY()-5);
                            GraphNode dest1_1 = new GraphNode(b.getMinX()+10, b.getMinY()-5);
                            GraphNode dest1_2 = new GraphNode(b.getMinX()+15, b.getMinY()-5);
                            GraphNode dest1_3 = new GraphNode(b.getMinX()+20, b.getMinY()-5);
                            GraphNode dest1_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode dest1_5 = new GraphNode(b.getMinX()+30, b.getMinY()-5);
                            GraphNode dest1_exit = new GraphNode(b.getMinX()+35, b.getMinY()-5);

                            GraphNode dest6_entrance = new GraphNode(b.getMinX()+35, b.getMinY()+5);
                            GraphNode dest6_0 = new GraphNode(b.getMinX()+30, b.getMinY()+5);
                            GraphNode dest6_1 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode dest6_2 = new GraphNode(b.getMinX()+20, b.getMinY()+5);
                            GraphNode dest6_3 = new GraphNode(b.getMinX()+15, b.getMinY()+5);
                            GraphNode dest6_4 = new GraphNode(b.getMinX()+10, b.getMinY()+5);
                            GraphNode dest6_5 = new GraphNode(b.getMinX()+5, b.getMinY()+5);
                            GraphNode dest6_exit = new GraphNode(b.getMinX()+0, b.getMinY()+5);

                            GraphNode dest3_entrance = new GraphNode(b.getMinX()+10, b.getMinY()+20);
                            GraphNode dest3_0 = new GraphNode(b.getMinX()+10, b.getMinY()+15);
                            GraphNode dest3_1 = new GraphNode(b.getMinX()+10, b.getMinY()+10);
                            GraphNode dest3_3 = new GraphNode(b.getMinX()+10, b.getMinY()+0);
                            GraphNode dest3_5 = new GraphNode(b.getMinX()+10, b.getMinY()-10);
                            GraphNode dest3_6 = new GraphNode(b.getMinX()+10, b.getMinY()-15);
                            GraphNode dest3_exit = new GraphNode(b.getMinX()+10, b.getMinY()-20);

                            GraphNode dest4_entrance = new GraphNode(b.getMinX()+25, b.getMinY()+20);
                            GraphNode dest4_0 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode dest4_1 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode dest4_3 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode dest4_5 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode dest4_6 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode dest4_exit = new GraphNode(b.getMinX()+25, b.getMinY()-20);

                            dest1_entrance.setId("d0_"+(getPieceCounterValue()));
                            dest1_0.setId("d1_"+(getPieceCounterValue()));
                            dest1_1.setId("d2"+(getPieceCounterValue()));
                            dest1_2.setId("d3_"+(getPieceCounterValue()));
                            dest1_3.setId("d4_"+(getPieceCounterValue()));
                            dest1_4.setId("d5"+(getPieceCounterValue()));
                            dest1_5.setId("d6_"+(getPieceCounterValue()));
                            dest1_exit.setId("d7_"+(getPieceCounterValue()));

                            dest6_entrance.setId("d8_"+(getPieceCounterValue()));
                            dest6_0.setId("d9_"+(getPieceCounterValue()));
                            dest6_1.setId("destinationGoal_"+(getPieceCounterValue()));
                            dest6_2.setId("d10_"+(getPieceCounterValue()));
                            dest6_3.setId("d11_"+(getPieceCounterValue()));
                            dest6_4.setId("d12_"+(getPieceCounterValue()));
                            dest6_5.setId("d13_"+(getPieceCounterValue()));
                            dest6_exit.setId("d14_"+(getPieceCounterValue()));

                            dest3_entrance.setId("d15_"+(getPieceCounterValue()));
                            dest3_0.setId("d16_"+(getPieceCounterValue()));
                            dest3_1.setId("d17_"+(getPieceCounterValue()));
                            dest3_3.setId("d18_"+(getPieceCounterValue()));
                            dest3_5.setId("d19_"+(getPieceCounterValue()));
                            dest3_6.setId("d20_"+(getPieceCounterValue()));
                            dest3_exit.setId("d21_"+(getPieceCounterValue()));

                            dest4_entrance.setId("d22_"+(getPieceCounterValue()));
                            dest4_0.setId("d23_"+(getPieceCounterValue()));
                            dest4_1.setId("d24_"+(getPieceCounterValue()));
                            dest4_3.setId("d25_"+(getPieceCounterValue()));
                            dest4_5.setId("d26_"+(getPieceCounterValue()));
                            dest4_6.setId("d27_"+(getPieceCounterValue()));
                            dest4_exit.setId("d28_"+(getPieceCounterValue()));

                            graph.addGraphNodeToList(dest1_entrance);
                            graph.addGraphNodeToList(dest1_0);
                            graph.addGraphNodeToList(dest1_1);
                            graph.addGraphNodeToList(dest1_2);
                            graph.addGraphNodeToList(dest1_3);
                            graph.addGraphNodeToList(dest1_4);
                            graph.addGraphNodeToList(dest1_5);
                            graph.addGraphNodeToList(dest1_exit);

                            graph.addGraphNodeToList(dest6_entrance);
                            graph.addGraphNodeToList(dest6_0);
                            graph.addGraphNodeToList(dest6_1);
                            graph.addGraphNodeToList(dest6_2);
                            graph.addGraphNodeToList(dest6_3);
                            graph.addGraphNodeToList(dest6_4);
                            graph.addGraphNodeToList(dest6_5);
                            graph.addGraphNodeToList(dest6_exit);

                            graph.addGraphNodeToList(dest3_entrance);
                            graph.addGraphNodeToList(dest3_0);
                            graph.addGraphNodeToList(dest3_1);
                            graph.addGraphNodeToList(dest3_3);
                            graph.addGraphNodeToList(dest3_5);
                            graph.addGraphNodeToList(dest3_6);
                            graph.addGraphNodeToList(dest3_exit);

                            graph.addGraphNodeToList(dest4_entrance);
                            graph.addGraphNodeToList(dest4_0);
                            graph.addGraphNodeToList(dest4_1);
                            graph.addGraphNodeToList(dest4_3);
                            graph.addGraphNodeToList(dest4_5);
                            graph.addGraphNodeToList(dest4_6);
                            graph.addGraphNodeToList(dest4_exit);

                            incrementPieceCounter();
                        }

                        /*
                        // if the currentPiece that was added is a destination piece specifically BUS STATION THEN
                        // map the node
                        if (currentPieceSelected.contains("bus_station.png")) {
                            GraphNode dest = new GraphNode(b.getMinX()+17, b.getMinY()-2);

                            dest.setId("destination_bus_station"+getPieceCounterValue());
                            incrementPieceCounter();

                            graph.addGraphNodeToList(dest);
                        }

                        // if the currentPiece that was added is a destination piece specifically a BUS STOP THEN
                        // map the node
                        if (currentPieceSelected.contains("bus_stop.png")) {
                            GraphNode dest = new GraphNode(b.getMinX()+17, b.getMinY()-2);

                            dest.setId("destination_bus_stop"+getPieceCounterValue());
                            incrementPieceCounter();

                            graph.addGraphNodeToList(dest);
                        }
                         */
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No piece selected");
                    alert.setContentText("Please select a piece to add into the scenario.");
                    alert.show();
                }
            }
        });

        /**
         * This event handler handles the placement of free-form nodes, that are Hazard pieces and Decorative pieces.
         * It checks if the pieces are of decorative OR hazard type, if so, retrieve the mouse event's X and Y coordinates,
         * set the attributes of the image to ensure the image is displayed appropriately, then apply a translation to
         * the piece before finally adding it to the main EditorPane (GridPane 7 x 24).
         *
         * if no piece selected, then display error.
         *
         * Specific pieces such as hazards or traffic lights may need specific adjustments in terms of their image/sprite.
         */
        tseep.add_MiscPiecesHandler(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (pieceType.equals("decorative") || pieceType.equals("hazard")) {
                    try {
                        Double xCoordinate = event.getX();
                        Double yCoordinate = event.getY();
                        ImageView piece = new ImageView(new Image(currentPieceSelected));
                        piece.setFitHeight(35);
                        piece.setFitWidth(35);
                        piece.setPreserveRatio(true);
                        if (currentPieceSelected.contains("cone") || currentPieceSelected.contains("barrier")) { // special case for hazard images
                            piece.setFitHeight(15);
                            piece.setFitWidth(15);
                            piece.setPreserveRatio(true);
                        }
                        if (currentPieceSelected.contains("trafficLight")) {
                            piece.setFitHeight(20);
                            piece.setFitWidth(20);
                            piece.setPreserveRatio(true);
                        }
                        piece.setTranslateX(xCoordinate-10);
                        piece.setTranslateY(yCoordinate-21);
                        tseep.getChildren().add(piece);
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("No piece selected");
                        alert.setContentText("Please select a piece to add into the scenario.");
                        alert.show();
                    }
                }
            }
        });
    }

    /**
     * This event handler handles the events set by the previous scene's controller; the TrafficSimulatorController.
     * The events are key presses. handleKeyPress() handles rotation, so if the R key is pressed on the keyboard.
     * It will play the rotation sound (or won't, if sounds are muted), modifies the displayed rotation degrees label in the BottomPane
     * and switches the currentPieceSelected URI (that is the selected piece) to its rotated variant based on the degrees.
     *
     * This is further reflected by re-setting the icon in the BottomPane, that displays to the user what the rotated piece looks like.
     * @throws UnsupportedAudioFileException If the audio file is not supported, throw this exception.
     * @throws IOException If the audio file is missing, throw this exception.
     */
    public void handleKeyPress() throws UnsupportedAudioFileException, IOException { // rotation handling
        if (!(pieceType.equals("destination"))) {
            if (!(currentPieceSelected.contains("grass.png"))) { // disable rotation for grass piece, because that's for deleting other road pieces.
                File file = new File("sfx\\rotate.wav");
                AudioInputStream sfx = AudioSystem.getAudioInputStream(file);
                if (!getMuteSounds()) {
                    try {
                        Clip clip = AudioSystem.getClip();
                        clip.open(sfx);
                        clip.start();
                        //clip.close();
                    } catch (Exception e) {}
                }

                if (currentPieceSelected.contains("inv")) { // remove any inversion of lanes
                    currentPieceSelected = currentPieceSelected.replace("_inv.png", ".png");
                }

                CurrentRotationDegrees += 90;
                if (CurrentRotationDegrees != 360) {
                    tse_bottompane.setCurrentRotationValue(CurrentRotationDegrees); // sets the text label of rotation
                    if (CurrentRotationDegrees == 90) {
                        //  example:
                        //  prev: img\2_EditorScreen\grass.png
                        //  to: img\2_EditorScreen\grass_90.png
                        currentPieceSelected = currentPieceSelected.replace(".png", "_" + 90 + ".png");
                        tse_bottompane.setRotationImage(currentPieceSelected);
                        // System.out.println(currentPieceSelected);
                    } else {
                        currentPieceSelected = currentPieceSelected.replace("_" + (CurrentRotationDegrees - 90) + ".png", "_" + CurrentRotationDegrees + ".png");
                        tse_bottompane.setRotationImage(currentPieceSelected);

    //                if (currentPieceSelected.contains("3_turnRight_90.png")) {
    //                    currentPieceSelected = currentPieceSelected.replace("3_turnRight_90", "2_turnLeft.png");
    //                }
                        // System.out.println(currentPieceSelected);
                    }
                } else {
                    currentPieceSelected = currentPieceSelected.replace("_" + (CurrentRotationDegrees-90) + ".png", ".png");
                    CurrentRotationDegrees = 0;
                    tse_bottompane.setCurrentRotationValue(CurrentRotationDegrees); // sets the text label of rotation
                    tse_bottompane.setRotationImage(currentPieceSelected);
                }
            }
        }
    }

    /**
     * This event handler attaches to the key press event set by the previous scene's controller: TrafficSimulatorController.
     * If the V key is pressed on the keyboard, it will flip the lanes of the turn pieces. (NB: this feature only works on the TURN LEFT piece)
     * It modifies the currentPieceSelected string, which holds the selected piece image URI so that it switches and points to the inverted version of the image/sprite.
     *
     * And it is able to switch back and forth between the standard variant of the piece and the inverted variant of the piece.
     * @throws UnsupportedAudioFileException If audio file is not supported, throw this exception.
     * @throws IOException If audio file is missing, throw this exception.
     */
    public void handleKeyPressInvertLanes () throws UnsupportedAudioFileException, IOException {
        if (!(currentPieceSelected.contains("grass.png"))) {
            if (currentPieceSelected.contains("turnLeft")) {
                // play sound effect for notification
                File file = new File("sfx\\invert.wav");
                AudioInputStream sfx = AudioSystem.getAudioInputStream(file);
                if (!getMuteSounds()) {
                    try {
                        Clip clip = AudioSystem.getClip();
                        clip.open(sfx);
                        clip.start();
                        //clip.close();
                    } catch (Exception e) {}
                }

                // modify piece name
                if (currentPieceSelected.contains("2_turnLeft.png")) {
                    currentPieceSelected = currentPieceSelected.replace("2_turnLeft.png", "2_turnLeft_inv.png");
                    tse_bottompane.setRotationImage(new Image(currentPieceSelected));
                } else if (currentPieceSelected.contains("2_turnLeft_inv.png")) {
                    currentPieceSelected = currentPieceSelected.replace("2_turnLeft_inv.png", "2_turnLeft.png");
                    tse_bottompane.setRotationImage(new Image(currentPieceSelected));
                } else if (currentPieceSelected.contains("2_turnLeft_90.png")) {
                    currentPieceSelected = currentPieceSelected.replace("2_turnLeft_90.png", "2_turnLeft_90_inv.png");
                    tse_bottompane.setRotationImage(new Image(currentPieceSelected));
                } else if (currentPieceSelected.contains("2_turnLeft_90_inv.png")) {
                    currentPieceSelected = currentPieceSelected.replace("2_turnLeft_90_inv.png", "2_turnLeft_90.png");
                    tse_bottompane.setRotationImage(new Image(currentPieceSelected));
                } else if (currentPieceSelected.contains("2_turnLeft_180.png")) {
                    currentPieceSelected = currentPieceSelected.replace("2_turnLeft_180.png", "2_turnLeft_180_inv.png");
                    tse_bottompane.setRotationImage(new Image(currentPieceSelected));
                } else if (currentPieceSelected.contains("2_turnLeft_180_inv.png")) {
                    currentPieceSelected = currentPieceSelected.replace("2_turnLeft_180_inv.png", "2_turnLeft_180.png");
                    tse_bottompane.setRotationImage(new Image(currentPieceSelected));
                } else if (currentPieceSelected.contains("2_turnLeft_270.png")) {
                    currentPieceSelected = currentPieceSelected.replace("2_turnLeft_270.png", "2_turnLeft_270_inv.png");
                    tse_bottompane.setRotationImage(new Image(currentPieceSelected));
                } else if (currentPieceSelected.contains("2_turnLeft_270_inv.png")) {
                    currentPieceSelected = currentPieceSelected.replace("2_turnLeft_270_inv.png", "2_turnLeft_270.png");
                    tse_bottompane.setRotationImage(new Image(currentPieceSelected));
                }
            }
        }
    }

    /**
     * This event handler attaches to the key press event set by the previous scene's controller: TrafficSimulatorController.
     * If the "F" key is pressed, this will deselect the piece, by setting currentPieceSelected variable to null and displaying this to the user
     * appropriately, by disabling any effects upon the slots of the PieceSelection that indicate that a piece was selected via method resetSlotSelection();
     */
    public void handleKeyPressDeselect () {
        resetSlotSelection();
        currentPieceSelected = null;
        tse_bottompane.setRotationImage(new WritableImage(45, 45));
    }


    /**
     * This method accepts a path and writes the whole scenario to a .txt file, it essentially saves the scenario.
     * Firstly, 2 arrays are made, the first array stores the ImageViewButtons (Road and destination pieces), the second array (which is an arraylist) stores
     * the free-form placed nodes - these are hazard and destination pieces. It then writes out the contents of each to the text file.
     *
     * If the path supplied was null, then by default the scenario is written to scenario.txt in the root directory.
     * @param path A path of type File, where the .txt file should be written to.
     * @throws IOException if Path doesn't exist, throw this exception.
     */
    private void writeScenarioDataToFile(File path) throws IOException {
        Node[][] gridPaneArray = new Node[7][24]; // 7 rows, 24 columns - holding ImageViewButton nodes
        ArrayList<ImageView> miscPieces = new ArrayList<ImageView>();
        GridPane editorPane = tseep.getCurrentEditorPane();
        for (Node node: editorPane.getChildren()) {
            if (node instanceof ImageViewButton) { // if the node CANNOT be casted to type ImageView
                gridPaneArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = node; // then it must be casted to ImageViewButton type
            } else {
                miscPieces.add((ImageView) node);
            }
        }
        if (path.getPath().equals("")) {
            FileWriter fileWriter = new FileWriter("scenario.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for(int col = 0; 24 > col; col++){
                for(int row = 0; 7 > row; row++) {
                    ImageViewButton node = (ImageViewButton) gridPaneArray[row][col];
                    printWriter.println(row + ", " + col + ", " + node.getRotate() + ", " + node.getImage().getUrl());
                }
            }
            for (ImageView image : miscPieces) {
                printWriter.println(image.getTranslateX() + ", " + image.getTranslateY() + ", " + image.getImage().getUrl());
            }
            printWriter.close();
            fileWriter.close();
        } else {
            FileWriter fileWriter = new FileWriter(path);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int col = 0; 24 > col; col++) {
                for (int row = 0; 7 > row; row++) {
                    ImageViewButton node = (ImageViewButton) gridPaneArray[row][col];
                    printWriter.println(row + ", " + col + ", " + node.getRotate() + ", " + node.getImage().getUrl());
                }
            }
            for (ImageView image : miscPieces) {
                printWriter.println(image.getTranslateX() + ", " + image.getTranslateY() + ", " + image.getImage().getUrl());
            }
            printWriter.close();
            fileWriter.close();
        }
    }

    /**
     * This method writes out the GraphNodes to a text file called 'graphnodes.txt', each graph node's ID, X and Y coordinate.
     * @throws IOException if it is not possible to write to graphnodes.txt in the root directory then throw this exception.
     */
    private void writeGraphNodesToFile() throws IOException {
        FileWriter fileWriter = new FileWriter("graphnodes.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (GraphNode node : graph.getRouteList()) {
            printWriter.println(node.getId() + ", " + node.getXCoordinate() + ", " + node.getYCoordinate());
        }
        printWriter.close();
        fileWriter.close();
    }

    /**
     * This method writes the entire AStarGraph object instance to a .dat file using the ObjectOutputStream.
     * Useful for saving scenarios.
     * @param scenarioPath File path where the .dat file should be written and saved.
     * @throws IOException If path cannot be accessed, then throw this exception.
     */
    private void writeGraphObjectToFile(File scenarioPath) throws IOException {
        scenarioPath = new File(scenarioPath.getAbsolutePath().replace(".txt", ".dat"));
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(scenarioPath));
        oos.writeObject(graph);
        oos.flush();
        oos.close();
    }

    /**
     * This methods reads back in the .dat file from the ObjectOutputStream, this time an ObjectInputStream is used,
     * it accepts the .dat file and re-assigns the current instance of AStarGraph to the object instance and its values from the .dat file.
     * Useful for loading saved scenarios.
     * @param scenarioPath The filepath where the .dat file can be found.
     * @throws IOException If the path cannot be accessed, throw this exception.
     * @throws ClassNotFoundException If the AStarGraph class is not available/found, throw this exception
     */
    private void readGraphObjectFromFile(File scenarioPath) throws IOException, ClassNotFoundException {
        scenarioPath = new File(scenarioPath.getAbsolutePath().replace(".txt", ".dat"));
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(scenarioPath));
        graph = (AStarGraph) ois.readObject();
        ois.close();
    }

    /**
     * This method checks if the scenario is empty, since the EditorPane (GridPane of 7 x 24) contains 168 ImageViewButton instances in each of the 168 cells
     * It checks if all 168 ImageViewButton instances contain an image with URI contains grass.png, by simply adding all ImageViewButtons to an arraylist
     * of ImageViewButtons and checking if the size equals 168, if so - the scenario is empty thus change isScenarioEmpty variable to true.
     */
    private void checkIfScenarioIsEmpty() {
        ArrayList<ImageViewButton> totalGrassPieces = new ArrayList<>();
        for (Node node: tseep.getChildren()) {
            if (node instanceof ImageViewButton) { // if the node CANNOT be casted to type ImageView
                ImageViewButton n = (ImageViewButton) node;
                if (n.getImage().getUrl().contains("grass.png")) {
                    totalGrassPieces.add(n);
                }
            }
        }
        if (totalGrassPieces.size() == 168) { // there are 168 total pieces in an editor pane
            // if all of them are grass, i.e. they contain grass.png in their URI name then the scenario is considered empty.
            isScenarioEmpty = true;
        }
    }

    /**
     * This method loads a scenario from a text file, reading in every line and mapping out the variables.
     * Before loading, it must clear all the nodes of the EditorPane and then re-add every node based on the text file.
     * It also contains a line counter, when there are more lines after line 168, this means there are free form nodes that exist, these are then
     * added into the scenario.
     * @param path File path to the .txt file containing the scenario
     * @throws IOException If the path cannot be accessed, throw this exception.
     */
    private void loadScenarioFromFile(File path) throws IOException {
        tseep.getChildren().removeAll();
        BufferedReader bufferedReader = null;
        try {
            String CurrentLine;
            bufferedReader = new BufferedReader(new FileReader(path));
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
                    if (imageURI.contains("cone") || imageURI.contains("barrier")) {
                        n.setFitHeight(15);
                        n.setFitWidth(15);
                        n.setPreserveRatio(true);
                    }
                    if (imageURI.contains("trafficLight")) {
                        n.setFitHeight(20);
                        n.setFitWidth(20);
                        n.setPreserveRatio(true);
                    }
                    tseep.getChildren().add(n);
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
                tseep.add(cellButton, col, row);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            bufferedReader.close();
        }
    }

    /**
     * This method disables the effects applied on the BottomPane piece selection slots, this indicates to the user that there is no current piece currently selected.
     */
    private void resetSlotSelection() {
        ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
        Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
    }

    /**
     * This method mutes and un-mutes sound by accepting a boolean value and re-assigning muteSounds to that value.
     * @param bool True or False value. True to mute sounds, false to un-mute sounds.
     */
    public void setMuteSounds(Boolean bool) {
        this.muteSounds = bool;
    }

    /**
     * This method returns the value of muteSounds.
     * @return Boolean value of muteSounds variable.
     */
    public boolean getMuteSounds() {
        return muteSounds;
    }

    /**
     * This method increments the piece counter, the total number of added pieces currently in the scenario
     */
    public void incrementPieceCounter() {
        this.pieceCounter++;
    }

    /**
     * This method retrieves the value of piece counter variable, total number of added pieces in the scenario.
     * @return Integer value representing the total number of ADDED pieces in the scenario.
     */
    public int getPieceCounterValue() {
        return this.pieceCounter;
    }

    /**
     * This method sets the current scene to a passed in scene instance.
     * @param scene The scene that should be set as the current scene.
     */
    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }

    /**
     * This method gets the current scene.
     * @return The current scene instance.
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * This is a debug method to check the location of each graphnode and to ensure it is mapped to the correct
     * location within each road piece (or destination piece). It places circles that act as 'markers'.
     * @param n GraphNode of type N (graphnodes contain x and y coordinates)
     * @param c Colour of the circle/marker
     */
    public void debugDisplayNodeLocation(GraphNode n, Color c) {
        Circle circle = new Circle(n.getXCoordinate(), n.getYCoordinate(), 2.0f);
        circle.setFill(c);
        circle.setTranslateX(n.getXCoordinate());
        circle.setTranslateY(n.getYCoordinate());

        System.out.println(n);
        System.out.println("X coord: " + n.getXCoordinate());
        System.out.println("Y coord: " + n.getYCoordinate());
        tseep.getChildren().add(circle);
    }

    /**
     * This is a debug method that creates circles (or markers) at every GraphNode in the scenario in order
     * to check their position. This has become redundant and was later implemented as an official feature "Display All Nodes"
     * as a menu bar item.
     */
    public void debugDisplayAllNodesLocation() {
        for (GraphNode n : graph.getRouteList()) {
            Circle circle = new Circle(n.getXCoordinate(), n.getYCoordinate(), 2.0f);
            circle.setFill(Color.GREEN);
            circle.setTranslateX(n.getXCoordinate());
            circle.setTranslateY(n.getYCoordinate());
            tseep.getChildren().add(circle);
        }
    }
}
