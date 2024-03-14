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

public class TrafficSimulator_EditorController {

    private TrafficSimulator_Editor view;

    private TrafficSimulatorRootMenuBar tse_menubar;
    private TrafficSimulator_EditorBottomPane tse_bottompane;

    private TrafficSimulator_EditorEditorPane tseep;

    private int CurrentRotationDegrees = 0; // current rotation value

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
        this.attachEventHandlers();
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
                        + "F Key - Deselect piece"
                        + "\n\n"
                        + "R key - Rotate Piece by 90 degrees");
                about.show();
            }
        });

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
                Stage stage = (Stage) tse_bottompane.getScene().getWindow(); // get the current window and cast to Stage
                scene = tse_bottompane.getScene(); // get the current scene
                scene.setCursor(null);
                TrafficSimulator_SimulatorSettings view = new TrafficSimulator_SimulatorSettings();
                new TrafficSimulator_SimulatorSettingsController(view);
                scene.setRoot(view); // change the root node of the current scene to the new screen
                stage.setTitle("Traffic Simulator - Simulation Options");
            }
        });

        tse_bottompane.getPiecesSelection().CursorOnClickHandler(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Scene scene = tse_bottompane.getScene();
                ImageCursor cursor = new ImageCursor(tse_bottompane.getPiecesSelection().getImageSlot1());
                scene.setCursor(cursor);
            }
        });

        tseep.add_BuildHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int currentRotationDeg = CurrentRotationDegrees;

                Scene scene = tseep.getScene();
                ImageCursor cursor = (ImageCursor) scene.getCursor();

                if (cursor != null) {

                    File straightRoad = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\1_straightRoad.png");
                    String straightRoadURI_0Degrees = straightRoad.toURI().toString();
                    Image imgStraightRoad = new Image(straightRoadURI_0Degrees);

                    File straightRoad_90 = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\1_straightRoad_90.png");
                    String straightRoadURI_90Degrees = straightRoad_90.toURI().toString();
                    Image imgStraightRoad_90 = new Image(straightRoadURI_90Degrees);

                    File straightRoad_180 = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\1_straightRoad_180.png");
                    String straightRoadURI_180Degrees = straightRoad_180.toURI().toString();
                    Image imgStraightRoad_180 = new Image(straightRoadURI_180Degrees);

                    File straightRoad_270 = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\1_straightRoad_270.png");
                    String straightRoadURI_270Degrees = straightRoad_270.toURI().toString();
                    Image imgStraightRoad_270 = new Image(straightRoadURI_270Degrees);

//                    File slot2_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\2_turnLeft.png");
//                    File slot3_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\3_turnRight.png");
//                    File slot4_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\4_straightRoadTrafficLeft.png");
//                    File slot5_tempImg = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\5_straightRoadTrafficRight.png");

                    // DECORATIVE PIECES
                    File tree = new File("C:\\Users\\jagre\\Documents\\IntelliJ\\Projects\\TrafficSimulator_CTEC3451\\img\\2_EditorScreen\\decorative\\1_tree.png");
                    String treeURI = tree.toURI().toString();
                    Image imgTree = new Image(treeURI);

                    ImageView slot1 = null;
                    if (cursor.getImage().getUrl().equals(straightRoadURI_0Degrees)) { // if the cursor's image is equal to the slot1_img (Straight Road Piece)
                        slot1 = new ImageView(imgStraightRoad);
                        slot1.setRotate(0);
                        slot1.setFitWidth(60);
                        slot1.setFitHeight(60);
                        slot1.setPreserveRatio(true);

                        tseep.getChildren().add(slot1);
                    }
                }
            }
        });


        public void handleKeyPress() { // rotation handling
            CurrentRotationDegrees += 90;
            if (CurrentRotationDegrees != 360) {
                tse_bottompane.setCurrentRotationValue(CurrentRotationDegrees);
            } else {
                CurrentRotationDegrees = 0;
                tse_bottompane.setCurrentRotationValue(CurrentRotationDegrees);
            }
        }

        public void handleKeyPressDeselect() {
            Scene scene = tseep.getScene();

        }
}
