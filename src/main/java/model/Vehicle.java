package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Vehicle {
    private String type;
    private String color;
    private ImageView sprite;
    private Double fuelLevel;
    private Boolean indicator_L;
    private Boolean indicator_R;

    public Vehicle() {
        File red_car = new File("img\\vehicles\\car\\car_red.png");
        this.type = "Coupe";
        this.color = "Red";
        this.sprite = new ImageView(new Image(red_car.toURI().toString())); // red car default
        this.sprite.setFitWidth(25

        );
        this.sprite.setFitHeight(25);
        this.sprite.setPreserveRatio(true);
        this.fuelLevel = 5.0;
        this.indicator_L = false;
        this.indicator_R = false;
    }

    public Vehicle(String type, String color, ImageView sprite, Double fuelLevel) {
        this.type = type;
        this.color = color;
        this.sprite = sprite;
        this.fuelLevel = fuelLevel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setSprite(Image sprite) {
        this.sprite.setImage(sprite);
    }

    public Image getSprite() {
        return sprite.getImage();
    }

    public ImageView getSpriteImageView() {
        return sprite;
    }

    public void setFuelLevel(Double fuelLevel){
        this.fuelLevel = fuelLevel;
    }

    public Double getFuelLevel() {
        return fuelLevel;
    }

    public void setLeftIndicatorStatus(Boolean status) {
        this.indicator_L = status;
    }

    public boolean getLeftIndicatorStatus() {
        return indicator_L;
    }

    public void setRightIndicatorStatus(Boolean status) {
        this.indicator_R = status;
    }

    public boolean getRightIndicatorStatus() {
        return indicator_R;
    }

    @Override
    public String toString() {
        return "Vehicle:[Type=" + type + ", Color=" + color + ", Sprite=" + sprite.getImage().getUrl() + ", fuelLevel=" + fuelLevel + ", indicator_L=" + indicator_L + ", indicator_R=" + indicator_R +"]";
    }
}
