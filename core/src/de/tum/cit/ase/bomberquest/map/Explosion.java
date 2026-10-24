package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an explosion in the Bomber Quest game.
 * <p>
 * Explosions are triggered by bombs and interact with game elements, such as
 * players, enemies, and destructible walls. They have a limited duration and
 * end automatically after a set time.
 <p>
 * Key Features:
 * <ul>
 *   <li>Triggered by bombs.</li>
 *   <li>Affects players and enemies within its blast radius.</li>
 *   <li>Ends after a predefined duration.</li>
 * </ul>
 */
public class Explosion extends GameObject {

    private static final float DURATION = 0.5f;
    private float elapsedTime = 0f;
    private final GameMap map;
    private boolean isEnded;
    private World world;

    /**
     * Creates an Explosion instance at the specified location within the game map.
     *
     * @param world the physics world the explosion belongs to.
     * @param map the game map where the explosion occurs.
     * @param x the x-coordinate of the explosion.
     * @param y the y-coordinate of the explosion.
     */
    public Explosion(World world, GameMap map, float x, float y) {
        super(x, y);
        this.map = map;
        this.isEnded = false;
        this.map.addExplosion(this);
        this.world = world;
    }

    /**
     * Updates the state of the explosion.
     * <p>
     * Checks for interactions with players and enemies, and determines if
     * the explosion's duration has ended.
     *
     * @param frameTime the time elapsed since the last frame.
     */
    public void tick(float frameTime) {
        isEnded = false;
        elapsedTime += frameTime;

        /** Check if the explosion affects the player. */
        if (Math.floor(map.getPlayer().getX() + 0.5f) == getX() &&
                Math.floor(map.getPlayer().getY() + 0.5f) == getY()) {
            if (map.getPlayer().getIsShielded()) {
                map.getPlayer().setIsShieldActivated(true);
            } else {
                map.endGame();
            }
        }

        /** Check if the explosion affects enemies. */
        List<Enemy> enemyList = map.getEnemy();
        for (Iterator<Enemy> iterator = enemyList.iterator(); iterator.hasNext();) {
            Enemy enemy = iterator.next();
            if (Math.floor(enemy.getX() + 0.5f) == getX() && Math.floor(enemy.getY() + 0.5f) == getY()) {
                world.destroyBody(enemy.getHitbox());
                map.removeEnemy(enemy);
                iterator.remove();
                break;
            }
        }

        /** Mark the explosion as ended if its duration has elapsed. */
        if (elapsedTime >= DURATION && !getIsEnded()) {
            isEnded = true;
        }
    }

    /**
     * Returns the current texture of the explosion.
     * <p>
     * This method provides the graphical representation of the explosion
     * to be rendered on the game map.
     *
     * @return a {@link TextureRegion} representing the explosion texture.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.EXPLOSION;
    }

    /**
     * Checks whether the explosion has ended.
     * <p>
     * An explosion ends when its duration has elapsed, and it ceases to
     * interact with the game world.
     *
     * @return true if the explosion has ended, false otherwise.
     */
    public boolean getIsEnded() {
        return this.isEnded;
    }
}
