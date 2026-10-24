package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.audio.SoundEffect;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Textures;
import java.util.Iterator;
import static de.tum.cit.ase.bomberquest.map.CollisionCategories.CATEGORY_PLAYER;
import static de.tum.cit.ase.bomberquest.map.CollisionCategories.MASK_PLAYER;

/**
 * Represents the player character in the Bomber Quest game.
 * <p>
 * The player is controlled by user input and interacts with the game map,
 * including moving, collecting power-ups, and dropping bombs. The player has
 * attributes such as speed, bomb capacity, and blast radius, which can be
 * enhanced through power-ups.
 * </p>
 */
public class Player extends GameObject {

    /**
     * Enum representing the possible movement directions of the player.
     */
    public enum DIRECTION {
        DOWN, RIGHT, UP, LEFT, DEFAULT
    }

    /** Total time elapsed since the game started. We use this for calculating the player movement and animating it. */
    private float elapsedTime;
    /** The Box2D hitbox of the player, used for position and collision detection. */
    private final Body hitbox;
    /** Player should spawn in the entrance. */
    private final Entrance entrance;
    /** Movement speed multiplier. */
    private float speed;
    /** The Box2D physics world where the player exists. Handles collisions and physics-based interactions. */
    private World world;
    /** The game map that the player interacts with. */
    private final GameMap map;
    /** The number of bombs the player can place simultaneously. */
    private int bombNo;
    /** The radius of bomb explosions placed by the player. */
    private int blastRadius;
    /** The maximum possible radius for bomb explosions. */
    private final int maxBlastRadius;
    /** The number of bombs the player can place concurrently. */
    private int concurrentBomb;
    /** The maximum number of bombs the player can place concurrently. */
    private final int maxConcurrentBombs;
    /** The maximum speed the player can achieve. */
    private final float maxSpeed;
    /** Indicates if the player is shielded from damage. */
    private boolean isShielded;
    /** The duration for which the player's shield remains active. */
    private float shieldDuration;
    /** Flag to determine if the player's shield is currently activated. */
    private boolean isShieldActivated;
    /** The current direction the player is facing or moving. */
    private DIRECTION direction;

    /**
     * Creates a player instance with initial attributes and position.
     * <p>
     * This constructor initializes the player at the specified entrance
     * on the game map and sets up their initial attributes, such as speed,
     * bomb capacity, and shield status.
     *  * </p>
     *
     * @param world the physics world where the player exists.
     * @param entrance the entrance where the player spawns.
     * @param map the game map where the player interacts.
     */
    public Player(World world, Entrance entrance, GameMap map) {
        super(entrance.getX(), entrance.getY());
        this.world = world;
        this.entrance = entrance;
        this.hitbox = createHitbox(world, new CircleShape(), true,
                this.entrance.getX(), this.entrance.getY(),
                CATEGORY_PLAYER, MASK_PLAYER);
        this.map = map;
        this.speed = 5.0f;
        this.bombNo = 1;
        this.blastRadius = 1;
        this.maxBlastRadius = 8;
        this.concurrentBomb = 1;
        this.maxConcurrentBombs = 8;
        this.maxSpeed = 10.0f;
        this.isShielded = false;
        this.shieldDuration = 3.0f;
        this.isShieldActivated = false;
        this.direction = DIRECTION.DEFAULT;
    }

    /**
     * Retrieves the player's hitbox for collision detection.
     *
     * @return the {@link Body} representing the player's hitbox.
     */
    public Body getHitbox() {
        return this.hitbox;
    }

