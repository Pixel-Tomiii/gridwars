package gridwars.buildings;

import gridwars.Builder;
import gridwars.GridWars;
import javafx.scene.image.ImageView;

/* ---------------------------------------------------------------------------------------------------------------------
The factory class creates a factory object with all the details of a factory. Includes method for the special upgrade. Extends
the building class which all other buildings inherit from as well.
--------------------------------------------------------------------------------------------------------------------- */
public class Factory extends Building {

    public Factory(Builder player) {
        super(3500, 600, 12500, player);
        addControlledPoints();

        power = 2;
        imagePath = "gridwars/resources/assets/Buildings/factory lvl ";
        image = new ImageView(imagePath + "1.png");
    }

    @Override
    public void addControlledPoints() {
        controlledSquares.add(new Integer[] {x, y});

        if (GridWars.grid[y + 1][x + 1] != null) {
            controlledSquares.add(new Integer[]{x + 1, y + 1});
        }

        if (GridWars.grid[y - 1][x + 1] != null) {
            controlledSquares.add(new Integer[]{x + 1, y - 1});
        }

        if (GridWars.grid[y + 1][x - 1] != null) {
            controlledSquares.add(new Integer[]{x - 1, y + 1});
        }

        if (GridWars.grid[y - 1][x - 1] != null) {
            controlledSquares.add(new Integer[]{x - 1, y - 1});
        }
    }

    @Override
    public void activate() {
        SpecialUpgrades.FACTORY(owner, this);
    }

    @Override
    public void updateTiles() {
        GridWars.updateSpecificTilesPower(controlledSquares, this);
    }
}
