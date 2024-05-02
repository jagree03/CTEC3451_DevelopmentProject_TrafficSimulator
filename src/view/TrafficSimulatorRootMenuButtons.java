package view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class TrafficSimulatorRootMenuButtons extends VBox {
    private Image imageGoToEditor;
    private Image imageGoToProcedural;
    private ImageView showEditor;
    private ImageView showProcedural;

    private Label lblEditor;
    private Label lblProcedural;

    /**
     * Default constructor
     */
    public TrafficSimulatorRootMenuButtons() {

        File editorLogo = new File("img\\1_MenuScreen\\EditorLogo.png");
        File proceduralLogo = new File("img\\1_MenuScreen\\ProceduralLogo.png");
        imageGoToEditor = new Image(editorLogo.toURI().toString());
        imageGoToProcedural = new Image(proceduralLogo.toURI().toString());

        showEditor = new ImageView(imageGoToEditor);
        showEditor.setFitWidth(200);
        showEditor.setFitHeight(300);
        showEditor.setPreserveRatio(true);

        showProcedural = new ImageView(imageGoToProcedural);
        showProcedural.setFitWidth(200);
        showProcedural.setFitHeight(300);
        showProcedural.setPreserveRatio(true);

        HBox imagesOfFeatures = new HBox(80);
        imagesOfFeatures.setAlignment(Pos.CENTER);
        imagesOfFeatures.getChildren().addAll(showEditor, showProcedural);

        HBox namesOfFeatures = new HBox(60);
        lblEditor = new Label("Build and edit your own scenario");
        lblEditor.setPadding(new Insets(0,0,0,50));
        lblProcedural = new Label("Use a random scenario via procedural generation");
        lblProcedural.setPadding(new Insets(0,10,0,0));
        namesOfFeatures.setAlignment(Pos.CENTER);
        namesOfFeatures.getChildren().addAll(lblEditor, lblProcedural);


        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(imagesOfFeatures, namesOfFeatures);
    }

    public ImageView getShowEditor(){
        return showEditor;
    }

    public ImageView getShowProcedural() {
        return showProcedural;
    }

    /**
     * Add an event handler to the ImageView, so when it's clicked - go to the Environment Editor screen
     * @param handler EventHandler<MouseEvent>
     */
    public void addEditorOnClickHandler(EventHandler<MouseEvent> handler) {
        showEditor.setOnMouseClicked(handler);
    }

    /**
     * Add an event handler to the ImageView, so when it's clicked - go to the Procedural Generation screen
     * @param handler EventHandler<MouseEvent>
     */
    public void addProceduralOnClickHandler(EventHandler<MouseEvent> handler) {
        showProcedural.setOnMouseClicked(handler);
    }

    /**
     * Add an event handler to the ImageView, so when the mouse entered the imageview, apply an effect
     * @param handler EventHandler<MouseEvent>
     */
    public void addEditorMouseEnteredHandler(EventHandler<MouseEvent> handler) {
        showEditor.setOnMouseEntered(handler);
    }

    /**
     * Add an event handler to the ImageView, so when the mouse exited the imageview, disable the effect
     * @param handler EventHandler<MouseEvent>
     */
    public void addEditorMouseExitedHandler(EventHandler<MouseEvent> handler) {
        showEditor.setOnMouseExited(handler);
    }

    /**
     * Add an event handler to the ImageView, so when the mouse entered the imageview, apply an effect
     * @param handler EventHandler<MouseEvent>
     */
    public void addProceduralMouseEnteredHandler(EventHandler<MouseEvent> handler) {
        showProcedural.setOnMouseEntered(handler);
    }

    /**
     * Add an event handler to the ImageView, so when the mouse exited the imageview, disable the effect
     * @param handler EventHandler<MouseEvent>
     */
    public void addProceduralMouseExitedHandler(EventHandler<MouseEvent> handler) {
        showProcedural.setOnMouseExited(handler);
    }
}
