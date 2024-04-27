package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PetrolStation {
    // data members
    private String name;
    private double totalFuel;
    private double pricePerLitre;
    private double sales;

    private boolean outOfFuel;
    private ImageView img;


    // default constructor
    public PetrolStation() {
        this.name = "Shell";
        this.totalFuel = 20.0;
        this.pricePerLitre = 1.75;
        this.sales = 0.0;
        this.outOfFuel = false;
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
        if (newTotalFuel <= 0.0) {
            this.totalFuel = 0.0;
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

    public ImageView getImageView() {
        return img;
    }

    public void setImageView(ImageView img) {
        this.img = img;
    }

    public void setImage(Image image) {
        this.getImageView().setImage(image);
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
                ", sales=" + sales + ", img=" + img.getImage().getUrl();
    }

}
