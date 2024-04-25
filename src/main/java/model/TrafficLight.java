package model;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficLight {

    private int redSignalHoldTime;
    private int amberSignalHoldTime;
    private int greenSignalHoldTime;
    private int timeForTransition;
    private String currentSignal;

    private Image sprite;
    private ImageView viewSprite;


    // default constructor
    public TrafficLight() {
        this.sprite = new Image("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        this.viewSprite = new ImageView(this.sprite);
        // arbitrary values
        this.redSignalHoldTime = 15;
        this.amberSignalHoldTime = 4;
        this.greenSignalHoldTime = 15;
        this.timeForTransition = 3;
        this.currentSignal = "none";
    }

    public TrafficLight(int redSignalHoldTime, int amberSignalHoldTime, int greenSignalHoldTime, int timeForTransition) {
        this.sprite = new Image("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        this.viewSprite = new ImageView(this.sprite);
        this.redSignalHoldTime = redSignalHoldTime;
        this.amberSignalHoldTime = amberSignalHoldTime;
        this.greenSignalHoldTime = greenSignalHoldTime;
        this.timeForTransition = timeForTransition;
    }

    public int getRedSignalHoldTime() {
        return redSignalHoldTime;
    }

    public void setRedSignalHoldTime(int value) {
        this.redSignalHoldTime = value;
    }

    public int getAmberSignalHoldTime() {
        return amberSignalHoldTime;
    }

    public void setAmberSignalHoldTime(int value) {
        this.amberSignalHoldTime = value;
    }

    public int getGreenSignalHoldTime() {
        return greenSignalHoldTime;
    }

    public void setGreenSignalHoldTime(int value) {
        this.greenSignalHoldTime = value;
    }

    public int getTimeForTransition() {
        return timeForTransition;
    }

    public void setTimeForTransition(int value) {
        this.timeForTransition = value;
    }

    public ImageView getImageView() {
        return viewSprite;
    }

    public String getCurrentSignal() {
        return currentSignal;
    }

    public void setCurrentSignal(String value) {
        this.currentSignal = value;
    }

    public void activateRedStopSignal() throws InterruptedException {
        Thread.sleep(timeForTransition * 1000L);
        this.getImageView().setImage(new Image("img\\2_EditorScreen\\trafficLight\\trafficLight_RedStop.png"));
        this.setCurrentSignal("red");
    }

    public void activateAmberStopSignal() throws InterruptedException {
        Thread.sleep(timeForTransition * 1000L);
        this.getImageView().setImage(new Image("img\\2_EditorScreen\\trafficLight\\trafficLight_YellowWait.png"));
        this.setCurrentSignal("amber");
    }

    public void activateGreenStopSignal() throws InterruptedException {
        Thread.sleep(timeForTransition * 1000L);
        this.getImageView().setImage(new Image("img\\2_EditorScreen\\trafficLight\\trafficLight_GreenGo.png"));
        this.setCurrentSignal("green");
    }

    public void disableTrafficLight() {
        this.getImageView().setImage(new Image("img\\2_EditorScreen\\trafficLight\\trafficLight.png"));
        this.setCurrentSignal("none");
    }

    public void activateTrafficLight() throws InterruptedException {
        activateRedStopSignal();
        Thread.sleep((redSignalHoldTime + timeForTransition) * 1000L);
        activateAmberStopSignal();
        Thread.sleep((amberSignalHoldTime + timeForTransition) * 1000L);
        activateGreenStopSignal();
        Thread.sleep((greenSignalHoldTime + timeForTransition) * 1000L);
    }

    @Override
    public String toString() {
        return "TrafficLight:[redSignalHoldTime=" + redSignalHoldTime + ", amberSignalHoldTime=" + amberSignalHoldTime +
                ", greenSignalHoldTime=" + amberSignalHoldTime + ", timeForTransition=" + timeForTransition +
                ", currentSignal=" + currentSignal + ", viewSprite=" + viewSprite.getImage().getUrl();
    }
}
