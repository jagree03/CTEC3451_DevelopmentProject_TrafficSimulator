package view;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;

public class TrafficSimulator_StatisticsDropDownMenu extends HBox {

    private ComboBox cb;

    private ImageView icon;

    /**
     * Default constructor
     */
    public TrafficSimulator_StatisticsDropDownMenu() {
        this.setSpacing(5);

        cb = new ComboBox();
        cb.setPadding(new Insets(5,10,0,0));

        icon = new ImageView(new WritableImage(40, 40));
        icon.setFitWidth(15);
        icon.setFitHeight(15);
        icon.setPreserveRatio(true);

        this.getChildren().addAll(cb, icon);
    }

    /**
     * Get the selected item from the combobox
     * @return the selected item as Object
     */
    public Object getSelectedItem() {
        return cb.getSelectionModel().getSelectedItem();
    }

    /**
     * Add a ChangeListener to the combobox that detects when the item of the combobox is changed by the user
     * @param listener ChangeListener<String>
     */
    public void addComboBoxListener(ChangeListener<String> listener) {
        cb.valueProperty().addListener(listener);
    }

    /**
     * Modify the observablelist (Combobox items) based on the passed in list of strings
     * @param strings ObservableList of strings e.g. "Driver 1, Driver 2, Petrol Station 1".
     */
    public void modifyComboBoxItems(ObservableList<String> strings) {
        cb.setItems(strings);
        cb.getSelectionModel().select(0); // by default select the first item.
        cb.setPrefSize(165,25);
    }

    /**
     * Set the image icon of the Driver or PetrolStation that has been selected in the combobox's selection model.
     * @param img Image to be the new icon
     */
    public void setIcon(Image img) {
        icon.setImage(img);
        icon.setPreserveRatio(true);
        icon.setFitWidth(40);
        icon.setFitHeight(40);
    }
}
