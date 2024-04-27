package view;

import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;

public class TrafficSimulator_StatisticsDropDownMenu extends HBox {

    private ComboBox cb;

    private ImageView icon;

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

    public Object getSelectedItem() {
        return cb.getSelectionModel().getSelectedItem();
    }

    public void addComboBoxListener(ChangeListener<String> listener) {
        cb.valueProperty().addListener(listener);
    }

    public void modifyComboBoxItems(ObservableList<String> strings) {
        cb.setItems(strings);
        cb.getSelectionModel().select(0); // by default select the first item.
        cb.setPrefSize(165,25);
    }

    public void setIcon(Image img) {
        icon.setImage(img);
        icon.setPreserveRatio(true);
        icon.setFitWidth(40);
        icon.setFitHeight(40);
    }
}
