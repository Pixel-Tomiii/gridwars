package gridwars.buildings;

import gridwars.Builder;
import gridwars.GridWars;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class SpecialUpgrades {

    /* -----------------------------------------------------------------------------------------------------------------
    The below code activates the ablitity for a tower. The tower's ability gives +1 power to adjacent buildings.
    There is a main method called TOWER which is what will be called but the private method updateBuilding will check
    to see if there is a tile and a building at the given coordinates an will then update the power on the building.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void TOWER(int x, int y, Tower tower) {
        updateBuilding(x + 1, y);
        updateBuilding(x - 1, y);
        updateBuilding(x, y + 1);
        updateBuilding(x, y - 1);

        generateOverlay(tower);
    }

    private static void updateBuilding(int x, int y) {
        if (GridWars.grid[y][x] != null) {
            if (GridWars.grid[y][x].building != null) {
                GridWars.grid[y][x].building.oldPower = GridWars.grid[y][x].building.power++;
                GridWars.grid[y][x].building.updateTiles();
            }
        }
    }



    /* -----------------------------------------------------------------------------------------------------------------
    The below code activates the ablitity for a quarry. The quarry's ability gives the player +10 money per money tick
    per activated quarry.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void QUARRY(Builder player, Quarry quarry) {
        player.rateMultiplier++;
        generateOverlay(quarry);
    }



    /* -----------------------------------------------------------------------------------------------------------------
    The below code activates the ablitity for a factory. The factory's ability allows the player to build 5% faster.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void FACTORY(Builder player, Factory factory) {
        player.buildSpeedMultiplier -= 0.05;
        generateOverlay(factory);
    }



    /* -----------------------------------------------------------------------------------------------------------------
    The below code activates the ablitity for a barracks. The barracks' ability allows the player to move 5% faster.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void BARRACKS(Builder player, Barracks barrack) {
        player.framesPerMove -= (int) (0.10 * player.framesPerMove);
        generateOverlay(barrack);
    }



    /* -----------------------------------------------------------------------------------------------------------------
    The below code activates the ablitity for a research lab. The research lab's ability allows the player to upgrade
    each building they own by an additional level, 1 per activated research lab. This does not affect the level at which
    a building can be activated. A building can still be activated from level 5 onwards.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void RESEARCH_LAB(Builder player, ResearchLab researchLab) {
        player.buildingLevelCap++;
        generateOverlay(researchLab);
    }



    /* -----------------------------------------------------------------------------------------------------------------
    The below code activates the ablitity for a bunker. The bunker's ability basically activates all nearby buildings
    for free, regardless of their level.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void BUNKER(Bunker bunker) {
        if (bunker.adjacentBuildings.size() > 0) {
            for (Building building : bunker.adjacentBuildings) {
                building.activate();
                building.hasActivated = true;
            }
        }

        generateOverlay(bunker);
    }



    /* -----------------------------------------------------------------------------------------------------------------
    The below code activates the ablitity for a microbots lab. The microbots lab's ability spawns a mini robot which
    upgrades the cheapest building to the next level for free. The time taken to upgrade the building is a lot longer
    but the speed is affected by the number of activated barracks.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void MICROBOTS_LAB(MicrobotsLab microbotLab) {
        generateOverlay(microbotLab);
        // TO DO...
        // Basically implement a microbot system with ai. Not a priority but needs doing at some point.
    }

    private static void generateOverlay(Building building) {
        ImageView overlay = new ImageView(new Image("gridwars/resources/assets/overlay.png"));

        overlay.setLayoutX(building.image.getLayoutX());
        overlay.setLayoutY(building.image.getLayoutY());

        GridWars.overlays.getChildren().add(overlay);
    }
}
