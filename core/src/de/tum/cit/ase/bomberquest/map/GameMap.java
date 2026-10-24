package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import java.util.*;

/**
 * Represents the game map in the Bomber Quest game.
 * <p>
 * The GameMap class is the core of the game environment, managing all entities and objects on the map.
 * It is responsible for initializing, updating, and rendering game elements such as players, enemies,
 * bombs, power-ups, and walls. It also handles the physics simulation and game state transitions.
 * <p>
 * <b>Important Points:</b>
 * <ul>
 *     <li>Manages all game objects, including players, enemies, bombs, power-ups, and walls.</li>
 *     <li>Implements the core update loop via {@link #tick(float)}.</li>
 *     <li>Handles physics simulation using Box2D.</li>
 *     <li>Supports dynamic map loading from resource files.</li>
 *     <li>Provides resource management and cleanup via {@link #dispose()}.</li>
 * </ul>
 */
public class GameMap {

    static {
        com.badlogic.gdx.physics.box2d.Box2D.init();
    }

    /**
     * The time step for the physics simulation.
     * This is the amount of time that the physics simulation advances by in each frame.
     * It is set to 1/refreshRate, where refreshRate is the refresh rate of the monitor, e.g., 1/60 for 60 Hz.
     */
    private static final float TIME_STEP = 1f / Gdx.graphics.getDisplayMode().refreshRate;
    /** The number of velocity iterations for the physics simulation. */
    private static final int VELOCITY_ITERATIONS = 6;
    /** The number of position iterations for the physics simulation. */
    private static final int POSITION_ITERATIONS = 2;

    /**
     * The accumulated time since the last physics step.
     * We use this to keep the physics simulation at a constant rate even if the frame rate is variable.
     */
    private float physicsTime = 0;

    /** The game, in case the map needs to access it. */
    private final BomberQuestGame game;

    /** The Box2D world object for handling physics and collision. */
    private final World world;

    /** Central list of all game objects on the map. */
    private final List<GameObject> gameObjects;

    private Player player;
    private Exit exit;
    private List<Bomb> bomb;
    private final List<Bomb> bombsToRemove;
    private List<Explosion> explosion;
    private final List<Explosion> explosionsToRemove;
    private final List<Floor> floor;
    private final List<DestructibleWall> destructibleWall;
    private final List<IndestructibleWall> indestructibleWall;
    private final List<Entrance> entrance;
    private final List<Enemy> enemy;
    private final List<Enemy> enemyToRemove;
    private final List<PowerUp> powerUp;
    private int width;
    private int height;
    private boolean isExitVisible;

    /**
     * Creates a new game map and initializes its components.
     * <p>
     * The map includes entities like walls, bombs, power-ups, and enemies, which are managed
     * within the map's boundaries.
     *
     * @param game the main game instance for coordinating game-wide events.
     */
    public GameMap(BomberQuestGame game) {
        this.game = game;
        this.world = new World(Vector2.Zero, true);
        this.world.setContactListener(new GameMapContactListener());
        this.gameObjects = new ArrayList<>();
        this.bomb = new ArrayList<>();
        this.bombsToRemove = new ArrayList<>();
        this.explosion = new ArrayList<>();
        this.explosionsToRemove = new ArrayList<>();
        this.floor = new ArrayList<>();
        this.destructibleWall = new ArrayList<>();
        this.indestructibleWall = new ArrayList<>();
        this.entrance = new ArrayList<>();
        this.enemy = new ArrayList<>();
        this.enemyToRemove = new ArrayList<>();
        this.powerUp = new ArrayList<>();
        this.width = 0;
        this.height = 0;
        this.isExitVisible = false;
    }

    /**
     * Updates the game map and its entities.
     * <p>
     * Called every frame to update dynamic objects on the map (players, bombs, enemies, etc.)
     * and verify game state conditions such as victory.
     *
     * @param frameTime the time elapsed since the last frame.
     */
    public void tick(float frameTime) {
        this.player.tick(frameTime);

        if (isExitVisible) {
            if (Math.floor(this.player.getX() + 0.5f) == this.exit.getX() &&
                    Math.floor(this.player.getY() + 0.5f) == this.exit.getY()) {
                game.goToVictory();
            }
        }

        for (Bomb b : bomb) {
            b.tick(frameTime);
            if (b.getIsExploded()) {
                bombsToRemove.add(b);
            }
        }
        bomb.removeAll(bombsToRemove);
        bombsToRemove.clear();

        for (Explosion e : explosion) {
            e.tick(frameTime);
            if (e.getIsEnded()) {
                explosionsToRemove.add(e);
            }
        }
        explosion.removeAll(explosionsToRemove);
        explosionsToRemove.clear();

        for (Enemy e : enemy) {
            e.tick(frameTime);
        }
        enemy.removeAll(enemyToRemove);
        enemyToRemove.clear();

        doPhysicsStep(frameTime);
    }

