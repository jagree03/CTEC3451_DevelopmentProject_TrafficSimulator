package model;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficLight {
    private String currentSignal;

    private boolean activated;

    private HBox h;
    private Image sprite;
    private ImageView viewSprite;


    // default constructor
    public TrafficLight() {
        this.sprite = new Image("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        this.viewSprite = new ImageView(this.sprite);
        this.currentSignal = "none";
        this.activated = false;
    }

    // Default constructor 2
    public TrafficLight(ImageView imgv) {
        this.viewSprite = imgv;
        this.sprite = this.getImageView().getImage();
        this.currentSignal = "none";
        this.activated = false;
    }

    public TrafficLight(int redSignalHoldTime, int amberSignalHoldTime, int greenSignalHoldTime, int timeForTransition) {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        this.sprite = new Image(file.toURI().toString());
        this.viewSprite = new ImageView(this.sprite);
        this.activated = false;
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

    public Boolean getActivatedState() {
        return activated;
    }

    public void setActivatedState(boolean value) {
        this.activated = value;
    }

    public void activateRedStopSignal() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight_RedStop.png");
        this.getImageView().setImage(new Image(file.toURI().toString()));
        this.setCurrentSignal("red");
    }

    public void activateAmberStopSignal() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight_YellowWait.png");
        this.getImageView().setImage(new Image(file.toURI().toString()));
        this.setCurrentSignal("amber");
    }

    public void activateGreenStopSignal() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight_GreenGo.png");
        this.getImageView().setImage(new Image(file.toURI().toString()));
        this.setCurrentSignal("green");
    }

    public void disableTrafficLight() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        this.getImageView().setImage(new Image(file.toURI().toString()));
        this.setCurrentSignal("none");
    }

    /**
     * using ScheduledExecutorService class, you can run "Runnables" that contain code at fixed period of times
     * or you can run tasks after a delay. This is helpful to activate a traffic light and control its transitions between
     * signals.
     * @param value Boolean value: True or false, true to activate the traffic light, false to disable
     * @throws InterruptedException
     */
    public void activateTrafficLight(boolean value) throws InterruptedException {
        ScheduledExecutorService ses1 = Executors.newScheduledThreadPool(1);
        if (value) {
            Runnable activation = () -> {
                ScheduledExecutorService ses2 = Executors.newScheduledThreadPool(1);

                Runnable task1 = () -> {
                    this.activateRedStopSignal();
                };

                Runnable task2 = () -> {
                    this.activateAmberStopSignal();
                };

                Runnable task3 = () -> {
                    this.activateGreenStopSignal();
                };

                ses2.schedule(task1, 0, TimeUnit.SECONDS); // red
                ses2.schedule(task2, 10, TimeUnit.SECONDS); // amber
                ses2.schedule(task3, 15, TimeUnit.SECONDS); // green
                ses2.schedule(task2, 25, TimeUnit.SECONDS); // amber
                ses2.schedule(task1, 28, TimeUnit.SECONDS); // red

                ses2.shutdown();
            };
            ses1.scheduleAtFixedRate(activation, 0, 32, TimeUnit.SECONDS);
        } else {
            this.setActivatedState(false);
        }
    }

    @Override
    public String toString() {
        return "TrafficLight:[currentSignal=" + currentSignal + ", viewSprite=" + viewSprite.getImage().getUrl() + "]";
    }
}
