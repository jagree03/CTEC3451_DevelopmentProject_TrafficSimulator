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

    private AStarGraph graph;

    private int CurrentRotationDegrees = 0; // current rotation value
    private String currentPieceSelected = "";

    private String pieceType = "";

    private int pieceCounter = 0; // does NOT include miscPieces such as decorative or hazard pieces.
    private boolean muteSounds = false;

    private boolean isScenarioEmpty;

    private Scene currentScene;

    private Scene scene;


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

    private void attachEventHandlers() {
        //attach an event handler to the menu item of the menu bar that shows about author info about the program
        tse_menubar.addAboutHandler(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Alert about = new Alert(Alert.AlertType.INFORMATION);
                about.setTitle("Controls");
                about.setHeaderText("Controls for Traffic Simulator");
                about.setContentText("Left click on pieces to select them from the bottom panel, what you select is reflected on your mouse cursor. " +
                        "To place your piece, left click anywhere within the light blue panel."
                        + "\n\n"
                        + "F Key - Deselect Piece"
                        + "\n\n"
                        + "R key - Rotate Piece By 90 Degrees"
                        + "\n\n"
                        + "When rotating road surfaces, an icon will appear at the bottom right corner to display the rotated piece."
                        + '\n'
                        + "The green arrow represents the LEFT lane, whereas the yellow arrow represents the RIGHT lane.");
                about.show();
            }
        });

        //attach an event handler to the menu bar that closes the application
        tse_menubar.addExitHandler(e -> System.exit(0));

        //attach an event handler to the menu bar that allows the user to save the current scenario
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

        tse_menubar.addLoadScenarioHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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

        tse_bottompane.getPiecesButtonsPane().addDestinationsButtonOnClickHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pieceType = "destination";
                tse_bottompane.getPiecesSelection().setToDestinationPieces();

                ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
                for (int i = 0; i < arrayOfSlots.length; i++) {
                    arrayOfSlots[i].setDisable(false);
                    arrayOfSlots[i].setVisible(true);
                }

                Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
                tse_bottompane.setRotationImage(new WritableImage(45, 45));
            }
        });

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
                }
                if (!isScenarioEmpty) {
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
                    alert.setContentText("Scenario is unmodified, no roads or destinations found");
                    alert.show();
                }
            }
        });

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
         * current editor pane instance of the application.
         *
         * First it must check if a piece is selected, if not then show error message and handle the exceptions.
         *
         * It accepts the generated event from a node (clickable ImageViewButton in the editor pane) that is
         * specifically a mouse event. It then checks if the current piece selected is from the Decorative type
         * and if so, retrieve the X and Y coordinates of where the left click mouse action occurred within the editor pane
         * This is required so when the decorative piece is added to the scenario (editor pane), it can be situated in the correct
         * position akin to where the mouse was clicked, using these x and y coordinates from the generated event
         * It then instantiates a new ImageView node, that takes in the selected decorative piece's image, by calling obj
         * obj is a casted ImageViewButton variable that gets the source of the event (where the mouse event occurred) so this is
         * one of the individual ImageViewButton's / Cell buttons.
         *
         * Else, if the selected piece is NOT of decorative type, then proceed to set the cell button's image to the
         * current selected piece (i.e. straight road) and assign the specific properties like width, height, preserve ratio.
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
                        //obj.setRotate(currentRotationDeg);
                        obj.setFitHeight(45);
                        obj.setFitWidth(40);
                        obj.setPreserveRatio(true);
                        //System.out.println(currentPieceSelected);
                        Bounds bounds = tseep.getBoundsInParent();
                        Bounds b = obj.getBoundsInParent(); // gets the bounds/coordinates of each imageviewbutton/cellbutton inside the pane which is inside the entire VBox layout.
                        //System.out.println(b.getMinX() + " " + b.getMinY());


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

                            GraphNode int4_entrance = new GraphNode(b.getMinX()+25, b.getMinY()+20);
                            GraphNode int4_0 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode int4_1 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode int4_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int4_3 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode int4_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int4_5 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode int4_6 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode int4_exit = new GraphNode(b.getMinX()+25, b.getMinY()-20);

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

                            GraphNode int4_entrance = new GraphNode(b.getMinX()+25, b.getMinY()+20);
                            GraphNode int4_0 = new GraphNode(b.getMinX()+25, b.getMinY()+15);
                            GraphNode int4_1 = new GraphNode(b.getMinX()+25, b.getMinY()+10);
                            GraphNode int4_2 = new GraphNode(b.getMinX()+25, b.getMinY()+5);
                            GraphNode int4_3 = new GraphNode(b.getMinX()+25, b.getMinY()+0);
                            GraphNode int4_4 = new GraphNode(b.getMinX()+25, b.getMinY()-5);
                            GraphNode int4_5 = new GraphNode(b.getMinX()+25, b.getMinY()-10);
                            GraphNode int4_6 = new GraphNode(b.getMinX()+25, b.getMinY()-15);
                            GraphNode int4_exit = new GraphNode(b.getMinX()+25, b.getMinY()-20);

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

                        // if the currentPiece that was added is a destination piece (PETROL STATION) THEN
                        // map the node
                        if (currentPieceSelected.contains("1_petrolStation.png")) {
                            GraphNode dest = new GraphNode(b.getMinX()+17, b.getMinY()-2);

                            dest.setId("destination_PetrolStation"+getPieceCounterValue());
                            incrementPieceCounter();

                            graph.addGraphNodeToList(dest);
                        }

                        // if the currentPiece that was added is a destination piece (SHOP) THEN
                        // map the node
                        if (currentPieceSelected.contains("2_shop.png")) {
                            GraphNode dest = new GraphNode(b.getMinX()+17, b.getMinY()-2);

                            dest.setId("destination_Shop"+getPieceCounterValue());
                            incrementPieceCounter();

                            graph.addGraphNodeToList(dest);
                        }

                        // if the currentPiece that was added is a destination piece (OFFICE) THEN
                        // map the node
                        if (currentPieceSelected.contains("3_office.png")) {
                            GraphNode dest = new GraphNode(b.getMinX()+17, b.getMinY()-2);

                            dest.setId("destination_Office"+getPieceCounterValue());
                            incrementPieceCounter();

                            graph.addGraphNodeToList(dest);
                        }

                        // if the currentPiece that was added is a destination piece (HOSPITAL) THEN
                        // map the node
                        if (currentPieceSelected.contains("4_hospital.png")) {
                            GraphNode dest = new GraphNode(b.getMinX()+17, b.getMinY()-2);

                            dest.setId("destination_Hospital"+getPieceCounterValue());
                            incrementPieceCounter();

                            graph.addGraphNodeToList(dest);
                        }

                        // if the currentPiece that was added is a destination piece (HOTEL) THEN
                        // map the node
                        if (currentPieceSelected.contains("5_hotel.png")) {
                            GraphNode dest = new GraphNode(b.getMinX()+17, b.getMinY()-2);

                            dest.setId("destination_Hotel"+getPieceCounterValue());
                            incrementPieceCounter();

                            graph.addGraphNodeToList(dest);
                        }

                        // if the currentPiece that was added is a destination piece (BUS STATION) THEN
                        // map the node
                        if (currentPieceSelected.contains("6_bus_station.png")) {
                            GraphNode dest = new GraphNode(b.getMinX()+17, b.getMinY()-2);

                            dest.setId("destination_BusStation"+getPieceCounterValue());
                            incrementPieceCounter();

                            graph.addGraphNodeToList(dest);
                        }

                        // if the currentPiece that was added is a destination piece (BUS STOP) THEN
                        // map the node
                        if (currentPieceSelected.contains("7_bus_stop.png")) {
                            GraphNode dest = new GraphNode(b.getMinX()+17, b.getMinY()-2);

                            dest.setId("destination_BusStop"+getPieceCounterValue());
                            incrementPieceCounter();

                            graph.addGraphNodeToList(dest);
                        }
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No piece selected");
                    alert.setContentText("Please select a piece to add into the scenario.");
                    alert.show();
                }
                //graph.getRouteListFormatted();
                //debugDisplayAllNodesLocation();
            }
        });

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

    public void handleKeyPressDeselect () {
        ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
        Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
        currentPieceSelected = null;
        tse_bottompane.setRotationImage(new WritableImage(45, 45));
    }

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

    private void writeGraphNodesToFile() throws IOException {
        FileWriter fileWriter = new FileWriter("graphnodes.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (GraphNode node : graph.getRouteList()) {
            printWriter.println(node.getId() + ", " + node.getXCoordinate() + ", " + node.getYCoordinate());
        }
        printWriter.close();
        fileWriter.close();
    }

    private void writeGraphObjectToFile(File scenarioPath) throws IOException {
        scenarioPath = new File(scenarioPath.getAbsolutePath().replace(".txt", ".dat"));
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(scenarioPath));
        oos.writeObject(graph);
        oos.flush();
        oos.close();
    }

    private void readGraphObjectFromFile(File scenarioPath) throws IOException, ClassNotFoundException {
        scenarioPath = new File(scenarioPath.getAbsolutePath().replace(".txt", ".dat"));
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(scenarioPath));
        graph = (AStarGraph) ois.readObject();
        ois.close();
    }

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

    private void resetSlotSelection() {
        ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
        Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
    }

    public void setMuteSounds(Boolean bool) {
        this.muteSounds = bool;
    }

    public boolean getMuteSounds() {
        return muteSounds;
    }

    public void incrementPieceCounter() {
        this.pieceCounter++;
    }

    public int getPieceCounterValue() {
        return this.pieceCounter;
    }

    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * This is a method to check the location of each graphnode and to ensure it is mapped to the correct
     * location within each road piece.
     * @param n GraphNode of type N, graphnodes contain x and y coordinates.
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