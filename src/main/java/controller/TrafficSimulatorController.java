package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class TrafficSimulatorController {

	//fields to be used throughout class
	private TrafficSimulatorRootPane view;

	private TrafficSimulatorRootMenuBar tsmb; //traffic simulator menu bar at the top
	private TrafficSimulatorRootMenuButtons tsrmb; //traffic simulator menu buttons in the middle

	private TrafficSimulatorRootMenuBottomButtonPanel tsrmbbp; // traffic simulator options and exit buttons

	private Stage stage;
	private Scene scene;
	private TrafficSimulator_Editor editorView;
	private TrafficSimulator_Procedural proceduralView;


	public TrafficSimulatorController(TrafficSimulatorRootPane view) {
		//initialise view and model fields
		this.view = view;


		//initialise view subcontainer fields
		tsmb = view.getTSMenuBar();
		tsrmb = view.getTSMenuButtons();
		tsrmbbp = view.getTSMenuButtonsBottomButtonPanel();


		//add courses to combobox in create student profile pane using the buildModulesAndCourses helper method below
		//cspp.addCourseDataToComboBox(buildModulesAndCourses());

		//attach event handlers to view using private helper method
		this.attachEventHandlers();
	}

	
	//helper method - used to attach event handlers
	private void attachEventHandlers() {
		//attach an event handler to the create student profile pane
		//cspp.addCreateStudentProfileHandler(new CreateStudentProfileHandler());
		
		//attach an event handler to the menu bar that closes the application
		tsmb.addExitHandler(e -> System.exit(0));


		//attach an event handler to the menu item of the menu bar that shows about author info about the program
		tsmb.addAboutHandler(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Alert about = new Alert(Alert.AlertType.INFORMATION);
				about.setTitle("About the program");
				about.setHeaderText("Final Year Development Project - Traffic Simulator");
				about.setContentText("This tool simulates traffic flow with drivers in an environment using complex behaviours and graphics for visualisation."
						+ "\n\n" + "Version 1.0" + "\n\n" + "Project By Jagjeet Reehal P2652829");
				about.show();
			}
		});

		tsrmb.addEditorMouseEnteredHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ColorAdjust colorAdjust_entered = new ColorAdjust();
				//Setting the brightness value
				colorAdjust_entered.setBrightness(0.5);
				tsrmb.getShowEditor().setEffect(colorAdjust_entered);
			}
		});

		tsrmb.addEditorMouseExitedHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// remove decreased brightness effect
				// after mouse leaves the ImageView showing the editor logo
				tsrmb.getShowEditor().setEffect(null);
			}
		});

		tsrmb.addProceduralMouseEnteredHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ColorAdjust colorAdjust_entered = new ColorAdjust();
				//Setting the brightness value
				colorAdjust_entered.setBrightness(0.5);
				tsrmb.getShowProcedural().setEffect(colorAdjust_entered);
			}
		});

		tsrmb.addProceduralMouseExitedHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// remove decreased brightness effect
				// after mouse leaves the ImageView showing the procedural logo
				tsrmb.getShowProcedural().setEffect(null);
			}
		});


		tsrmb.addEditorOnClickHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) tsrmb.getScene().getWindow(); // get the window from one of the Layout managers,
				scene = tsrmb.getScene(); // get the current scene
				editorView = new TrafficSimulator_Editor();
				TrafficSimulator_EditorController editorController = new TrafficSimulator_EditorController(editorView);
				scene.setRoot(editorView); // change the root node of the current scene to the new screen
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
					else if (e.getCode() == KeyCode.V) {
						try {
							editorController.handleKeyPressInvertLanes();
						} catch (UnsupportedAudioFileException ex) {
							throw new RuntimeException(ex);
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}
					}
				});
				stage.setTitle("Traffic Simulator - Build an environment");
			}
		});

		tsrmb.addProceduralOnClickHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) tsrmb.getScene().getWindow(); // get the window from one of the Layout managers,
				scene = tsrmb.getScene(); // get the current scene
				proceduralView = new TrafficSimulator_Procedural();
				new TrafficSimulator_ProceduralController(proceduralView);
				scene.setRoot(proceduralView); // change the root node of the current scene to the new screen
				stage.setTitle("Traffic Simulator - Generate a random scenario");
			}
		});

		tsrmbbp.addExitHandler(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});

	}
}
