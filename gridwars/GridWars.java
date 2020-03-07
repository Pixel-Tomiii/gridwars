package gridwars;

import gridwars.buildings.*;
import gridwars.timers.BuildingBar;
import gridwars.timers.Timer;
import gridwars.timers.UpgradeBar;
import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class GridWars {

    // Global Variables.

    // Application window variables.
    public static Stage window;
    public static Scene scene;
    public static Pane display;

    public static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    public static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();

    // Tile dimensions.
    public static final int tileWidth = ((WIDTH / 23) / 16) * 16; // The width of a tile (multiple of 16).
    public static final int tileHeight = (int) (tileWidth * 0.75); // The height of a tile.

    // The grid.
    public static final int GRID_WIDTH = 25; // The width of the grid.
    public static final int GRID_HEIGHT = 21; // The height of the grid.
    public static final Tile[][] grid = new Tile[GRID_HEIGHT][GRID_WIDTH]; // The grid itself.


    // Game variables;
    public static boolean isRunning = false;

    public static Builder player1 = new Builder("PLAYER1");
    public static Builder player2 = new Builder("PLAYER2");

    private Timer timer = new Timer(600); // The timer for the game.


    // Variables to do with the special upgrades.

    // Groups;
    public Group auras = new Group();
    public static Group overlays = new Group();
    public Group nodes = new Group();
    public Group tiles = new Group();
    public Group builderNodes = new Group();
    public Group textNodes = new Group();
    public Group animation = new Group();

    public ArrayList<BuildingBar> buildingTimers = new ArrayList<BuildingBar>(0);
    public static ArrayList<Building> buildings = new ArrayList<Building>(0);
    public static Group[] buildingNodes = new Group[7];
    public static Image[] buildbarImages = new Image[6];
    public static int upgradeCap = 5;


    // The actual start method of the game (creating the application window).
    public void start(Stage primaryStage) {

        window = primaryStage;

        loadMainMenu();
        addBuildbarImages();
        addLayers();
        loadMap();
        //setupPlayers();

        // Scene
        scene = new Scene(display, WIDTH, HEIGHT);
        scene.getStylesheets().add(Main.class.getResource("resources/Styles.css").toExternalForm());

        // The cursor.
        Image cursor = new Image("gridwars/resources/assets/cursor.png");
        scene.setCursor(new ImageCursor(cursor, cursor.getWidth() / 2, cursor.getHeight() / 2));

        // Adding the scene to the stage.
        window.setScene(scene);

        window.setTitle("Grid Wars");
        window.setFullScreen(true);
        window.show();
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Adds each image for the building bar to the array so that it does not need to be reloaded every time.
    ----------------------------------------------------------------------------------------------------------------- */
    public void addBuildbarImages() {
        for (int i = 0; i < 6; i ++) {
            buildbarImages[i] = new Image("gridwars/resources/assets/Animation/Building/" + Integer.toString(i) + ".png");
        }
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Adds each building layer to the group.
    ----------------------------------------------------------------------------------------------------------------- */
    public void addLayers() {
        for (int i = 0; i < 7; i++) {
            buildingNodes[i] = new Group();
        }
    }

    /* -----------------------------------------------------------------------------------------------------------------
    Adds a node or a group of nodes to the display pane to be rendered.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void render(Object node) {
        display.getChildren().add((Node) node);
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Adds the tile and builder images to the display pane to be rendered. Adds the tiles, then the players and then the
    buildings so that the buildings overlap everything and the players overlap the tiles. Finally adds the text nodes to
    the screen.
    ----------------------------------------------------------------------------------------------------------------- */
    public void renderMap() {
        display = new Pane();
        render(tiles);
        render(overlays);

        for (Group layer:buildingNodes) {
            render(layer);
        }

        render(builderNodes);
        render(textNodes);


        scene.setRoot(display);
    }


    /* -----------------------------------------------------------------------------------------------------------------
    Adds a node or a group of nodes to the display pane to be rendered.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void renderBuilding(ImageView image, Builder player) {
        // Setting the x and y layout of the image.
        image.setLayoutX(player.image.getLayoutX());
        image.setLayoutY(player.image.getLayoutY());

        image.setFitWidth(tileWidth);
        image.setPreserveRatio(true);

        if (player.y <= 9) {
            buildingNodes[6 - Math.abs(9 - player.y)].getChildren().add(image);
        }

        else {
            buildingNodes[6 - (17 - player.y)].getChildren().add(image);
        }
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Loads the main menu. There are 2 buttons and a logo. The 2 buttons are the play and quit buttons.
    ----------------------------------------------------------------------------------------------------------------- */
    public void loadMainMenu() {

        // Resetting the pane.
        display = new Pane();

        // The logo.
        ImageView logo = new ImageView(new Image("gridwars/resources/assets/logo.png"));

        logo.setFitWidth(tileWidth * 15);
        logo.setPreserveRatio(true);

        logo.setLayoutX((WIDTH / 2) - (tileWidth * 7.5));
        logo.setLayoutY(0.15 * HEIGHT);
        render(logo);

        // Buttons.
        Button play = new Button();
        Button quit = new Button();
        Button options = new Button();


        // The play button.
        play.setMinWidth(312 * 0.1 * (WIDTH / 100));
        play.setMinHeight(120 * 0.1 * (WIDTH / 100));

        play.setLayoutX((WIDTH / 2) - (play.getMinWidth() / 2));
        play.setLayoutY(HEIGHT * 0.45);

        play.setId("playButton");
        play.setOnAction(playEvent -> startGame());
        render(play);


        // The quit button.
        quit.setMinWidth(312 * 0.1 * (WIDTH / 100));
        quit.setMinHeight(120 * 0.1 * (WIDTH / 100));

        quit.setLayoutX((WIDTH / 2) - (quit.getMinWidth() / 2));
        quit.setLayoutY(HEIGHT * 0.65);

        quit.setId("quitButton");
        quit.setOnAction(quitEvent -> Platform.exit());
        render(quit);

    }



    /* -----------------------------------------------------------------------------------------------------------------
    Loads the map but does not render it until the game starts. It gets values from 'map.txt', and then opens up images
    ready to be rendered.
    ----------------------------------------------------------------------------------------------------------------- */
    public void loadMap() {

        // Reading the file.
        FileReader mapFile = null;
        BufferedReader reader = null;


        // Variables used to add to the grid.
        int lineNumber = 0;
        String line;

        int counter = 0; // The y value.
        int column = 0; // The current x value.

        try {

            mapFile = new FileReader("gridwars/resources/map.txt");
            reader = new BufferedReader(mapFile);

            while ((line = reader.readLine()) != null) {

                String[] positions = line.split(",");

                for (String pos : positions) {

                    if (!pos.equals("")) {
                        if (!pos.equals("-")) {

                            // Player 1's Builder.
                            if (pos.equals("P1")) {

                                // Updating the positions that will be used for colision detection.
                                player1.x = column;
                                player1.y = counter;

                                player1.image.setImage(new Image("gridwars/resources/assets/Player1.png"));
                                setupImage(player1.image, player1.x, player1.y - 1);
                                player1.originalY = (float) player1.image.getLayoutY();
                                player1.originalX = (float) player1.image.getLayoutX();

                                builderNodes.getChildren().add(player1.image);
                            }


                            // Player 2's Builder.
                            else if (pos.equals("P2")) {

                                // Updating the positions that will be used for colision detection.
                                player2.x = column;
                                player2.y = counter;

                                player2.image.setImage(new Image("gridwars/resources/assets/Player2.png"));
                                setupImage(player2.image, player2.x, player2.y - 1);
                                player2.originalY = (float) player2.image.getLayoutY();
                                player2.originalX = (float) player2.image.getLayoutX();

                                builderNodes.getChildren().add(player2.image);
                            }

                            // Tiles.
                            else {

                                Tile toAdd = new Tile();

                                column = Integer.parseInt(pos);
                                setupImage(toAdd.image, column, counter);
                                grid[counter][column] = toAdd;

                                tiles.getChildren().add(grid[counter][column].image);
                            }
                        }
                    } else {
                        break;
                    }
                }
                counter++;
            }

        } catch (IOException error) {
            error.printStackTrace();
        }

        // Closing the file.
        finally {
            try {
                assert reader != null;
                reader.close();
                mapFile.close();

            } catch (IOException closeError) {
                closeError.printStackTrace();
            }
        }

        setupBackboard();
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Takes in the image and the x and y position of the builder/tile and then sets up the image width and x and y layout.
    ----------------------------------------------------------------------------------------------------------------- */
    public void setupImage(ImageView image, int x, int y) {

        image.setFitWidth(tileWidth);
        image.setPreserveRatio(true);

        image.setLayoutX((WIDTH / 2) + (tileWidth / 2) + ((x - 13) * tileWidth));
        image.setLayoutY((HEIGHT / 2) + (tileHeight / 2) + ((y - 11) * tileHeight));
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Adds the back board image for the timer.
    ----------------------------------------------------------------------------------------------------------------- */
    public void setupBackboard() {
        ImageView board = new ImageView(new Image("gridwars/resources/assets/board.png"));

        board.setFitWidth(tileWidth * 5);
        board.setFitHeight(tileHeight * 3);

        board.setLayoutX((WIDTH / 2) - (tileWidth * 2.5));
        board.setLayoutY((HEIGHT / 2) - (tileHeight * 1.5));

        tiles.getChildren().add(board);

        // The player balances.
        player1.strBalance.setX(0.1 * GridWars.WIDTH);
        player1.strBalance.setTextAlignment(TextAlignment.LEFT);

        player2.strBalance.setX(0.7 * GridWars.WIDTH);
        player2.strBalance.setTextAlignment(TextAlignment.RIGHT);

        textNodes.getChildren().addAll(player1.strBalance, player2.strBalance);
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Starts the game.
    ----------------------------------------------------------------------------------------------------------------- */
    public void startGame() {
        renderMap();
        update();
        startCountdown();
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Counts down from 3 and then starts the game.
    ----------------------------------------------------------------------------------------------------------------- */
    public void startCountdown() {

        player1.movement.start();
        player1.income.start();

        player2.movement.start();
        player2.income.start();
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Starts or ends the game.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void handleTimerOutput() {

        if (isRunning) {
            player1.movement.stop();
            player1.income.stop();

            player2.movement.stop();
            player2.income.stop();
        }

        else {
            player1.movement.start();
            player1.income.start();

            player2.movement.start();
            player2.income.start();
        }
    }




    /* -----------------------------------------------------------------------------------------------------------------
    Setting up the key bindings for each player. This includes building, upgrading and moving. Sets up a listener
    ----------------------------------------------------------------------------------------------------------------- */
    public void update() {

        // Key pressed events.
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {

                /* -----------------------------------------------------------------------------------------------------
                The movement keybindings for both players. Player 1 moves with the wasd keys and player 2 moves with the
                up, down, left and right arrow keys.
                ----------------------------------------------------------------------------------------------------- */
                // Player 1's Builder
                switch (key.getCode()) {
                    case W:
                    case S:
                    case A:
                    case D:
                        player1.previousDirection = player1.newDirection;
                        player1.newDirection = key.getCode();
                        break;
                }

                // Player 2's Builder
                switch (key.getCode()) {
                    case UP:
                    case DOWN:
                    case LEFT:
                    case RIGHT:
                        player2.previousDirection = player2.newDirection;
                        player2.newDirection = key.getCode();
                        break;
                }



                /* -----------------------------------------------------------------------------------------------------
                The building and upgrading keybindings for both players. Player 1 builds buildings with the number keys
                1 - 7 and upgrades buildings with the 1 and 2 keys. Player 2 buildings buildings with the numpad keys
                1 - 7 and upgrades buildings with the numpad keys 1 and 2. The player must not be moving when trying to
                build and they must not already be building before trying to upgrade. In order to build the tile must
                not have a building on it already and in order to upgrade, the tile must have a building on it.
                ----------------------------------------------------------------------------------------------------- */
                // Setting up variables which will be used every time this is ran.
                boolean builtOn;
                if (!player1.isMoving && !player1.isBuilding) {
                    builtOn = grid[player1.y][player1.x].builtOn;

                    /* -------------------------------------------------------------------------------------------------
                    Getting the build inputs for player 1. This is seperate from player 2 because the key bindings are
                    different.
                    ------------------------------------------------------------------------------------------------- */
                    switch (key.getCode()) {

                        case DIGIT1:
                            checkBuild(new Tower(player1), player1, builtOn);

                            if (builtOn) {
                                upgrade(player1);
                            }
                            break;

                        case DIGIT2:
                            checkBuild(new Quarry(player1), player1, builtOn);

                            if (builtOn) {
                                activateAbility(player1);
                            }
                            break;

                        case DIGIT3:
                            checkBuild(new Factory(player1), player1, builtOn);
                            break;

                        case DIGIT4:
                            checkBuild(new Barracks(player1), player1, builtOn);
                            break;

                        case DIGIT5:
                            checkBuild(new ResearchLab(player1), player1, builtOn);
                            break;

                        case DIGIT6:
                            checkBuild(new Bunker(player1), player1, builtOn);
                            break;

                        case DIGIT7:
                            checkBuild(new MicrobotsLab(player1), player1, builtOn);
                            break;
                    }
                }

                if (!player2.isMoving && !player2.isBuilding) {
                    /* -------------------------------------------------------------------------------------------------
                    Getting the build inputs for player 2. Player 2 uses the numpad keys to build buildings.
                    ------------------------------------------------------------------------------------------------- */
                    builtOn = grid[player2.y][player2.x].builtOn;

                    switch (key.getCode()) {

                        case NUMPAD1:
                            checkBuild(new Tower(player2), player2, builtOn);

                            if (builtOn) {
                                upgrade(player2);
                            }
                            break;

                        case NUMPAD2:
                            checkBuild(new Quarry(player2), player2, builtOn);

                            if (builtOn) {
                                activateAbility(player2);
                            }
                            break;

                        case NUMPAD3:
                            checkBuild(new Factory(player2), player2, builtOn);
                            break;

                        case NUMPAD4:
                            checkBuild(new Barracks(player2), player2, builtOn);
                            break;

                        case NUMPAD5:
                            checkBuild(new ResearchLab(player2), player2, builtOn);
                            break;

                        case NUMPAD6:
                            checkBuild(new Bunker(player2), player2, builtOn);
                            break;

                        case NUMPAD7:
                            checkBuild(new MicrobotsLab(player2), player2, builtOn);
                            break;
                    }
                }
            }
        });

        // Key released events.
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {

                // Player 1's Builder
                switch (key.getCode()) {

                    case W:
                    case S:
                    case A:
                    case D:
                        if (player1.newDirection == key.getCode()) {
                            player1.newDirection = KeyCode.UNDEFINED;
                        }
                        break;
                }

                // Player 2's Builder
                switch (key.getCode()) {

                    case UP:
                    case DOWN:
                    case LEFT:
                    case RIGHT:
                        if (player2.newDirection == key.getCode()) {
                            player2.newDirection = KeyCode.UNDEFINED;
                        }
                        break;
                }
            }
        });
    }


    /* -----------------------------------------------------------------------------------------------------------------
    Checks to see if the player can build a building and then builds it. It does this by first checking to see if
    there is already a building on the tile and then it checks to see if the player has enough money. It then sends the
    necessary information to the build method so that the building can be built.
    ----------------------------------------------------------------------------------------------------------------- */
    public void checkBuild(Building building, Builder player, boolean builtOn) {
        if (!builtOn) {
            if (player.balance - building.COST >= 0) {
                build(building, player);
            }
        }
    }



    /* -----------------------------------------------------------------------------------------------------------------
    This method builds the given building in the correct location. It adds the image to the correct "layer", in the list
     so that the images will not overlap each other in a weird way.
    ----------------------------------------------------------------------------------------------------------------- */
    public void build(Building building, Builder player) {

        int x = player.x;
        int y = player.y;

        grid[y][x].build(building);
        player.balance -= grid[y][x].building.COST;

        //updateIfNextToSpecialTower(player, grid[y][x].building, x, y);
        new BuildingBar(building, player);
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Upgrades the building if the player has enough money.
    ----------------------------------------------------------------------------------------------------------------- */
    public void upgrade(Builder player) {
        Building building = (Building) grid[player.y][player.x].building;

        if (building.level < player.buildingLevelCap) {
            if (player.balance - building.UPGRADE_COST >= 0) {
                new UpgradeBar(building, player, false);
                player.balance -= building.UPGRADE_COST;
            }
        }
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Includes a switch statement which checks which building's ability is being activated and fetches the cost. It then
    runs a method which should handle the special upgrade.
    ----------------------------------------------------------------------------------------------------------------- */
    public void activateAbility(Builder player) {
        Building building = grid[player.y][player.x].building;

        if (!building.hasActivated) {
            if (building.level >= 5) {
                new UpgradeBar(building, player, true);
                player.balance -= building.ACTIVATE_COST;
                building.hasActivated = true;
            }
        }
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Checks all the tiles with buildings and finds the distance between each one and the players. If the distance is less
    than 1.5 then the opacity becomes 0.4, else it stays as 1.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void setOpacities() {

        int distanceFromPlayer1;
        int distanceFromPlayer2;
        int maxDistance = (int) Math.pow(Math.pow(2 * tileWidth, 2) + Math.pow(2 * tileHeight, 2), 0.5);

        for (Building building:buildings) {
            distanceFromPlayer1 = distance(player1, building);
            distanceFromPlayer2 = distance(player2, building);

            if (distanceFromPlayer1 <= maxDistance / 4 || distanceFromPlayer2 <= maxDistance / 4 || (distanceFromPlayer1 > maxDistance && distanceFromPlayer2 > maxDistance)) {
                building.image.setOpacity(1);
            }

            else {
                building.image.setOpacity(0.25);
            }
        }
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Finds the distance between the given player and the given tile. It does this using the image positions of the tiles
    so that the opacities update as the player is moving and not as they move from a tile to another.
    ----------------------------------------------------------------------------------------------------------------- */
    public static int distance(Builder player, Building building) {

        double px = player.image.getLayoutX();
        double py = player.image.getLayoutY();

        double bx = building.image.getLayoutX();
        double by = building.image.getLayoutY();

        return (int) (Math.pow(Math.pow(px - bx, 2) + Math.pow(py - by, 2), 0.5));

    }



    /* -----------------------------------------------------------------------------------------------------------------
    Updates the powers on the tiles. Takes in a player and a building and then uses the id of
    the building to determine which pattern to use. Removes the value of old power from the tile's total (based on which
    player owns the building), then adds the new one.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void updateTilePowers(Builder player, Building building) {

        /*switch (building.id) {
            case 1:
                towerUpdate(building, player.x, player.y);
                break;

            case 2:
                quarryUpdate(building, player.x, player.y);
                break;

            case 3:
                factoryUpdate(building, player.x, player.y);
                break;

            case 4:
                barracksUpdate(building, player.x, player.y);
                break;

            case 5:
                researchLabUpdate(building, player.x, player.y);
                break;

            case 6:
                bunkerUpdate(building, player.x, player.y);
                break;

            case 7:
                microbotsLabUpdate(building, player.x, player.y);
                break;
        }*/
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Takes in an x position, y position and the building (which will be used to acces the old and new powers) and then
    updates the power of the given tile. It makes sure the tile exists before updating the power.
    ------------------------------------------------------------------------------------------------------------------*/
    public static void updateSpecificTilesPower(ArrayList<Integer[]> points, Building building) {
        int x;
        int y;

        for (Integer[] coordinate:points) {
            x = coordinate[0];
            y = coordinate[1];

            if (grid[y][x] != null) {
                if (y <= 10) {
                    grid[y][x].yellowPower += building.power - building.oldPower;
                    grid[10 + (10 - y)][x].yellowPower += building.power - building.oldPower;
                } else {
                    grid[y][x].purplePower += building.power - building.oldPower;
                    grid[10 + (10 - y)][x].purplePower += building.power - building.oldPower;
                }

                grid[y][x].updateColour();
                grid[10 + (10 - y)][x].updateColour();
            }
        }
    }



    /* -----------------------------------------------------------------------------------------------------------------
    Checks to see if the built building is adjacent to a tower which has had it's ability activated.
    ----------------------------------------------------------------------------------------------------------------- */
    public static void updateIfNextToSpecialTower(Building building, int x, int y) {
        if (grid[y][x + 1] != null) {
            if (grid[y][x + 1].building != null) {
                if (grid[y][x + 1].building instanceof Tower && grid[y][x + 1].building.hasActivated) {
                    building.power++;
                }
            }
        }

        if (grid[y][x - 1] != null) {
            if (grid[y][x - 1].building != null) {
                if (grid[y][x - 1].building instanceof Tower && grid[y][x - 1].building.hasActivated) {
                    building.power++;
                }
            }
        }

        if (grid[y + 1][x] != null) {
            if (grid[y + 1][x].building != null) {
                if (grid[y + 1][x].building instanceof Tower && grid[y + 1][x].building.hasActivated) {
                    building.power++;
                }
            }
        }

        if (grid[y - 1][x] != null) {
            if (grid[y - 1][x].building != null) {
                if (grid[y - 1][x].building instanceof Tower && grid[y - 1][x].building.hasActivated) {
                    building.power++;
                }
            }
        }
    }

/*

        // The ingame timer.
        lastSecond = System.nanoTime();

        AnimationTimer clock = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (now > lastSecond + second) {

                    // Checking to see if the game has ended.
                    if (timer == 0) {
                        int score = getScore();

                        if (score == 0 && drew == false){
                            timer += 180; // Adding 3 minutes.
                            drew = true;

                        } else {
                            endGame(score);
                            player1Movement.stop();
                            player2Movement.stop();
                            player1Income.stop();
                            player2Income.stop();
                         this.stop();
    }

    // Updating the timer.

} else {
        updateTimer();
        lastSecond = now;
        }
        }
        }
        };clock.start();




    // Special upgrade.

    public void upgradeSpecial(int x, int y) {

        grid[y][x].building.special();

        auras.getChildren().add(grid[y][x].building.aura);

        display = new Pane(tiles,buildings,auras,builderNodes,textNodes,animation);

        game.setRoot(display);

        grid[y][x].building.aura.setFitWidth(tileWidth);
        grid[y][x].building.aura.setPreserveRatio(true);

    }



    // Getting the score.

    public int getScore() {

        int score = 0;


        // Looping through the grid.

        for (int x = 0; x < 25; x ++) {
            for(int y = 0; y < 10; y ++) {

                if (grid[y][x] != null) {

                    int centre = 10;
                    int difference = centre - y;


                    // Calculating the overall power.

                    int power1 = grid[y][x].power;
                    int power2 = grid[centre + difference][x].power;

                    int total = power1 - power2;


                    // updating the score.

                    if (total < 0) {
                        score --;
                    }

                    else if (total > 0) {
                        score ++;
                    }
                }
            }
        }

        return score;
    }





    // Ending the game.

    public void endGame(int score) {
        Image gameOver;

        Group end = new Group();

        if (score == 0) {
            gameOver = new Image("Assets/Screens/Draw.png");

        } else if (score > 0) {
            gameOver = new Image("Assets/Screens/Player 1 Win.png");

        } else {
            gameOver = new Image("Assets/Screens/Player 2 Win.png");

        }

        ImageView gameOverImage = new ImageView(gameOver);


        // Bounds


        gameOverImage.setFitWidth(width);
        gameOverImage.setPreserveRatio(true);


        // Position.

        gameOverImage.setLayoutX(0);
        gameOverImage.setLayoutY((height/2) - (width / 2));

        end.getChildren().add(gameOverImage);

        display = new Pane(end);

        game.setRoot(display);
    }
    */
}

