package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class TrafficSimulatorRootMenuBottomButtonPanel extends HBox {
    private Button Exit;

    /**
     * Default constructor
     */
    public TrafficSimulatorRootMenuBottomButtonPanel() {
        Exit = new Button("Exit");
        Exit.setPrefSize(80, 20);
        this.setSpacing(15);
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.getChildren().addAll(Exit);
    }

    /**
     * Add an event handler to the exit button in the bottom button panel of the main menu, when clicked - exit the app.
     * @param handler EventHandler<ActionEvent>
     */
    public void addExitHandler(EventHandler<ActionEvent> handler) {
        Exit.setOnAction(handler);
    }
}
