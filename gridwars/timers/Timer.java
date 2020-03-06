package gridwars.timers;

import gridwars.GridWars;
import javafx.animation.AnimationTimer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Timer {

    // Variables.
    private int timeRemaining; // How long is left of the game.
    public static final long SECOND = 1000000000; // 1 second.
    private long lastSecond = 0; // The time at which the last second happened.
    public Text strTime = new Text(); // A text node used to display the time.
    public AnimationTimer timer;

    // Constructor.
    public Timer(int time) {

        timeRemaining = time;
        strTime.setTextAlignment(TextAlignment.CENTER);
        strTime.setX((GridWars.WIDTH / 2) - (0.0475 * GridWars.WIDTH));
        strTime.setY((GridWars.WIDTH / 2) + (0.3 * GridWars.tileHeight));
        strTime.setFill(javafx.scene.paint.Color.WHITE);
        strTime.setFont(Font.font("tw cen mt", FontWeight.BOLD, 0.9 * GridWars.tileHeight));

        setupTimer();
        update();

    }



    public void setupTimer() {
        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now > lastSecond + SECOND) {
                    update();
                    timeRemaining -= 1;

                    lastSecond = now;
                }
            }
        };
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Updates the text on the timer every second.
    ----------------------------------------------------------------------------------------------------------------- */
    public void update() {

        String minutes = Integer.toString(timeRemaining / 60);
        String seconds = Integer.toString(timeRemaining % 60);

        if (minutes.length() != 2) {
            minutes = "0" + minutes;
        }

        if (seconds.length() != 2) {
            seconds = "0" + seconds;
        }

        String format = minutes + ":" + seconds;

        strTime.setText(format);

    }

}
