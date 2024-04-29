package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private Scene scene;
	private TrafficSimulator_Editor editorView;


	/**
	 * Constructor for the Main Menu controller (TrafficSimulatorController)
	 * @param view a TrafficSimulatorRootPane instance
	 */
	public TrafficSimulatorController(TrafficSimulatorRootPane view) {
		//initialise view and model fields
		this.view = view;


		//initialise view subcontainer fields
		tsmb = view.getTSMenuBar();
		tsrmb = view.getTSMenuButtons();
		tsrmbbp = view.getTSMenuButtonsBottomButtonPanel();

		//attach event handlers to view using private helper method
		this.attachEventHandlers();
	}


	/**
	 * This method attaches the event handlers to the respective controls in each of the main menu related view classes that form the whole
	 * main menu screen
	 */
	private void attachEventHandlers() {

		/**
		 * This method attaches an event handler to the exit item of the menu bar, when clicked, program closes.
		 */
		tsmb.addExitHandler(e -> System.exit(0));


		/**
		 * This method attaches an event handler to the about item of the menu bar, when clicked, an alert will pop up
		 * to show the information about the author and what the application is about.
		 */
		tsmb.addAboutHandler(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Alert about = new Alert(Alert.AlertType.INFORMATION);
				about.setTitle("About the program");
				about.setHeaderText("Final Year Development Project - Traffic Simulator");
				about.setContentText("This tool simulates traffic flow with drivers in an environment using complex behaviours and graphics for visualisation."
						+ "\n\n" + "Project By Jagjeet Reehal P2652829");
				about.show();
			}
		});

		/**
		 * This method attaches an event handler that handles mouse events based on if the mouse has entered the ImageView icon
		 * (ImageView icons represent the menu options, so Environment Editor or Procedural Generation).
		 * If mouse entered then showcase this to the user with a brightness effect applied to the ImageView.
		 */
		tsrmb.addEditorMouseEnteredHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ColorAdjust colorAdjust_entered = new ColorAdjust();
				//Setting the brightness value
				colorAdjust_entered.setBrightness(0.5);
				tsrmb.getShowEditor().setEffect(colorAdjust_entered);
			}
		});

		/**
		 * This method attaches an event handler that handles mouse events based on if the mouse has exited the ImageView icon
		 * (ImageView icons represent the menu options, so Environment Editor or Procedural Generation).
		 * If mouse exited then showcase this to the user by disabling the previous brightness effect, essentially
		 * setting the effect to null.
		 */
		tsrmb.addEditorMouseExitedHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// remove decreased brightness effect
				// after mouse leaves the ImageView showing the editor logo
				tsrmb.getShowEditor().setEffect(null);
			}
		});

		/**
		 * This method attaches an event handler that handles mouse events based on if the mouse has entered the ImageView icon
		 * (ImageView icons represent the menu options, so Environment Editor or Procedural Generation).
		 * If mouse entered then showcase this to the user with a brightness effect applied to the ImageView.
		 */
		tsrmb.addProceduralMouseEnteredHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ColorAdjust colorAdjust_entered = new ColorAdjust();
				//Setting the brightness value
				colorAdjust_entered.setBrightness(0.5);
				tsrmb.getShowProcedural().setEffect(colorAdjust_entered);
			}
		});

		/**
		 * This method attaches an event handler that handles mouse events based on if the mouse has exited the ImageView icon
		 * (ImageView icons represent the menu options, so Environment Editor or Procedural Generation).
		 * If mouse exited then showcase this to the user by disabling the previous brightness effect, essentially
		 * setting the effect to null.
		 */
		tsrmb.addProceduralMouseExitedHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// remove decreased brightness effect
				// after mouse leaves the ImageView showing the procedural logo
				tsrmb.getShowProcedural().setEffect(null);
			}
		});

		/**
		 * If the ImageView Icon representing the Environment Editor is clicked, this method attaches an event handler to that specific event.
		 * When clicked, the scene in the current stage will switch to the next screen, which is the main environment editor screen/scene.
		 *
		 * It also sets up key press events, so if R key (Rotation), F key (Deselection), V key (Invert Lanes) are pressed then this generates
		 * an event that is handled by the TrafficSimulator_EditorController
		 */
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

		/**
		 * If the ImageView Icon representing Procedural Generation is clicked, this method attaches an event handler to that specific event.
		 * When clicked, the scene in the current stage will switch to the next screen, which is the main procedural generation screen/scene.
		 *
		 * UN-IMPLEMENTED METHOD
		 */
		tsrmb.addProceduralOnClickHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				/*
				Stage stage = (Stage) tsrmb.getScene().getWindow(); // get the window from one of the Layout managers,
				scene = tsrmb.getScene(); // get the current scene
				proceduralView = new TrafficSimulator_Procedural();
				new TrafficSimulator_ProceduralController(proceduralView);
				scene.setRoot(proceduralView); // change the root node of the current scene to the new screen
				stage.setTitle("Traffic Simulator - Generate a random scenario");
				 */
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("NOT IMPLEMENTED");
				alert.setHeaderText("Procedural generation");
				alert.setContentText("Procedural generation not implemented.");
				alert.showAndWait();
			}
		});

		/**
		 * This method attaches an event handler to the exit button in the Main Menu's Bottom Button Panel,
		 * when clicked, exit the program.
		 */
		tsrmbbp.addExitHandler(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
	}
}
