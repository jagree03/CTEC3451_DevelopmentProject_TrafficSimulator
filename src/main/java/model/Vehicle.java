package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Vehicle {
    private String type;
    private String color;
    private ImageView sprite;
    private Double fuelLevel;
    private Boolean indicator_L;
    private Boolean indicator_R;

    /**
     * default constructor, creates a vehicle object that is red car with fuel level 5.0 litres and all indicators off
     */
    public Vehicle() {
        File red_car = new File("img\\vehicles\\car\\car_red.png");
        this.type = "car";
        this.sprite = new ImageView(new Image(red_car.toURI().toString())); // red car default and creates ImageView instance to store the sprite
        this.sprite.setFitWidth(25);
        this.sprite.setFitHeight(25);
        this.sprite.setPreserveRatio(true);
        this.setColor("Red");
        this.fuelLevel = 5.0;
        this.indicator_L = false;
        this.indicator_R = false;
    }

    /**
     * Custom Constructor #1 which accepts vehicle type, color and fuel level
     * @param type type of vehicle; car, van or bus
     * @param color color of vehicle
     * @param fuelLevel fuel level of vehicle
     */
    public Vehicle(String type, String color, Double fuelLevel) {
        this.type = type;
        this.sprite = new ImageView(); // create an ImageView instance for storing sprites.
        this.setColor(color); // setting the colour, sets the sprite
        this.sprite.setFitWidth(25);
        this.sprite.setFitHeight(25);
        this.sprite.setPreserveRatio(true);
        this.fuelLevel = fuelLevel;
        this.indicator_L = false;
        this.indicator_R = false;
    }

    /**
     * Custom Constructor #2 for generating random coloured vehicles
     * @param type type of vehicle; car, van or bus
     * @param fuelLevel fuel level of vehicle
     */
    public Vehicle(String type, Double fuelLevel) {
        this.type = type;
        this.sprite = new ImageView();
        this.setColor(this.getRandomColour());
        this.sprite.setFitWidth(25);
        this.sprite.setFitHeight(25);
        this.sprite.setPreserveRatio(true);
        this.fuelLevel = fuelLevel;
        this.indicator_L = false;
        this.indicator_R = false;
    }

    public String getType() {
        return type;
    }

    public void setColor(String color) {
        this.color = color;
        File file;
        if (this.getType() == "car") {
            file = new File("img\\vehicles\\car\\car_" + this.getColor().toLowerCase() + ".png");
            this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
        } else { // it's a van, with only one colour, grey.
            file = new File("img\\vehicles\\car\\van_grey.png");
            this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
        }
    }

    public String getRandomColour() {
        String[] colors = {"Red", "Black", "Purple", "Green", "Orange", "Blue", "Yellow", "Grey"};
        List<String> list = new ArrayList<>(Arrays.asList(colors));
        Collections.shuffle(list);
        return list.get(0);
    }

    public String getColor() {
        return color;
    }

    public void setSprite(Image sprite) {
        this.sprite.setImage(sprite);
    }

    public Image getSprite() {
        return this.getSpriteImageView().getImage();
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
        File file;
        if (this.getLeftIndicatorStatus()) { // if left indicator is on
            if (this.getType().equals("van")) {
                file = new File("img\\vehicles\\car\\van_grey_leftIndicatorOn.png");
                this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
            } else { // type is a car
                file = new File("img\\vehicles\\car\\car_" + this.getColor().toLowerCase() + "_leftIndicatorOn.png");
                this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
            }
        } else { // if Left indicator is off
            if (this.getType().equals("van")) {
                file = new File("img\\vehicles\\car\\van_grey.png");
                this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
            } else { // type is a car
                file = new File("img\\vehicles\\car\\car_" + this.getColor().toLowerCase() + ".png");
                this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
            }
        }
    }

    public boolean getLeftIndicatorStatus() {
        return indicator_L;
    }

    public void setRightIndicatorStatus(Boolean status) {
        this.indicator_R = status;
        File file;
        if (this.getRightIndicatorStatus()) { // if right indicator is on
            if (this.getType().equals("van")) {
                file = new File("img\\vehicles\\car\\van_grey_rightIndicatorOn.png");
                this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
            } else { // type is a car
                file = new File("img\\vehicles\\car\\car_" + this.getColor().toLowerCase() + "_rightIndicatorOn.png");
                this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
            }
        } else { // if right indicator is off
            if (this.getType().equals("van")) {
                file = new File("img\\vehicles\\car\\van_grey.png");
                this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
            } else { // type is a car
                file = new File("img\\vehicles\\car\\car_" + this.getColor().toLowerCase() + ".png");
                this.setSprite(new Image(String.valueOf(file.getAbsoluteFile().toURI())));
            }
        }
    }

    public boolean getRightIndicatorStatus() {
        return indicator_R;
    }

    @Override
    public String toString() {
        return "Vehicle:[Color=" + color + ", Sprite=" + sprite.getImage().getUrl() + ", fuelLevel=" + fuelLevel + ", indicator_L=" + indicator_L + ", indicator_R=" + indicator_R +"]";
    }
}
