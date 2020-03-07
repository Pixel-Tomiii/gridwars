package gridwars.buildings;

import gridwars.Builder;
import gridwars.GridWars;
import javafx.scene.image.ImageView;

/* ---------------------------------------------------------------------------------------------------------------------
The barracks class creates a barracks object with all the details of a barracks. Includes method for the special upgrade. Extends
the building class which all other buildings inherit from as well.
--------------------------------------------------------------------------------------------------------------------- */
public class Barracks extends Building {

    public Barracks(Builder player) {
        super(5500, 800, 14000, player);
        addControlledPoints();

        power = 2;
        imagePath = "gridwars/resources/assets/Buildings/barracks lvl ";
        image = new ImageView(imagePath + "1.png");
    }

    @Override
    public void addControlledPoints() {
        for (int posy = y - 1; posy <= y + 1; posy++) {
            for (int posx = x - 1; posx <= x + 1; posx++) {
                if (GridWars.grid[posy][posx] != null) {
                    controlledSquares.add(new Integer[]{posx, posy});
                }
            }
        }
    }

    @Override
    public void activate() {
        SpecialUpgrades.BARRACKS(owner, this);
    }

    @Override
    public void updateTiles() {
        GridWars.updateSpecificTilesPower(controlledSquares, this);
    }
}
