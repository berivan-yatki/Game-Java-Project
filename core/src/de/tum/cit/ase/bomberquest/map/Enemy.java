package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Animations;
import static de.tum.cit.ase.bomberquest.map.CollisionCategories.CATEGORY_ENEMY;
import static de.tum.cit.ase.bomberquest.map.CollisionCategories.MASK_ENEMY;

/**
 * Represents an enemy character in the Bomber Quest game.
 * <p>
 * Enemies move randomly within the game map and can collide with other objects.
 * They have a hitbox for collision detection and use animations for visual representation.
 */
public class Enemy extends GameObject {

    private final World world;
    private final Body hitbox;
    private final GameMap map;
    private float speed = 1.0f;
    private int currentDirection = -1;
    private float elapsedTime = 1.0f;

    /**
     * Creates an Enemy instance at the specified coordinates within the game map.
     *
     * @param world the physics world the enemy belongs to.
     * @param x the x-coordinate of the enemy's starting position.
     * @param y the y-coordinate of the enemy's starting position.
     * @param map the game map the enemy navigates.
     */
    public Enemy(World world, float x, float y, GameMap map) {
        super(x, y);
        this.hitbox = createHitbox(world, new CircleShape(), true,
                x, y, CATEGORY_ENEMY, MASK_ENEMY);
        this.world = world;
        this.map = map;

        chooseRandomDirection();
    }

    /**
     * Chooses a random direction for the enemy to move.
     *
     * Directions are represented as integers:
     * 0 = up, 1 = down, 2 = left, 3 = right.
     */
    public void chooseRandomDirection() {
        currentDirection = MathUtils.random(0, 3);
    }

    /**
     * Updates the enemy's movement direction and velocity based on the current direction.
     */
    private void updateDirection() {

        float xVelocity = 0;
        float yVelocity = 0;

        switch (currentDirection) {
            case 0 -> yVelocity += speed;
            case 1 -> yVelocity -= speed;
            case 2 -> xVelocity -= speed;
            case 3 -> xVelocity += speed;
        }

        hitbox.setLinearVelocity(xVelocity, yVelocity);
    }

    /**
     * Updates the enemy's state and movement.
     *
     * @param deltaTime the time elapsed since the last frame.
     */
    public void tick(float deltaTime) {
        elapsedTime -= deltaTime;
        if (elapsedTime <= 0.0f) {
            elapsedTime = 1.0f;
            chooseRandomDirection();
        }
        updateDirection();
    }

    /**
     * Returns the current appearance of the enemy based on its animation state.
     *
     * @return a {@link TextureRegion} representing the enemy's animation frame.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Animations.ENEMY_ANIMATION.getKeyFrame(this.elapsedTime, true);
    }

    /**
     * Retrieves the enemy's hitbox for collision detection.
     *
     * @return the {@link Body} representing the enemy's hitbox.
     */
    public Body getHitbox() {
        return this.hitbox;
    }

    /**
     * Gets the enemy's current x-coordinate.
     *
     * @return the x-coordinate of the enemy's position.
     */
    @Override
    public float getX() {
        return hitbox.getPosition().x;
    }

    /**
     * Gets the enemy's current y-coordinate.
     *
     * @return the y-coordinate of the enemy's position.
     */
    @Override
    public float getY() {
        return hitbox.getPosition().y;
    }
}
