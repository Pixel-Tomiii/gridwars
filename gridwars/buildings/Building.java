package gridwars.buildings;

import gridwars.Builder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public abstract class Building {


    // Variables.
    public int level = 1;
    public boolean hasActivated = false;

    public final int COST;
    public final int UPGRADE_COST;
    public final int ACTIVATE_COST;

    public final Builder owner;
    public int x;
    public int y;

    public int power;
    public int oldPower = 0;
    public ArrayList<Integer[]> controlledSquares = new ArrayList<Integer[]>(0);

    public ImageView image;
    public ImageView aura;
    public String imagePath;


    // Constructor.
    public Building(int cost, int upgradeCost, int activateCost, Builder player) {
        COST = cost;
        UPGRADE_COST = upgradeCost;
        ACTIVATE_COST = activateCost;

        owner = player;
        x = player.x;
        y = player.y;
    }


    /* -----------------------------------------------------------------------------------------------------------------
    Updates the old power, the power and the image of the building to the next level.
    ----------------------------------------------------------------------------------------------------------------- */
    public void upgrade() {
        oldPower = power++;
        level++;
        if (level <= 5) {
            image.setImage(new Image(imagePath + Integer.toString(level) + ".png"));
        }
    }

    public abstract void addControlledPoints();
    public abstract void activate();
    public abstract void updateTiles();
}
