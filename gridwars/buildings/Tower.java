package gridwars.buildings;

import gridwars.Builder;
import gridwars.GridWars;
import javafx.scene.image.ImageView;

/* ---------------------------------------------------------------------------------------------------------------------
The Tower class creates a tower object with all the details of a tower. Includes method for the special upgrade. Extends
the building class which all other buildings inherit from as well.
--------------------------------------------------------------------------------------------------------------------- */
public class Tower extends Building {

    public Tower(Builder player) {
        super(1000, 200, 3500, player);
        addControlledPoints();

        power = 1;
        imagePath = "gridwars/resources/assets/Buildings/tower lvl ";
        image = new ImageView(imagePath + "1.png");
    }

    @Override
    public void addControlledPoints() {
        controlledSquares.add(new Integer[] {x, y});
    }

    @Override
    public void activate() {
        SpecialUpgrades.TOWER(x, y, this);
    }

    @Override
    public void updateTiles() {
        GridWars.updateSpecificTilesPower(controlledSquares, this);
    }
}