package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;


public class TrafficSimulator_EditorMenuBar extends MenuBar {

	private MenuItem aboutItem, exitItem, loadItem, saveItem, muteItem, clearScenarioItem;

	public TrafficSimulator_EditorMenuBar() {

		//temp var for menus and menu items within this MenuBar
		Menu menu;

		menu = new Menu("_File");


		loadItem = new MenuItem("_Load Scenario");
		loadItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+L"));
		menu.getItems().add(loadItem);

		saveItem = new MenuItem("_Save Scenario");
		saveItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+S"));
		menu.getItems().add(saveItem);


		menu.getItems().add( new SeparatorMenuItem());

		exitItem = new MenuItem("E_xit");
		exitItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+X"));
		menu.getItems().add(exitItem);

		this.getMenus().add(menu);   


		menu = new Menu("_Help");

		aboutItem = new MenuItem("_About");
		aboutItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+A"));
		menu.getItems().add(aboutItem);

		muteItem = new MenuItem("_Mute Sounds");
		muteItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+M"));
		menu.getItems().add(muteItem);

		clearScenarioItem = new MenuItem("_Clear and Reset Scenario");
		clearScenarioItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+C"));
		menu.getItems().add(clearScenarioItem);

		this.getMenus().add(menu); 
	}

	//these methods allow handlers to be externally attached to this menubar and used by the controller

	public void addSaveScenarioHandler(EventHandler<ActionEvent> handler) {
		saveItem.setOnAction(handler);
	}
	
	public void addLoadScenarioHandler(EventHandler<ActionEvent> handler) {
		loadItem.setOnAction(handler);
	}

	public void addAboutHandler(EventHandler<ActionEvent> handler) {
		aboutItem.setOnAction(handler);
	}

	public void addMuteSoundsHandler(EventHandler<ActionEvent> handler) {
		muteItem.setOnAction(handler);
	}

	public void addClearScenarioHandler(EventHandler<ActionEvent> handler) {
		clearScenarioItem.setOnAction(handler);
	}

	public void addExitHandler(EventHandler<ActionEvent> handler) {
		exitItem.setOnAction(handler);
	}
	public MenuItem getExitItem(){
		return exitItem;
	}

	public void setMuteItemName(String text) {
		muteItem.setText(text);
	}
}
