package gridwars.buildings;

import gridwars.Builder;
import gridwars.GridWars;
import javafx.scene.image.ImageView;

/* ---------------------------------------------------------------------------------------------------------------------
The quarry class creates a quarry object with all the details of a quarry. Includes method for the special upgrade. Extends
the building class which all other buildings inherit from as well.
--------------------------------------------------------------------------------------------------------------------- */
public class Quarry extends Building {

    public Quarry(Builder player) {
        super(3000, 500, 7500, player);
        addControlledPoints();

        power = 1;
        imagePath = "gridwars/resources/assets/Buildings/quarry lvl ";
        image = new ImageView(imagePath + "1.png");
    }

    @Override
    public void addControlledPoints() {
        controlledSquares.add(new Integer[] {x, y});

        if (GridWars.grid[y][x + 1] != null) {
            controlledSquares.add(new Integer[]{x + 1, y});
        }

        if (GridWars.grid[y][x - 1] != null) {
            controlledSquares.add(new Integer[]{x - 1, y});
        }

        if (GridWars.grid[y + 1][x] != null) {
            controlledSquares.add(new Integer[]{x, y + 1});
        }

        if (GridWars.grid[y - 1][x] != null) {
            controlledSquares.add(new Integer[]{x, y - 1});
        }
    }

    @Override
    public void activate() {
        SpecialUpgrades.QUARRY(owner);
    }

    @Override
    public void updateTiles() {
        GridWars.updateSpecificTilesPower(controlledSquares, this);
    }
}
