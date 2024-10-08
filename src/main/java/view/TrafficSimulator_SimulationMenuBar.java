package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;


public class TrafficSimulator_SimulationMenuBar extends MenuBar {

	private MenuItem aboutItem, exitItem, displayBounding;

	/**
	 * Default Constructor
	 */
	public TrafficSimulator_SimulationMenuBar() {

		//temp var for menus and menu items within this MenuBar
		Menu menu;

		menu = new Menu("_File");

		menu.getItems().add( new SeparatorMenuItem());

		exitItem = new MenuItem("E_xit");
		exitItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+X"));
		menu.getItems().add(exitItem);

		this.getMenus().add(menu);   


		menu = new Menu("_Help");

		aboutItem = new MenuItem("_About");
		aboutItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+A"));
		menu.getItems().add(aboutItem);

		displayBounding = new MenuItem("_Display Driver Boundaries");
		displayBounding.setAccelerator(KeyCombination.keyCombination("SHORTCUT+B"));
		menu.getItems().add(displayBounding);

		this.getMenus().add(menu); 
	}

	//these methods allow handlers to be externally attached to this menubar and used by the controller

	/**
	 * Add event handler to the about menu bar item, so it displays the author information.
	 * @param handler EventHandler<ActionEvent>
	 */
	public void addAboutHandler(EventHandler<ActionEvent> handler) {
		aboutItem.setOnAction(handler);
	}

	/**
	 * Add the event handler to the display boundaries menu bar item so it displays the driver's boundaries (HBox borders).
	 * @param handler EventHandler<ActionEvent>
	 */
	public void addDisplayBoundingHandler(EventHandler<ActionEvent> handler) {
		displayBounding.setOnAction(handler);
	}

	/**
	 * Add event handler to the exit item in the menu bar, so when its clicked, exit the app.
	 * @param handler EventHandler<ActionEvent>
	 */
	public void addExitHandler(EventHandler<ActionEvent> handler) {
		exitItem.setOnAction(handler);
	}
	public MenuItem getExitItem(){
		return exitItem;
	}

	/**
	 * Sets the Display Boundaries menu item name
	 * @param text String value
	 */
	public void setDisplayBoundingMenuItemName(String text) {
		displayBounding.setText(text);
	}

	/**
	 * Gets the Display Boundaries menu item name
	 * @return String value representing the text of the menu item name
	 */
	public String getDisplayBoundingMenuItemName() {
		return displayBounding.getText();
	}

}