    /**
     * Advances the physics simulation to match the frame time.
     * <p>
     * Ensures physics consistency regardless of frame rate variability.
     *
     * @param frameTime the time elapsed since the last frame.
     */
    private void doPhysicsStep(float frameTime) {
        this.physicsTime += frameTime;
        while (this.physicsTime >= TIME_STEP) {
            this.world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            this.physicsTime -= TIME_STEP;
        }
    }

    /**
     * Retrieves the player object from the map.
     *
     * @return the {@link Player} object representing the player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the list of enemies currently on the map.
     *
     * @return a {@link List} of {@link Enemy} objects.
     */
    public List<Enemy> getEnemy() {
        return this.enemy;
    }

    /**
     * Gets the number of active enemies on the map.
     *
     * @return the number of {@link Enemy} objects remaining.
     */
    public int getEnemyNo() {
        return this.enemy.size();
    }

    /** Retrieves the width of the game map. @return the width of the map. */
    public int getWidth() {
        return width;
    }

    /** Retrieves the height of the game map. @return the height of the map. */
    public int getHeight() {
        return height;
    }

    /** Retrieves the list of power-ups currently on the map. @return a {@link List} of {@link PowerUp} objects. */
    public List<PowerUp> getPowerUp() {
        return this.powerUp;
    }

    /**
     * Adds a bomb to the map.
     *
     * @param b the {@link Bomb} object to add.
     */
    public void addBomb(Bomb b) {
        this.bomb.add(b);
    }

    /**
     * Adds an explosion to the map.
     *
     * @param e the {@link Explosion} object to add.
     */
    public void addExplosion(Explosion e) {
        this.explosion.add(e);
    }

    /**
     * Checks if the exit is visible on the map.
     *
     * @return true if the exit is visible, false otherwise.
     */
    public boolean getIsExitVisible() {
        return this.isExitVisible;
    }

    /** Ends the game and transitions to the game-over state. */
    public void endGame() {
        game.goToGameOver();
    }

