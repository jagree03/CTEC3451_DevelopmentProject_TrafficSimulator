package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class TrafficSimulatorRootMenuBottomButtonPanel extends HBox {

    private Button Options;
    private Button Exit;

    public TrafficSimulatorRootMenuBottomButtonPanel() {
        Options = new Button("Options");
        Exit = new Button("Exit");
        this.setSpacing(15);
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.getChildren().addAll(Options, Exit);
    }

    public void addExitHandler(EventHandler<ActionEvent> handler) {
        Exit.setOnAction(handler);
    }
}
