package gridwars.buildings;

import gridwars.Builder;
import gridwars.GridWars;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/* ---------------------------------------------------------------------------------------------------------------------
The Bunker class creates a bunker object with all the details of a bunker. Includes method for the special upgrade.
Extends the building class which all other buildings inherit from as well.
--------------------------------------------------------------------------------------------------------------------- */
public class Bunker extends Building {

    public ArrayList<Building> adjacentBuildings = new ArrayList<Building>(0);

    public Bunker(Builder player) {
        super(8000, 900, 50000, player);
        addControlledPoints();

        power = 3;
        imagePath = "gridwars/resources/assets/Buildings/bunker lvl ";
        image = new ImageView(imagePath + "1.png");
    }

    @Override
    public void addControlledPoints() {
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
        if (GridWars.grid[y + 1][x] != null) {
            if (GridWars.grid[y + 1][x].building != null) {
                if (!(GridWars.grid[y + 1][x].building instanceof Bunker)) {
                    adjacentBuildings.add(GridWars.grid[y + 1][x].building);
                }
            }
        }

        if (GridWars.grid[y - 1][x] != null) {
            if (GridWars.grid[y - 1][x].building != null) {
                if (!(GridWars.grid[y - 1][x].building instanceof Bunker)) {
                    adjacentBuildings.add(GridWars.grid[y - 1][x].building);
                }
            }
        }

        if (GridWars.grid[y][x + 1] != null) {
            if (GridWars.grid[y][x + 1].building != null) {
                if (!(GridWars.grid[y][x + 1].building instanceof Bunker)) {
                    adjacentBuildings.add(GridWars.grid[y][x + 1].building);
                }
            }
        }

        if (GridWars.grid[y][x - 1] != null) {
            if (GridWars.grid[y][x - 1].building != null) {
                if (!(GridWars.grid[y][x - 1].building instanceof Bunker)) {
                    adjacentBuildings.add(GridWars.grid[y][x - 1].building);
                }
            }
        }

        SpecialUpgrades.BUNKER(this);
    }

    @Override
    public void updateTiles() {
        GridWars.updateSpecificTilesPower(controlledSquares, this);
    }
}