    /**
     * Removes a power-up from the map at the specified coordinates.
     *
     * @param x the x-coordinate of the power-up.
     * @param y the y-coordinate of the power-up.
     */
    public void removePowerUp(float x, float y) {
        for (Iterator<PowerUp> iterator = powerUp.iterator(); iterator.hasNext();) {
            PowerUp powerUp = iterator.next();
            if (powerUp.getX() == x && powerUp.getY() == y) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * Marks an enemy for removal from the map.
     *
     * @param e the {@link Enemy} object to remove.
     */
    public void removeEnemy(Enemy e) {
        enemyToRemove.add(e);
    }

    /**
     * Checks if a bomb is present at the specified position on the map.
     *
     * @param x the x-coordinate to check.
     * @param y the y-coordinate to check.
     * @return true if a bomb is present, false otherwise.
     */
    public boolean isBombPresent(int x, int y) {
        return bomb.stream().anyMatch(b -> b.getX() == x && b.getY() == y);
    }

    /**
     * Checks if an indestructible wall is present at the specified position.
     *
     * @param x the x-coordinate to check.
     * @param y the y-coordinate to check.
     * @return true if an indestructible wall is present, false otherwise.
     */
    public boolean isIndestructibleWall(int x, int y) {
        return indestructibleWall.stream().anyMatch(wall -> wall.getX() == x && wall.getY() == y);
    }

    /**
     * Checks if a destructible wall is present at the specified position.
     *
     * @param x the x-coordinate to check.
     * @param y the y-coordinate to check.
     * @return true if a destructible wall is present, false otherwise.
     */
    public boolean isDestructibleWall(int x, int y) {
        return destructibleWall.stream().anyMatch(wall -> wall.getX() == x && wall.getY() == y);
    }

    /**
     * Destroys a destructible wall at the specified position.
     *
     * @param x the x-coordinate of the wall.
     * @param y the y-coordinate of the wall.
     */
    public void destroyWall(int x, int y) {
        for (Iterator<DestructibleWall> iterator = destructibleWall.iterator(); iterator.hasNext();) {
            DestructibleWall wall = iterator.next();
            if (wall.getX() == x && wall.getY() == y) {
                world.destroyBody(wall.getHitbox());
                iterator.remove();
                break;
            }
        }
    }

    /**
     * Loads the map from a .properties file.
     * <p>
     * Reads the map layout and initializes entities like walls, power-ups, and enemies.
     *
     * @param filePath the relative path to the map file.
     */
    public void loadMap(String filePath) {

        FileHandle fileHandle = Gdx.files.internal(filePath);
        List<Vector2> enemyPositions = new ArrayList<>();

        int[][] tiles = new int[50][50]; // It is assumed that map size won't be higher than 50x50

        try {
            String mapData = fileHandle.readString();
            String[] lines = mapData.split("\n");
            Random random = new Random();
            boolean exitPlaced = false;

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] keyValue = line.split("=");
                String[] coordinates = keyValue[0].split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                int type = Integer.parseInt(keyValue[1]);

                if (x > width) {
                    width = x;
                }

                if (y > height) {
                    height = y;
                }

                switch (type) {

                    case 0 -> {
                        IndestructibleWall iw = new IndestructibleWall(world, x, y);
                        this.indestructibleWall.add(iw);
                        tiles[x][y] = 1;
                    }
                    case 1 -> {
                        DestructibleWall dw = new DestructibleWall(world, x, y);
                        this.destructibleWall.add(dw);
                    }
                    case 2 -> {
                        Entrance entrance = new Entrance(x, y);
                        this.entrance.add(entrance);
                        Floor floor = new Floor(x, y);
                        this.floor.add(floor);
                        this.player = new Player(world, entrance, this);
                    }
                    case 3 -> {
                        Enemy enemy = new Enemy(world, x, y, this);
                        this.enemy.add(enemy);
                        enemyPositions.add(new Vector2(x, y));
                    }
                    case 4 -> {
                        Exit exit = new Exit(x, y);
                        this.exit = exit;
                        exitPlaced = true;
                        DestructibleWall dw = new DestructibleWall(world, x, y);
                        this.destructibleWall.add(dw);
                    }
                    case 5 -> {
                        ConcurrentBomb cb = new ConcurrentBomb(x, y);
                        this.powerUp.add(cb);
                        DestructibleWall dw = new DestructibleWall(world, x, y);
                        this.destructibleWall.add(dw);
                    }
                    case 6 -> {
                        BlastRadius br = new BlastRadius(x, y);
                        this.powerUp.add(br);
                        DestructibleWall dw = new DestructibleWall(world, x, y);
                        this.destructibleWall.add(dw);
                    }
                    case 7 -> {
                        Speed speed = new Speed(x, y);
                        this.powerUp.add(speed);
                        DestructibleWall dw = new DestructibleWall(world, x, y);
                        this.destructibleWall.add(dw);
                    }
                    case 8 -> {
                        Shield shield = new Shield(x, y);
                        this.powerUp.add(shield);
                        DestructibleWall dw = new DestructibleWall(world, x, y);
                        this.destructibleWall.add(dw);
                    }
                    default -> {
                        System.out.println("Unknown game object.");
                    }
                }
            }

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (tiles[i][j] != 1) {
                        Floor floor = new Floor(i, j);
                        this.floor.add(floor);
                    }
                }
            }

            if (!exitPlaced && !destructibleWall.isEmpty()) {
                Wall randomWall = destructibleWall.get(random.nextInt(destructibleWall.size()));
                Exit exit = new Exit((int) randomWall.getX(), (int) randomWall.getY());
                this.exit = exit;
            }

        } catch (Exception e) {
            Gdx.app.error("GameMap", "Failed to load map file: " + e.getMessage());
        }
    }

    /**
     * Returns a list of all drawable objects on the map.
     *
     * @return a {@link List} of {@link GameObject} instances to be rendered.
     */
    public List<GameObject> getAllDrawables() {
        gameObjects.clear();

        gameObjects.addAll(this.floor);
        gameObjects.addAll(this.powerUp);
        if (getEnemyNo() == 0 && !isDestructibleWall((int) this.exit.getX(), (int) this.exit.getY())) {
            gameObjects.add(this.exit);
            isExitVisible = true;
        }
        gameObjects.addAll(this.destructibleWall);
        gameObjects.addAll(this.indestructibleWall);
        gameObjects.addAll(this.explosion);
        gameObjects.addAll(this.bomb);
        gameObjects.addAll(enemy);
        gameObjects.add(this.player);

        return gameObjects;
    }

    /**
     * Cleans up all resources used by the map.
     * <p>
     * Disposes of physics objects and removes game entities to free memory.
     */
    public void dispose() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Disposable) {
                ((Disposable) gameObject).dispose();
            }
        }
        gameObjects.clear();

        bomb.clear();
        bombsToRemove.clear();
        explosion.clear();
        explosionsToRemove.clear();
        floor.clear();
        destructibleWall.clear();
        indestructibleWall.clear();
        entrance.clear();
        enemy.clear();
        enemyToRemove.clear();
        powerUp.clear();

        if (world != null) {
            world.dispose();
        }

        Gdx.app.log("GameMap", "Disposed all resources for the current map.");
    }
}
