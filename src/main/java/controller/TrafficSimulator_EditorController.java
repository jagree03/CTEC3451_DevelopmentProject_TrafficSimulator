package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                        + "R key - Rotate Piece By 90 Degrees");
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
                File scenarioPath = fileChooser.showSaveDialog(stage);
                if (!(scenarioPath.getPath().contains(".txt"))) {
                    scenarioPath = new File(scenarioPath.getPath() + ".txt");
                }
                try {
                    writeScenarioDataToFile(scenarioPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Scenario has been saved.");
                alert.setContentText("Scenario file saved at: " + scenarioPath);
                alert.showAndWait();
            }
        });

        tse_menubar.addLoadScenarioHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt*"));
                Stage stage = (Stage) tse_bottompane.getScene().getWindow();
                File scenarioPath = fileChooser.showOpenDialog(stage);
                try {
                    loadScenarioFromFile(scenarioPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Scenario has been loaded.");
                alert.setContentText("Scenario file loaded from: " + scenarioPath);
                alert.showAndWait();
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
                tse_bottompane.getPiecesSelection().setToRoadSurfacePieces();
                ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
                for (int i = 0; i < arrayOfSlots.length; i++) {
                    arrayOfSlots[i].setDisable(false);
                    arrayOfSlots[i].setVisible(true);
                }
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
            }
        });

        tse_bottompane.getPiecesButtonsPane().addDecorativeButtonOnClickHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pieceType = "decorative";
                tse_bottompane.getPiecesSelection().setToDecorativePieces();
                ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
                for (int i = 0; i < arrayOfSlots.length; i++) {
                    arrayOfSlots[i].setDisable(false);
                    arrayOfSlots[i].setVisible(true);
                }
                Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
            }
        });

        tse_bottompane.getPiecesButtonsPane().addHazardsButtonOnClickHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pieceType = "hazard";
                tse_bottompane.getPiecesSelection().setToHazardPieces();
                ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
                for (int i = 2; i < arrayOfSlots.length; i++) {
                    arrayOfSlots[i].setDisable(true);
                    arrayOfSlots[i].setVisible(false);
                }
                Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                    if (!(pieceType.equals("decorative") || pieceType.equals("hazard"))) {
                        ImageViewButton obj = (ImageViewButton) event.getSource();
                        obj.setImage(new Image(currentPieceSelected));
                        //obj.setRotate(currentRotationDeg);
                        obj.setFitHeight(45);
                        obj.setFitWidth(40);
                        obj.setPreserveRatio(true);
                        System.out.println(currentPieceSelected);
                        Bounds bounds = tseep.getBoundsInParent();
                        Bounds b = obj.getBoundsInParent();
                        System.out.println(b.getMinX() + " " + b.getMinY());
                        // if the currentPiece that was added is a straight road piece THEN
                        if (currentPieceSelected.contains("1_straightRoad.png")) {
                            // create a set of graph nodes, originally all placed at a default position of each ImageViewButton, then manually positioned using addition/subtraction
                            GraphNode leftLaneBottom = new GraphNode(b.getMinX()+10, b.getMinY()+14);
                            GraphNode leftLaneTop = new GraphNode(obj.getLayoutX()+10, obj.getLayoutY()-14);
                            GraphNode rightLaneBottom = new GraphNode(obj.getLayoutX()+24, obj.getLayoutY()+14);
                            GraphNode rightLaneTop = new GraphNode(obj.getLayoutX()+24, obj.getLayoutY()-14);
                            leftLaneBottom.setId("left_"+(getPieceCounterValue()));
                            leftLaneTop.setId("left_"+(getPieceCounterValue()));
                            rightLaneBottom.setId("right_"+(getPieceCounterValue()));
                            rightLaneTop.setId("right_"+(getPieceCounterValue()));

                            // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                            // Debug and checking coordinate locations
                            // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                            Circle circle = new Circle(leftLaneBottom.getXCoordinate(), leftLaneBottom.getYCoordinate(), 2.0f);
                            circle.setFill(Color.RED);
                            circle.setTranslateX(leftLaneBottom.getXCoordinate());
                            circle.setTranslateY(leftLaneBottom.getYCoordinate());

                            System.out.println(leftLaneBottom);
                            System.out.println(leftLaneBottom.getXCoordinate());
                            System.out.println(leftLaneBottom.getYCoordinate());
                            //tseep.getChildren().add(circle);

                            // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

                            graph.addGraphNodeToList(leftLaneBottom);
                            graph.addGraphNodeToList(leftLaneTop);
                            graph.addGraphNodeToList(rightLaneBottom);
                            graph.addGraphNodeToList(rightLaneTop);
                        }

                        // if the currentPiece that was added is a straight road piece rotated to 90 degrees THEN
                        if (currentPieceSelected.contains("1_straightRoad_90.png")) {
                            GraphNode leftLaneTopLeft = new GraphNode(obj.getLayoutX()+5, obj.getLayoutY()-8);
                            GraphNode leftLaneTopRight = new GraphNode(obj.getLayoutX()+27, obj.getLayoutY()-8);
                            GraphNode rightLaneBottomLeft = new GraphNode(obj.getLayoutX()+5, obj.getLayoutY()+8);
                            GraphNode rightLaneBottomRight = new GraphNode(obj.getLayoutX()+27, obj.getLayoutY()+8);

                            leftLaneTopLeft.setId("left_"+getPieceCounterValue());
                            leftLaneTopRight.setId("left_"+(getPieceCounterValue()+1));
                            rightLaneBottomLeft.setId("right_"+getPieceCounterValue());
                            rightLaneBottomRight.setId("right_"+(getPieceCounterValue()+1));

                            graph.addGraphNodeToList(leftLaneTopLeft);
                            graph.addGraphNodeToList(leftLaneTopRight);
                            graph.addGraphNodeToList(rightLaneBottomLeft);
                            graph.addGraphNodeToList(rightLaneBottomRight);
                        }
                        // if the currentPiece that was added is a straight road piece rotated to 180 degrees THEN
                        // do this.. Also to point out that the left lanes and right lanes are flipped, from the image
                        // the rightmost lane is now the Left Lane, cars should travel from Top to Bottom
                        // the leftmost lane is now the right lane, cars should travel from Bottom to Top.
                        if (currentPieceSelected.contains("1_straightRoad_180.png")) {
                            GraphNode leftLaneTop = new GraphNode(obj.getLayoutX()+24, obj.getLayoutY()-14);
                            GraphNode leftLaneBottom = new GraphNode(obj.getLayoutX()+24, obj.getLayoutY()+14);
                            GraphNode rightLaneTop = new GraphNode(obj.getLayoutX()+10, obj.getLayoutY()-14);
                            GraphNode rightLaneBottom = new GraphNode(obj.getLayoutX()+10, obj.getLayoutY()+14);

                            leftLaneTop.setId("left_"+getPieceCounterValue());
                            leftLaneBottom.setId("left_"+(getPieceCounterValue()+1));
                            rightLaneTop.setId("right_"+getPieceCounterValue());
                            rightLaneBottom.setId("right_"+(getPieceCounterValue()+1));

                            graph.addGraphNodeToList(leftLaneTop);
                            graph.addGraphNodeToList(leftLaneBottom);
                            graph.addGraphNodeToList(rightLaneBottom);
                            graph.addGraphNodeToList(rightLaneTop);
                        }
                        if (currentPieceSelected.contains("1_straightRoad_270.png")) {
                            GraphNode leftLaneBottomLeft = new GraphNode(obj.getLayoutX()+5, obj.getLayoutY()+8);
                            GraphNode leftLaneBottomRight = new GraphNode(obj.getLayoutX()+27, obj.getLayoutY()+8);
                            GraphNode rightLaneTopLeft = new GraphNode(obj.getLayoutX()+5, obj.getLayoutY()-8);
                            GraphNode rightLaneTopRight = new GraphNode(obj.getLayoutX()+27, obj.getLayoutY()-8);

                            leftLaneBottomLeft.setId("left_"+getPieceCounterValue());
                            leftLaneBottomRight.setId("left_"+(getPieceCounterValue()+1));
                            rightLaneTopLeft.setId("right_"+getPieceCounterValue());
                            rightLaneTopRight.setId("right_"+(getPieceCounterValue()+1));

                            graph.addGraphNodeToList(leftLaneBottomLeft);
                            graph.addGraphNodeToList(leftLaneBottomRight);
                            graph.addGraphNodeToList(rightLaneTopLeft);
                            graph.addGraphNodeToList(rightLaneTopRight);
                        }
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No piece selected");
                    alert.setContentText("Please select a piece to add into the scenario.");
                    alert.show();
                }
            }
        });

        tseep.add_MiscPiecesHandler(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (pieceType.equals("decorative") || pieceType.equals("hazard")) {
                    Double xCoordinate = event.getX();
                    Double yCoordinate = event.getY();
                    ImageView piece = new ImageView(new Image(currentPieceSelected));
                    piece.setFitHeight(35);
                    piece.setFitWidth(35);
                    piece.setPreserveRatio(true);
                    piece.setTranslateX(xCoordinate-10);
                    piece.setTranslateY(yCoordinate-21);
                    tseep.getChildren().add(piece);
                }
            }
        });
    }

    public void handleKeyPress() throws UnsupportedAudioFileException, IOException { // rotation handling

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
                // System.out.println(currentPieceSelected);
            } else {
                currentPieceSelected = currentPieceSelected.replace("_" + (CurrentRotationDegrees - 90) + ".png", "_" + CurrentRotationDegrees + ".png");

//                if (currentPieceSelected.contains("3_turnRight_90.png")) {
//                    currentPieceSelected = currentPieceSelected.replace("3_turnRight_90", "2_turnLeft.png");
//                }
                // System.out.println(currentPieceSelected);
            }
        } else {
            currentPieceSelected = currentPieceSelected.replace("_" + (CurrentRotationDegrees-90) + ".png", ".png");
            CurrentRotationDegrees = 0;
            tse_bottompane.setCurrentRotationValue(CurrentRotationDegrees); // sets the text label of rotation
        }
    }

    public void handleKeyPressDeselect () {
        ImageView[] arrayOfSlots = tse_bottompane.getPiecesSelection().getSlots();
        Arrays.stream(arrayOfSlots).forEach(x -> x.setEffect(null));
        currentPieceSelected = null;
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
            System.out.println(ex.getMessage());
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
}
