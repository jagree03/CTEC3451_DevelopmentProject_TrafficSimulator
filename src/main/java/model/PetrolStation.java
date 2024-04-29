package model;

import javafx.scene.image.Image;
import jfxtras.scene.control.ImageViewButton;

import java.util.UUID;

public class PetrolStation {
    // data members

    private UUID uniqueID; // unique ID for the Petrol Station
    private String name; // name of the petrol station
    private double totalFuel; // petrol station's total fuel in double
    private double pricePerLitre; // petrol stations price per litre value
    private double sales; // how much money the petrol station made, when one or more drivers used the petrol station for fuel, based on price per litre

    private boolean outOfFuel; // this represents if the petrol station is out of fuel or not, if yes then driver's can't refuel.
    private ImageViewButton imgb;


    /**
     * Default constructor for a petrol station object. Sets a default ID, name, totalFuel, PricePerLitre, sales and outOfFuel value.
     * @param imgb ImageViewButton, in reference to its placement in the scenario on the EditorPane (The GridPane)
     */
    public PetrolStation(ImageViewButton imgb) {
        this.imgb = imgb;
        this.uniqueID = UUID.randomUUID();
        this.name = "Shell";
        this.totalFuel = 20.00;
        this.pricePerLitre = 1.04;
        this.sales = 0.00;
        this.outOfFuel = false;
    }

    /**
     * Constructor for statistics
     * Can supply all PetrolStation data members as parameters.
     * @param uniqueID UUID unique id of the petrol station
     * @param name name of the petrol station
     * @param imageURI The image URI of the ImageViewButton
     * @param totalFuel double value to represent the total fuel
     * @param pricePerLitre double value to represent the price per litre
     * @param sales double value to represent the sales of the petrol station
     * @param outOfFuel boolean value to represent the outOfFuel status of the petrol station
     */
    public PetrolStation(UUID uniqueID, String name, String imageURI, double totalFuel, double pricePerLitre, double sales, boolean outOfFuel) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.imgb = new ImageViewButton();
        this.setImage(new Image(imageURI));
        this.setTotalFuel(totalFuel);
        this.setPricePerLitre(pricePerLitre);
        this.setSales(sales);
        this.setOutOfFuelValue(outOfFuel);
    }

    /**
     * Retrieves the unique identifier of the PetrolStation instance.
     * @return The UUID value
     */
    public UUID getUniqueID() {
        return uniqueID;
    }

    /**
     * Gets the name of the petrol station
     * @return String value
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the petrol station
     * @param name String value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the total fuel value of the petrol station
     * @return Double value
     */
    public double getTotalFuel() {
        return totalFuel;
    }

    /**
     * Sets the total fuel value of the petrol station
     * @param totalFuel Double value
     */
    public void setTotalFuel(double totalFuel) {
        this.totalFuel = totalFuel;
    }

    /**
     * This method decreases the totalFuel value by subtracting the passed in value from totalFuel.
     * If totalFuel is less than or equal to 0 then totalFuel is set to 0 and outOfFuel becomes true.
     * @param val Double value
     */
    public void reduceTotalFuel(double val) {
        double newTotalFuel = this.getTotalFuel() - val;
        if (newTotalFuel <= 0.00) {
            this.totalFuel = 0.00;
            this.setOutOfFuelValue(true);
        } else {
            this.totalFuel = newTotalFuel;
        }
    }

    /**
     * Gets the price per litre value of the petrol station
     * @return Double value
     */
    public double getPricePerLitre() {
        return pricePerLitre;
    }

    /**
     * Sets the price per litre value of the petrol station
     * @param pricePerLitre Double value
     */
    public void setPricePerLitre(double pricePerLitre) {
        this.pricePerLitre = pricePerLitre;
    }

    /**
     * Gets the sales value of the petrol station
     * @return Double value
     */
    public double getSales() {
        return sales;
    }

    public String getSalesInString() {
        return ("£ " + sales);
    }

    /**
     * Sets the sales value of the petrol station
     * @param sales Double value
     */
    public void setSales(double sales) {
        this.sales = sales;
    }

    /**
     * Adds the passed in value to the current sales value of the petrol station
     * @param val Double value
     */
    public void addSales(double val) {
        double newSales = this.getSales() + val;
        this.sales = newSales;
    }

    /**
     * Retrieve the ImageViewButton associated to the Petrol Station
     * @return ImageViewButton
     */
    public ImageViewButton getImageViewButton() {
        return imgb;
    }

    public void setImageViewButton(ImageViewButton imgb) {
        this.imgb = imgb;
    }

    /**
     * Sets the petrol station's image.
     * @param image Image instance
     */
    public void setImage(Image image) {
        this.getImageViewButton().setImage(image);
    }

    /**
     * Get the out of fuel boolean value for the petrol station
     * @return Boolean value
     */
    public boolean getOutOfFuelValue() {
        return outOfFuel;
    }

    /**
     * Set the out of fuel boolean value for the petrol station
     * @param val Boolean value, true for out of fuel, false for NOT out of fuel.
     */
    public void setOutOfFuelValue(boolean val) {
        this.outOfFuel = val;
    }

    /**
     * ToString method, indicates the state of each of the data members of the PetrolStation class
     * @return String representing states of the key variables of a PetrolStation.
     */
    @Override
    public String toString() {
        return "PetrolStation:[uniqueID=" + uniqueID + ", name=" + name + ", totalFuel=" + totalFuel
                + ", pricePerLitre=" + pricePerLitre + ", sales=" + sales + ", outOfFuel=" + outOfFuel
                + ", ImageViewButton(imgb)_URI=" + imgb.getImage().getUrl() + "]";
    }

}
