package gridwars;

import gridwars.buildings.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile {

    // Variables

    public boolean builtOn = false;
    public boolean builderOn = false;

    public ImageView image = new ImageView("gridwars/resources/assets/Grey Tile.png");

    public int yellowPower = 0;
    public int purplePower = 0;
    public Building building;



    /* -----------------------------------------------------------------------------------------------------------------
    Builds a building on the tile. Sets the builtOn attribute to true so that another building cannot be built there.
    ----------------------------------------------------------------------------------------------------------------- */
    public void build(Building building) {
        this.building = building;
        builtOn = true;
        GridWars.buildings.add(building);
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Sets the colour of the tile based on the overal power on the tile. It first calculates a single number and compares
    that number to 0.
    ----------------------------------------------------------------------------------------------------------------- */
    public void updateColour() {
        int total = yellowPower - purplePower;

        if (total == 0) {
            image.setImage(new Image("gridwars/resources/assets/Grey Tile.png"));
        }

        else if (total > 0) {
            image.setImage(new Image("gridwars/resources/assets/Yellow Tile.png"));
        }

        else {
            image.setImage(new Image("gridwars/resources/assets/Purple Tile.png"));
        }
    }
}
