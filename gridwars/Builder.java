package gridwars;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;

/* ---------------------------------------------------------------------------------------------------------------------
The builder class handles everything builder related. This mostly includes movement but it also includes the image
which is displayed.
--------------------------------------------------------------------------------------------------------------------- */
public class Builder {

	// Movement.
	public AnimationTimer movement;
	public float framesPerMove = 20; // The interval at which the player moves.
	int frameCount = 0;

	private long lastMove = 0; // The last time the player moved.
	public boolean isMoving = false; // Determines if the player is moving or not.

	public float originalX = 0;
	private float newX = 0;
	public float originalY = 0;
	private float newY = 0;

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

	public KeyCode previousDirection = KeyCode.UNDEFINED;
 	public KeyCode newDirection = KeyCode.UNDEFINED;


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
 	public float buildSpeedMultiplier = 0; // Increases the speed at which the player builds.
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

  					if (!isMoving) {
						checkForCollision();
					}

  					else {
  						frameCount++;
  						if (frameCount <= framesPerMove) {
							accelerate();
							translate();
						}

  						else {
							isMoving = false;

							newX = 0;
							newY = 0;

							originalX = (float) image.getLayoutX();
							originalY = (float) image.getLayoutY();

  							frameCount = 0;
						}
					}
				}
			}
		};
	}

	public void accelerate() {
		float newVel = (float) ((-2 * Math.pow(frameCount / framesPerMove, 3)) + (3 * Math.pow(frameCount / framesPerMove, 2)));
		setVelocity(newVel);
	}

	public void setVelocity(float newVel) {

		if (up) {
			newY = GridWars.tileHeight * -newVel;

		} else if (down) {
			newY = GridWars.tileHeight * newVel;

		} else if (left) {
			newX = GridWars.tileWidth * -newVel;

		} else if (right) {
			newX =  GridWars.tileWidth * newVel;
		}
	}


	public void checkForCollision() {

		if (newDirection != KeyCode.UNDEFINED) {
			switch (newDirection) {
				case W:
				case UP:
					up = true;
					down = false;
					left = false;
					right = false;
					break;

				case S:
				case DOWN:
					up = false;
					down = true;
					left = false;
					right = false;
					break;

				case A:
				case LEFT:
					up = false;
					down = false;
					left = true;
					right = false;
					break;

				case D:
				case RIGHT:
					up = false;
					down = false;
					left = false;
					right = true;
					break;
			}

			if (up) {
				if (!willCollide(x, y - 1)) {
					y--;
				}

			} else if (down) {
				if (!willCollide(x, y + 1)) {
					y++;
				}

			} else if (left) {
				if (!willCollide(x - 1, y)) {
					x--;
				}

			} else if (right) {
				if (!willCollide(x + 1, y)) {
					x++;
				}

			}

		} else {
			up = false;
			down = false;
			left = false;
			right = false;
			isMoving = false;
		}
	}

	public boolean willCollide(int x, int y) {
		if (GridWars.grid[y][x] == null) {
			return true;
		}

		isMoving = true;
		return false;
	}

	public void translate() {
		image.setLayoutX(originalX + newX);
		image.setLayoutY(originalY + newY);
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
}
