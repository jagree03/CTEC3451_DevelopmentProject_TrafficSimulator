package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Course;
import model.RunPlan;
import model.Module;
import model.StudentProfile;
import view.*;

import java.io.IOException;

public class TrafficSimulatorController {

	//fields to be used throughout class
	private TrafficSimulatorRootPane view;

	private TrafficSimulatorRootMenuBar tsmb; //traffic simulator menu bar at the top
	private TrafficSimulatorRootMenuButtons tsrmb; //traffic simulator menu buttons in the middle

	private TrafficSimulatorRootMenuBottomButtonPanel tsrmbbp; // traffic simulator options and exit buttons
	private StudentProfile model;

	private Stage stage;
	private Scene scene;
	private TrafficSimulator_Editor editorView;
	private TrafficSimulator_Procedural proceduralView;


	public TrafficSimulatorController(StudentProfile model, TrafficSimulatorRootPane view) {
		//initialise view and model fields
		this.view = view;
		this.model = model;

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
				new TrafficSimulator_EditorController(model, editorView);
				scene.setRoot(editorView); // change the root node of the current scene to the new screen
				stage.setTitle("Traffic Simulator - Build an environment");
			}
		});

		tsrmb.addProceduralOnClickHandler(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) tsrmb.getScene().getWindow(); // get the window from one of the Layout managers,
				scene = tsrmb.getScene(); // get the current scene
				proceduralView = new TrafficSimulator_Procedural();
				//new TrafficSimulator_ProceduralController(model, proceduralView);
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
	
	//event handler (currently empty), which can be used for creating a profile
	private class CreateStudentProfileHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			
			
		}
	}

	//helper method - builds modules and course data and returns courses within an array
	private Course[] buildModulesAndCourses() {
		Module imat3423 = new Module("IMAT3423", "Systems Building: Methods", 15, true, RunPlan.TERM_1);
		Module ctec3451 = new Module("CTEC3451", "Development Project", 30, true, RunPlan.YEAR_LONG);
		Module ctec3902_SoftEng = new Module("CTEC3902", "Rigorous Systems", 15, true, RunPlan.TERM_2);	
		Module ctec3902_CompSci = new Module("CTEC3902", "Rigorous Systems", 15, false, RunPlan.TERM_2);
		Module ctec3110 = new Module("CTEC3110", "Secure Web Application Development", 15, false, RunPlan.TERM_1);
		Module ctec3605 = new Module("CTEC3605", "Multi-service Networks 1", 15, false, RunPlan.TERM_1);	
		Module ctec3606 = new Module("CTEC3606", "Multi-service Networks 2", 15, false, RunPlan.TERM_2);	
		Module ctec3410 = new Module("CTEC3410", "Web Application Penetration Testing", 15, false, RunPlan.TERM_2);
		Module ctec3904 = new Module("CTEC3904", "Functional Software Development", 15, false, RunPlan.TERM_2);
		Module ctec3905 = new Module("CTEC3905", "Front-End Web Development", 15, false, RunPlan.TERM_2);
		Module ctec3906 = new Module("CTEC3906", "Interaction Design", 15, false, RunPlan.TERM_1);
		Module ctec3911 = new Module("CTEC3911", "Mobile Application Development", 15, false, RunPlan.TERM_1);
		Module imat3410 = new Module("IMAT3104", "Database Management and Programming", 15, false, RunPlan.TERM_2);
		Module imat3406 = new Module("IMAT3406", "Fuzzy Logic and Knowledge Based Systems", 15, false, RunPlan.TERM_1);
		Module imat3611 = new Module("IMAT3611", "Computer Ethics and Privacy", 15, false, RunPlan.TERM_1);
		Module imat3613 = new Module("IMAT3613", "Data Mining", 15, false, RunPlan.TERM_1);
		Module imat3614 = new Module("IMAT3614", "Big Data and Business Models", 15, false, RunPlan.TERM_2);
		Module imat3428_CompSci = new Module("IMAT3428", "Information Technology Services Practice", 15, false, RunPlan.TERM_2);


		Course compSci = new Course("Computer Science");
		compSci.addModuleToCourse(imat3423);
		compSci.addModuleToCourse(ctec3451);
		compSci.addModuleToCourse(ctec3902_CompSci);
		compSci.addModuleToCourse(ctec3110);
		compSci.addModuleToCourse(ctec3605);
		compSci.addModuleToCourse(ctec3606);
		compSci.addModuleToCourse(ctec3410);
		compSci.addModuleToCourse(ctec3904);
		compSci.addModuleToCourse(ctec3905);
		compSci.addModuleToCourse(ctec3906);
		compSci.addModuleToCourse(ctec3911);
		compSci.addModuleToCourse(imat3410);
		compSci.addModuleToCourse(imat3406);
		compSci.addModuleToCourse(imat3611);
		compSci.addModuleToCourse(imat3613);
		compSci.addModuleToCourse(imat3614);
		compSci.addModuleToCourse(imat3428_CompSci);

		Course softEng = new Course("Software Engineering");
		softEng.addModuleToCourse(imat3423);
		softEng.addModuleToCourse(ctec3451);
		softEng.addModuleToCourse(ctec3902_SoftEng);
		softEng.addModuleToCourse(ctec3110);
		softEng.addModuleToCourse(ctec3605);
		softEng.addModuleToCourse(ctec3606);
		softEng.addModuleToCourse(ctec3410);
		softEng.addModuleToCourse(ctec3904);
		softEng.addModuleToCourse(ctec3905);
		softEng.addModuleToCourse(ctec3906);
		softEng.addModuleToCourse(ctec3911);
		softEng.addModuleToCourse(imat3410);
		softEng.addModuleToCourse(imat3406);
		softEng.addModuleToCourse(imat3611);
		softEng.addModuleToCourse(imat3613);
		softEng.addModuleToCourse(imat3614);

		Course[] courses = new Course[2];
		courses[0] = compSci;
		courses[1] = softEng;

		return courses;
	}

}
