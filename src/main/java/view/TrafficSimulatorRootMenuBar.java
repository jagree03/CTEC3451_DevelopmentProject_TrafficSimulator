package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;


public class TrafficSimulatorRootMenuBar extends MenuBar {

	private MenuItem aboutItem, exitItem;

	/**
	 * Default constructor
	 */
	public TrafficSimulatorRootMenuBar() {

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

		this.getMenus().add(menu); 
	}

	/**
	 * Add an event handler to the about menu bar item, so when it's clicked it will show info about the app.
	 * @param handler EventHandler<ActionEvent>
	 */
	public void addAboutHandler(EventHandler<ActionEvent> handler) {
		aboutItem.setOnAction(handler);
	}

	/**
	 * Add an event handler to the exit menu bar item, so when it's clicked, it will exit the app.
	 * @param handler EventHandler<ActionEvent>
	 */
	public void addExitHandler(EventHandler<ActionEvent> handler) {
		exitItem.setOnAction(handler);
	}

	/**
	 * Return the exit menu item
	 * @return MenuItem
	 */
	public MenuItem getExitItem(){
		return exitItem;
	}

}
