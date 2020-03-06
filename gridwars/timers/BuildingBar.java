package gridwars.timers;

import gridwars.Builder;
import gridwars.GridWars;
import gridwars.buildings.Building;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/* ---------------------------------------------------------------------------------------------------------------------
The building bar is the loading bar which appears when the player is building or upgrading a building.
--------------------------------------------------------------------------------------------------------------------- */
public class BuildingBar {

    protected int frame = 0;

    protected ImageView image = new ImageView(new Image("gridwars/resources/assets/Animation/Building/0.png"));
    protected long buildSpeedInterval;
    protected long lastUpdate = 0;

    protected Building building;
    protected Builder player;

    protected AnimationTimer timer;


    // Constructor.
    public BuildingBar(Building building, Builder player) {
        this.buildSpeedInterval = (long) (player.BUILD_INTERVAL * player.buildSpeedMultiplier);
        this.player = player;
        this.building = building;

        player.isBuilding = true;
        player.image.setOpacity(0.4);

        image.setLayoutX(player.image.getLayoutX());
        image.setLayoutY(player.image.getLayoutY());
        image.setFitWidth(GridWars.tileWidth);
        image.setPreserveRatio(true);

        GridWars.display.getChildren().add(image);
        GridWars.scene.setRoot(GridWars.display);
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
                        GridWars.renderBuilding(building.image, player);
                        building.updateTiles();
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


    /* -----------------------------------------------------------------------------------------------------------------
    Stops the timer when it should end. It updates the tiles the building "controls", and then it stops the player from
    building and resets the opacity of the player image to 1. It then removes the build bar image from the scene and
    stops the timer.
    ----------------------------------------------------------------------------------------------------------------- */
    protected void endTimer(AnimationTimer timer) {
        player.isBuilding = false;
        player.image.setOpacity(1);

        GridWars.display.getChildren().remove(image);
        GridWars.scene.setRoot(GridWars.display);

        timer.stop();
    }
}
