package gridwars.buildings;

import gridwars.Builder;
import gridwars.GridWars;
import javafx.scene.image.ImageView;

/* ---------------------------------------------------------------------------------------------------------------------
The MicrobotsLab class creates a MicrobotsLab object with all the details of a Microbot Lab. Includes method for the
special upgrade. Extends the building class which all other buildings inherit from as well.
--------------------------------------------------------------------------------------------------------------------- */
public class MicrobotsLab extends Building {

    public MicrobotsLab(Builder player) {
        super(12000, 1300, 15000, player);
        addControlledPoints();

        power = 4;
        imagePath = "gridwars/resources/assets/Buildings/microbots lab lvl ";
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

        for (int posy = y - 1; posy <= y + 1; posy++) {
            if (GridWars.grid[posy][x + 2] != null) {
                controlledSquares.add(new Integer[]{x + 2, posy});
            }
        }

        for (int posy = y - 1; posy <= y + 1; posy++) {
            if (GridWars.grid[posy][x - 2] != null) {
                controlledSquares.add(new Integer[]{x - 2, posy});
            }
        }

        for (int posx = x - 1; posx <= x + 1; posx++) {
            if (GridWars.grid[y + 2][posx] != null) {
                controlledSquares.add(new Integer[]{posx, y + 2});
            }
        }

        for (int posx = x - 1; posx <= x + 1; posx++) {
            if (GridWars.grid[y - 2][posx] != null) {
                controlledSquares.add(new Integer[]{posx, y - 2});
            }
        }
    }

    @Override
    public void activate() {
        SpecialUpgrades.MICROBOTS_LAB();
    }

    @Override
    public void updateTiles() {
        GridWars.updateSpecificTilesPower(controlledSquares, this);
    }
}
