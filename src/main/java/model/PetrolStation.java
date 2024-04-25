package model;

import javafx.scene.image.ImageView;

public class PetrolStation {
    // data members
    private String name;
    private double totalFuel;
    private double pricePerLitre;
    private double sales;
    private ImageView img;


    // default constructor
    public PetrolStation() {
        this.name = "Shell";
        this.totalFuel = 20.0;
        this.pricePerLitre = 1.75;
        this.sales = 0.0;
    }

    // custom constructor
    public PetrolStation(String name, double totalFuel, double pricePerLitre) {
        this.name = name;
        this.totalFuel = totalFuel;
        this.pricePerLitre = pricePerLitre;
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

    public ImageView getImageView() {
        return img;
    }

    public void setImageView(ImageView img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "PetrolStation:[name=" + name + ", totalFuel=" + totalFuel + ", pricePerLitre=" + pricePerLitre +
                ", sales=" + sales + ", img=" + img.getImage().getUrl();
    }

}
