package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jfxtras.scene.control.ImageViewButton;

import java.util.UUID;

public class PetrolStation {
    // data members

    private UUID uniqueID;
    private String name;
    private double totalFuel;
    private double pricePerLitre;
    private double sales;

    private boolean outOfFuel;
    private ImageViewButton imgb;


    // default constructor
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalFuel() {
        return totalFuel;
    }

    public void setTotalFuel(double totalFuel) {
        this.totalFuel = totalFuel;
    }

    public void reduceTotalFuel(double val) {
        double newTotalFuel = this.getTotalFuel() - val;
        if (newTotalFuel <= 0.00) {
            this.totalFuel = 0.00;
            this.setOutOfFuelValue(true);
        } else {
            this.totalFuel = newTotalFuel;
        }
    }

    public double getPricePerLitre() {
        return pricePerLitre;
    }

    public void setPricePerLitre(double pricePerLitre) {
        this.pricePerLitre = pricePerLitre;
    }

    public double getSales() {
        return sales;
    }

    public String getSalesInString() {
        return ("£ " + sales);
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public void addSales(double val) {
        double newSales = this.getSales() + val;
        this.sales = newSales;
    }

    public ImageViewButton getImageViewButton() {
        return imgb;
    }

    public void setImageViewButton(ImageViewButton imgb) {
        this.imgb = imgb;
    }

    public void setImage(Image image) {
        this.getImageViewButton().setImage(image);
    }

    public boolean getOutOfFuelValue() {
        return outOfFuel;
    }

    public void setOutOfFuelValue(boolean val) {
        this.outOfFuel = val;
    }

    @Override
    public String toString() {
        return "PetrolStation:[name=" + name + ", totalFuel=" + totalFuel + ", pricePerLitre=" + pricePerLitre +
                ", sales=" + sales + ", imgb=" + imgb.getImage().getUrl();
    }

}
