package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Vehicle {
    private String type;
    private String color;
    private ImageView sprite;
    private Double fuelLevel;

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
    }

    /**
     * This method sets the type of the vehicle, based on an integer value.
     * There are 3 types: car, van and bus.
     * @param val 0 for car, 1 for van, 2 for bus
     */
    public void setType(int val) {
        if (val == 0) {
            this.type = "car";
        } else if (val == 1) {
            this.type = "van";
            this.setColor("grey");
        } else if (val == 2) {
            this.type = "bus";
        }
    }

    /**
     * This method sets the vehicle instance to a random vehicle type based on the chances.
     * @param carSpawnChance A value of chance to spawn a car / for the type to be 'car'
     * @param vanSpawnChance A value of chance to spawn a van / for the type to be 'van'
     */
    public void setARandomType(double carSpawnChance, double vanSpawnChance) {
        var random = Math.random();
        if(random < carSpawnChance ) {
            //we hit the ( % of car spawn chance ) case.
            this.setType(0); // set type to car (0)
        } else if (random < vanSpawnChance && random > carSpawnChance) {
            // we hit the ( % of van spawn chance ) case.
            this.setType(1); // set type to van (1)
        }
    }

    public String getType() {
        return type;
    }


    /**
     * Set the car's colour, it will automatically update the sprite of the car to match the given colour.
     * NB will only work if the type is 'car', due to time constraints a van type vehicle only has a single colour 'Grey'.
     * @param color A string that represents a colour e.g. "Red", "Black", "Green".
     */
    public void setColor(String color) {
        this.color = color;
        File file;
        if (this.getType().equals("car")) {
            file = new File("img\\vehicles\\car\\car_" + this.getColor().toLowerCase() + ".png");
            this.setSprite(new Image(file.toURI().toString()));
        } else { // it's a van, with only one colour, grey.
            file = new File("img\\vehicles\\car\\van_grey.png");
            this.setSprite(new Image(file.toURI().toString()));
        }
    }

    public String getRandomColour() {
        String[] colors = {"Red", "Black", "Purple", "Green", "Orange", "Blue", "Yellow", "Grey"};
        Random rand = new Random();
        int randomVal = rand.nextInt(0, colors.length);
        return colors[randomVal];
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
        if (fuelLevel <= 0.0) {
            fuelLevel = 0.0;
        }
        this.fuelLevel = fuelLevel;
    }

    public Double getFuelLevel() {
        return fuelLevel;
    }

    @Override
    public String toString() {
        return "Vehicle:[type=" + type + ", color=" + color + ", sprite=" + sprite.getImage().getUrl() + ", fuelLevel=" + fuelLevel + "]";
    }
}
