package gridwars.buildings;

import gridwars.Builder;
import gridwars.GridWars;
import javafx.scene.image.ImageView;

/* ---------------------------------------------------------------------------------------------------------------------
The research lab class creates a research lab object with all the details of a research lab. Includes method for the special
upgrade. Extends the building class which all other buildings inherit from as well.
--------------------------------------------------------------------------------------------------------------------- */
public class ResearchLab extends Building {

    public ResearchLab(Builder player) {
        super(7500, 900, 20000, player);
        addControlledPoints();

        power = 3;
        imagePath = "gridwars/resources/assets/Buildings/research lab lvl ";
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

        if (GridWars.grid[y][x + 2] != null) {
            controlledSquares.add(new Integer[]{x + 2, y});
        }

        if (GridWars.grid[y][x - 2] != null) {
            controlledSquares.add(new Integer[]{x - 2, y});
        }

        if (GridWars.grid[y + 2][x] != null) {
            controlledSquares.add(new Integer[]{x, y + 2});
        }

        if (GridWars.grid[y - 2][x] != null) {
            controlledSquares.add(new Integer[]{x, y - 2});
        }
    }

    @Override
    public void activate() {
        SpecialUpgrades.RESEARCH_LAB(owner, this);
    }

    @Override
    public void updateTiles() {
        GridWars.updateSpecificTilesPower(controlledSquares, this);
    }
}
