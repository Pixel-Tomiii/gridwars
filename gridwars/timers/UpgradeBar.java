package gridwars.timers;

import gridwars.Builder;
import gridwars.GridWars;
import gridwars.buildings.Building;
import javafx.animation.AnimationTimer;

public class UpgradeBar extends BuildingBar {

    private boolean activate;

    public UpgradeBar(Building building, Builder player, boolean activate) {
        super(building, player);
        this.activate = activate;

        timer.stop();
        setupTimer();
        timer.start();
    }

    /* -----------------------------------------------------------------------------------------------------------------
    Sets up the timer used to regulate the speed at which a building is built/upgraded. There are 6 frames to the
    building animation at the end of which the building stops. The outcome of what happens to the building is based on
    if the building is being upgraded or not.
    ----------------------------------------------------------------------------------------------------------------- */
    private void setupTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastUpdate + buildSpeedInterval) {

                    // Stopping the loop.
                    if (frame == 6) {

                        if (activate) {
                            if (building.level >= 5) {
                                building.activate();
                            }
                        }

                        else {
                            building.upgrade();
                            building.updateTiles();
                        }

                        endTimer(this);

                    } else {
                        image.setImage(GridWars.buildbarImages[frame]);
                        frame++;

                        lastUpdate = now;
                    }
                }
            }
        };
    }
}
