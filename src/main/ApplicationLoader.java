 
package main;

import controller.TrafficSimulatorController;
import controller.TrafficSimulator_EditorController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.TrafficSimulatorRootPane;
import view.TrafficSimulator_Editor;

public class ApplicationLoader extends Application {

	private TrafficSimulatorRootPane view;
	
	@Override
	public void init() {
		//create view and model and pass their references to the controller
		view = new TrafficSimulatorRootPane();
		new TrafficSimulatorController(view);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		//sets min width and height for the stage window
		stage.setMinWidth(900);
		stage.setMinHeight(550);

		stage.setTitle("Traffic Simulator - Environment Options");
		stage.setScene(new Scene(view));
		stage.show();
	}



	public static void main(String[] args) {
		launch(args);
	}

}
