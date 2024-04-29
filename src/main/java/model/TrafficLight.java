package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficLight {
    private String currentSignal; // string representing the current signal of the traffic light

    private boolean activated; // boolean represents if traffic lights activated or not
    private Image sprite; // image of the traffic light
    private ImageView viewSprite;


    /**
     * Default constructor of a traffic light, sets a default image, current signal to none and activated to false
     */
    public TrafficLight() {
        this.sprite = new Image("img\\2_EditorScreen\\trafficLight\\trafficLight.png");
        this.viewSprite = new ImageView(this.sprite);
        this.currentSignal = "none";
        this.activated = false;
    }

    /**
     * Default constructor 2 of a traffic light, sets the Image to the passed in ImageView, currentSignal is none and activated is false.
     * @param imgv ImageView
     */
    public TrafficLight(ImageView imgv) {
        this.viewSprite = imgv;
        this.sprite = this.getImageView().getImage();
        this.currentSignal = "none";
        this.activated = false;
    }

    /**
     * Get the traffic light's image view.
     * @return ImageView
     */
    public ImageView getImageView() {
        return viewSprite;
    }

    /**
     * Get the current signal of the traffic light
     * @return String value (either 'none','red', 'amber' or 'green').
     */
    public String getCurrentSignal() {
        return currentSignal;
    }

    /**
     * Set the current signal of the traffic light
     * @param value String value of the traffic light, can be 'none', 'red', 'amber' or 'green'.
     */
    public void setCurrentSignal(String value) {
        this.currentSignal = value;
    }

    public Boolean getActivatedState() {
        return activated;
    }

    /**
     * Set the activated status of the Traffic Light based on the passed in Boolean value
     * @param value Boolean value, true to activate, false to not activate.
     */
    public void setActivatedState(boolean value) {
        this.activated = value;
    }

    /**
     * Activates the Traffic Light's red signal, modifies the sprite and updates the currentSignal to 'red'.
     */
    public void activateRedStopSignal() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight_RedStop.png");
        this.getImageView().setImage(new Image(file.toURI().toString()));
        this.setCurrentSignal("red");
    }

    /**
     * Activates the Traffic Light's amber signal, modifies the sprite and updates the currentSignal to 'amber'.
     */
    public void activateAmberStopSignal() {
        File file = new File("img\\2_EditorScreen\\trafficLight\\trafficLight_YellowWait.png");
        this.getImageView().setImage(new Image(file.toURI().toString()));
        this.setCurrentSignal("amber");
    }

    /**
     * Activates the Traffic Light's green signal, modifies the sprite and updates the currentSignal to 'green'.
     */
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
     * @throws InterruptedException Throw this exception if the ScheduledExecutorService is interleaved with another process.
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

    /**
     * ToString method, indicates the state of each of the data members of the TrafficLight class
     * @return String representing states of the key variables of a TrafficLight.
     */
    @Override
    public String toString() {
        return "TrafficLight:[currentSignal=" + currentSignal + ", activated=" + activated + ", ImageView(viewSprite)_URI=" + viewSprite.getImage().getUrl() + "]";
    }
}
