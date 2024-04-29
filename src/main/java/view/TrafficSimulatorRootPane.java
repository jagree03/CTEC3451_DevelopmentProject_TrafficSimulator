package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TrafficSimulatorRootPane extends VBox {
	private TrafficSimulatorRootMenuBar tsmb;
	private TrafficSimulatorRootMenuButtons tsrmb;

	private TrafficSimulatorRootMenuBottomButtonPanel tsrmbbp;
	private Label lblAuthorAndVersion;
	private TrafficSimulator_Editor editorView = new TrafficSimulator_Editor();

	/**
	 * Default Constructor
	 */
	public TrafficSimulatorRootPane() {
		lblAuthorAndVersion = new Label("Jagjeet Reehal P2652829, Final Year Project 2024");
		lblAuthorAndVersion.setPadding(new Insets(10,10,10,10));
		//lblAuthorAndVersion.setAlignment();

		// create the panes i.e. parts of this scene window
		tsmb = new TrafficSimulatorRootMenuBar();
		tsrmb = new TrafficSimulatorRootMenuButtons();
		tsrmbbp = new TrafficSimulatorRootMenuBottomButtonPanel();
		tsrmbbp.setPadding(new Insets(30,0,0,0));

		this.setAlignment(Pos.TOP_CENTER);
		this.getChildren().addAll(tsmb, lblAuthorAndVersion, tsrmb, tsrmbbp);
	}


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
