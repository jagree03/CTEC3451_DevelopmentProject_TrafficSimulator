package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class TrafficSimulatorRootPane extends VBox {
	/*
	private CreateStudentProfilePane cspp;

	private TabPane tp;
	 */
	private TrafficSimulatorRootMenuBar tsmb;
	private TrafficSimulatorRootMenuButtons tsrmb;

	private TrafficSimulatorRootMenuBottomButtonPanel tsrmbbp;
	private Label lblAuthorAndVersion;

	///
	private TrafficSimulator_Editor editorView = new TrafficSimulator_Editor();
	
	public TrafficSimulatorRootPane() {
		Label lblAuthorAndVersion = new Label("Jagjeet Reehal, Version 1.0");
		lblAuthorAndVersion.setPadding(new Insets(10,10,10,10));
		//lblAuthorAndVersion.setAlignment();

		// create the panes i.e. parts of this scene window
		tsmb = new TrafficSimulatorRootMenuBar();
		tsrmb = new TrafficSimulatorRootMenuButtons();
		tsrmbbp = new TrafficSimulatorRootMenuBottomButtonPanel();


		this.setAlignment(Pos.TOP_CENTER);
		this.getChildren().addAll(tsmb, lblAuthorAndVersion, tsrmb, tsrmbbp);
	}

	//methods allowing sub-containers to be accessed by the controller.
	/*
	public CreateStudentProfilePane getCreateStudentProfilePane() {
		return cspp;
	}
	 */

	public TrafficSimulatorRootMenuBar getTSMenuBar() {
		return tsmb;
	}

	public TrafficSimulatorRootMenuButtons getTSMenuButtons() {
		return tsrmb;
	}

	public TrafficSimulatorRootMenuBottomButtonPanel getTSMenuButtonsBottomButtonPanel() {
		return tsrmbbp;
	}

	public TrafficSimulator_Editor getEditorView(){
		return editorView;
	}

}
