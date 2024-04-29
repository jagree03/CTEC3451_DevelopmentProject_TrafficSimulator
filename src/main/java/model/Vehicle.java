package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Random;

public class Vehicle {
    private String type; // type of the vehicle 'car' or 'van'
    private String color; // colour of the vehicle
    private ImageView sprite; // the sprite of the vehicle (image)
    private Double fuelLevel; // vehicle's fuel level.

    /**
     * Default constructor, creates a vehicle object that is a red car with fuel level 5.0 litres.
     */
    public Vehicle() {
        File red_car = new File("img\\vehicles\\car\\car_red.png");
        this.type = "car";
        this.sprite = new ImageView(new Image(red_car.toURI().toString())); // red car default and creates ImageView instance to store the sprite
        this.sprite.setFitWidth(25);
        this.sprite.setFitHeight(25);
        this.sprite.setPreserveRatio(true);
        this.setColor("Red");
        this.fuelLevel = 5.00;
    }

    /**
     * Custom Constructor #1 which accepts vehicle type, color and fuel level
     * @param type type of vehicle; car, van
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
     * @param type type of vehicle; car, van
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
     * For statistics controller, create a Vehicle Instance by passing all data members as parameters.
     */
    public Vehicle(String type, String ImageURI, String colour, Double fuelLevel) {
        this.type = type;
        this.sprite = new ImageView();
        this.setSprite(new Image(ImageURI));
        this.color = colour;
        this.fuelLevel = fuelLevel;
    }

    /**
     * This method sets the type of the vehicle, based on an integer value.
     * There are 2 types: car, van.
     * @param val 0 for car, 1 for van.
     */
    public void setType(int val) {
        if (val == 0) {
            this.type = "car";
        } else if (val == 1) {
            this.type = "van";
            this.setColor("grey");
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

    /**
     * Gets the type of the vehicle
     * @return String value representing the type of the vehicle: 'car' or 'van'
     */
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

    /**
     * Fetches a random colour from the list of available colours.
     * @return A string value representing the random colour.
     */
    public String getRandomColour() {
        String[] colors = {"Red", "Black", "Purple", "Green", "Orange", "Blue", "Yellow", "Grey"};
        Random rand = new Random();
        int randomVal = rand.nextInt(0, colors.length);
        return colors[randomVal];
    }

    /**
     * Get the vehicle colour
     * @return String value representing the colour
     */
    public String getColor() {
        return color;
    }

    /**
     * Set the vehicle's sprite/image
     * @param sprite Image
     */
    public void setSprite(Image sprite) {
        this.sprite.setImage(sprite);
    }

    /**
     * Get the vehicle's sprite/image
     * @return Image
     */
    public Image getSprite() {
        return this.getSpriteImageView().getImage();
    }

    /**
     * Get the ImageView of the vehicle
     * @return ImageView
     */
    public ImageView getSpriteImageView() {
        return sprite;
    }

    /**
     * Set the fuel level of the vehicle to the passed in value
     * If fuel level is less than or equal to 0 then fuel level is 0.
     * @param fuelLevel Double value
     */
    public void setFuelLevel(Double fuelLevel){
        if (fuelLevel <= 0.00) {
            fuelLevel = 0.00;
        }
        this.fuelLevel = fuelLevel;
    }

    /**
     * Get the fuel level of the vehicle
     * @return Double value
     */
    public Double getFuelLevel() {
        return fuelLevel;
    }

    /**
     * ToString method, indicates the state of each of the data members of the Vehicle class
     * @return String representing states of the key variables of a Vehicle.
     */
    @Override
    public String toString() {
        return "Vehicle:[type=" + type + ", color=" + color + ", ImageView(sprite)_URI=" + sprite.getImage().getUrl() + ", fuelLevel=" + fuelLevel + "]";
    }
}
