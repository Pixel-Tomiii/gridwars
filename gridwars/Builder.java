package gridwars;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/* ---------------------------------------------------------------------------------------------------------------------
The builder class handles everything builder related. This mostly includes movement but it also includes the image
which is displayed.
--------------------------------------------------------------------------------------------------------------------- */
public class Builder {

	// Movement.
	public AnimationTimer movement;
	private long speedInterval = 16666667; // The interval at which the player moves.

	public int acceleration = 4; // How fast the player will accelerate.
	public int maxVel = acceleration * 2;
	private long lastMove = 0; // The last time the player moved.
	public boolean isMoving = false; // Determines if the player is moving or not.

	private int baseXVel = GridWars.tileWidth / 16; // Pixels to move in the x direction.
	private int baseYVel = GridWars.tileHeigh / 16; // Pixels to move in the y direction.


	public long BUILD_INTERVAL = 1200000000;
	public long UPGRADE_INTERVAL = 800000000;
	public long MICROBOT_UPGRADE_INTERVAL = 3000000000L;
 
 	// Position on grid.
 	public int x = 0;
 	public int y = 0;

	// Directional Variables.
 	public boolean up = false;
 	public boolean down = false;
 	public boolean left = false;
 	public boolean right = false;

 	// Income.
	public AnimationTimer income;
 	private long incomeInterval = 500000000; // The interval at which the player gets money.
	private long lastIncome = 0; // The last time the player got money.
	public int balance = 900000; // Current balance of the player.
 	private int rate = 125; // Money gained per tick.
 	public int rateMultiplier = 0; // Increases the money gained per tick.
	public Text strBalance = new Text(); // Used to display the player's balance on the screen.

 	// Building.
	public AnimationTimer build;
 	public float buildSpeedMultiplier = 0.1f; // Increases the speed at which the player builds.
 	public boolean isBuilding = false; // Determins if the builder is building or not.
	public int buildingLevelCap = 5; // Determines how many times a building can be upgraded.

 	// Image.
 	public ImageView image = new ImageView();

 	public String id;


 	// Constructor.
	public Builder(String id) {

		this.id = id;

		setupPlayerMovement();
		setupIncome();
	}



	/* -----------------------------------------------------------------------------------------------------------------
    Sets up the thread which will move the player. The player can only move if they are not building/upgrading. They
    only move at set intervals so that they don't move too fast. It fetches the velocity every frame.
    ----------------------------------------------------------------------------------------------------------------- */
	private void setupPlayerMovement() {

  		movement = new AnimationTimer() {

  			@Override
			public void handle(long now) {

  				if (!isBuilding) {
  					if (now > lastMove + speedInterval) {
						if (isMoving) {
							if (left || right) {
								if (GridWars.grid[y][x].image.getLayoutX() + )
							}
						}

						else {
							willCollide();
						}
					}
				}
			}
		};
	}



	/* -----------------------------------------------------------------------------------------------------------------
    Sets up the thread which will give the player money regularily.
    ----------------------------------------------------------------------------------------------------------------- */
	private void setupIncome() {

		// Setting up the balance.
		strBalance.setY((GridWars.HEIGHT / 2) + (0.3 * GridWars.tileHeight));
		strBalance.setFill(javafx.scene.paint.Color.WHITE);
		strBalance.setFont(Font.font("tw cen mt", FontWeight.BOLD, 0.9 * GridWars.tileHeight));
		strBalance.setText(id + ": " + Integer.toString(balance));

		income = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (now > lastIncome + incomeInterval) {
					balance += rate + (50 * rateMultiplier);
					strBalance.setText(id + ": " + Integer.toString(balance));

					lastIncome = now;
				}
			}
		};
	}



	/* -----------------------------------------------------------------------------------------------------------------
	Checks to make sure the player can move in the direction they want to.
	----------------------------------------------------------------------------------------------------------------- */
	private boolean willCollide() {
		if (!isMoving) {
			if (up && y - 1 >= 0) {
				if (GridWars.grid[y - 1][x] != null) {
					y -= 1;
					isMoving = true;
					return false;
				}

			} else if (down && y + 1 < GridWars.GRID_HEIGHT) {
				if (GridWars.grid[y + 1][x] != null) {
					y += 1;
					isMoving = true;
					return false;
				}

			} else if (left && x - 1 >= 0) {
				if (GridWars.grid[y][x - 1] != null) {
					x -= 1;
					isMoving = true;
					return false;
				}

			} else if (right && x + 1 < GridWars.GRID_WIDTH) {
				if (GridWars.grid[y][x + 1] != null) {
					x += 1;
					return false;
				}
			}
		}
		return true;
	}



	public void translate() {

	}
}