    /**
     * Updates the player's state each frame.
     <p>
     * Handles movement, bomb placement, shield expiration, and power-up collection.
     * </p>
     *
     * @param frameTime the time elapsed since the last frame, used to calculate updates.
     *
     */
    public void tick(float frameTime) {

        this.elapsedTime += frameTime;

        if (this.isShieldActivated) {
            this.shieldDuration -= frameTime;
            if (this.shieldDuration <= 0) {
                this.isShielded = false;
                this.isShieldActivated = false;
            }
        }

        float xVelocity = 0f;
        float yVelocity = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yVelocity -= speed;
            this.direction = DIRECTION.DOWN;
        }else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xVelocity += speed;
            this.direction = DIRECTION.RIGHT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yVelocity += speed;
            this.direction = DIRECTION.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xVelocity -= speed;
            this.direction = DIRECTION.LEFT;
        } else this.direction = DIRECTION.DEFAULT;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            dropBomb();
        }

        this.hitbox.setLinearVelocity(xVelocity, yVelocity);

        checkForPowerUps();
    }

    /**
     * Renders the current appearance of the player based on movement direction.
     *
     * @return a {@link TextureRegion} representing the player's appearance.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        switch (this.direction) {
            case DOWN -> {
                return Animations.CHARACTER_WALK_DOWN.getKeyFrame(this.elapsedTime, true);
            }
            case RIGHT -> {
                return Animations.CHARACTER_WALK_RIGHT.getKeyFrame(this.elapsedTime, true);
            }
            case UP -> {
                return Animations.CHARACTER_WALK_UP.getKeyFrame(this.elapsedTime, true);
            }
            case LEFT -> {
                return Animations.CHARACTER_WALK_LEFT.getKeyFrame(this.elapsedTime, true);
            }
            default -> {
                return Textures.CHARACTER;
            }
        }
    }

    @Override
    public float getX() {
        return hitbox.getPosition().x;
    }

    @Override
    public float getY() {
        return hitbox.getPosition().y;
    }

    /**
     * Drops a bomb at the player's current position if available.
     */
    private void dropBomb() {
        if (getBombNo() > 0) {
            float bombX = (float) Math.floor(this.getX() + 0.5f);
            float bombY = (float) Math.floor(this.getY() + 0.5f);

            if (!map.isBombPresent((int) bombX, (int) bombY)) {
                Bomb bomb = new Bomb(this.world, (int) bombX, (int) bombY, this.map, 3, this.blastRadius);
                map.addBomb(bomb);
                SoundEffect.DROP_BOMB.play();
                decrementBombNo();
            }
        }
    }

    /**
     * Checks for and collects any power-ups at the player's current position.
     */
    private void checkForPowerUps() {
        Iterator<PowerUp> iterator = map.getPowerUp().iterator();

        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            if (Math.floor(getX() + 0.5f) == powerUp.getX() &&
                    Math.floor(getY() + 0.5f) == powerUp.getY()) {
                if (powerUp instanceof BlastRadius) {
                    if (blastRadius <= maxBlastRadius) {
                        incrementBlastRadius();
                    }
                } else if (powerUp instanceof ConcurrentBomb) {
                    if (concurrentBomb <= maxConcurrentBombs) {
                        incrementConcurrentBomb();
                        incrementBombNo();
                    }
                } else if (powerUp instanceof Speed) {
                    if (speed <= maxSpeed) {
                        incrementSpeed();
                    }
                } else if (powerUp instanceof Shield) {
                    isShielded = true;
                }

                SoundEffect.POWER_UP.play();
                iterator.remove();
                map.removePowerUp(powerUp.getX(), powerUp.getY());
                break;
            }
        }
    }

    /** Includes getter and setter methods for accessing and modifying player attributes. */
    public GameMap getMap() {
        return this.map;
    }

    /**
     * Retrieves the current number of bombs the player can place.
     *
     * @return the number of bombs available.
     */
    public int getBombNo() {
        return this.bombNo;
    }

    /**
     * Retrieves the player's current blast radius for bomb explosions.
     *
     * @return the blast radius of bombs.
     */
    public int getBlastRadius() {
        return this.blastRadius;
    }

    /**
     * Retrieves the player's current capacity for placing concurrent bombs.
     *
     * @return the number of bombs that can be placed concurrently.
     */
    public int getConcurrentBombs() {
        return this.concurrentBomb;
    }

    /**
     * Increases the number of bombs available for the player to place.
     */
    public void incrementBombNo() {
        this.bombNo++;
    }

    /**
     * Decreases the number of bombs available for the player to place.
     */
    private void decrementBombNo() {
        this.bombNo--;
    }

    /**
     * Increases the player's bomb blast radius by one tile, up to the maximum allowed radius.
     */
    private void incrementBlastRadius() {
        this.blastRadius++;
    }

    /**
     * Increases the player's concurrent bomb placement capacity by one, up to the maximum limit.
     */
    private void incrementConcurrentBomb() {
        this.concurrentBomb++;
    }

    /**
     * Increases the player's movement speed by one unit, up to the maximum speed.
     */
    private void incrementSpeed() {
        this.speed++;
    }

    /**
     * Checks if the player is shielded from damage.
     *
     * @return {@code true} if the player is shielded, {@code false} otherwise.
     */
    public boolean getIsShielded() {
        return this.isShielded;
    }

    /**
     * Activates or deactivates the player's shield.
     *
     * @param isActivated {@code true} to activate the shield, {@code false} to deactivate it.
     * @return the current shield activation status.
     */
    public boolean setIsShieldActivated(boolean isActivated) {
        return this.isShieldActivated = isActivated;
    }

    /**
     * Retrieves the player's current speed.
     *
     * @return the movement speed of the player.
     */
    public float getSpeed() {
        return this.speed;
    }
}