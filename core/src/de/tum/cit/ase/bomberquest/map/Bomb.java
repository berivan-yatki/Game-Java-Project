package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.audio.SoundEffect;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * The Bomb class represents a bomb object in the BomberQuest game.
 * A bomb has a fuse time, blast radius, and interacts with the game map
 * to create explosions that affect destructible and indestructible walls,
 * as well as the player.
 *
 * <p>Features of this class include:
 * <ul>
 *   <li>Handling the bomb's countdown fuse timer.</li>
 *   <li>Triggering explosions in specified directions based on the blast radius.</li>
 *   <li>Interacting with the game map to destroy walls and end the game if the player is hit.</li>
 *   <li>Providing the current visual appearance of the bomb.</li>
 * </ul>
 *
 * @see GameMap
 * @see Explosion
 * @see Animations
 */
public class Bomb extends GameObject {

    private World world;
    private final GameMap map;
    private float fuseTime;
    private final int blastRadius;
    private boolean isExploded = false;

    /**
     * Constructs a Bomb object with the specified position, map, fuse time, and blast radius.
     *
     * @param x the x-coordinate of the bomb's position
     * @param y the y-coordinate of the bomb's position
     * @param map the game map the bomb interacts with
     * @param fuseTime the duration before the bomb explodes
     * @param blastRadius the radius of the bomb's explosion
     */
    public Bomb(World world, float x, float y, GameMap map, float fuseTime, int blastRadius) {
        super(x, y);
        this.world = world;
        this.map = map;
        this.fuseTime = fuseTime;
        this.blastRadius = blastRadius;
    }

    /**
     * Decreases the bomb's fuse time and triggers the explosion if the fuse reaches zero.
     *
     * @param deltaTime the time elapsed since the last tick
     */
    public void tick(float deltaTime) {
        fuseTime -= deltaTime;
        if (fuseTime < 0) {
            fuseTime = 0;
        }
        if (fuseTime <= 0 && !getIsExploded()) {
            explode();
        }
    }

    /**
     * Creates an explosion in a specified direction based on the blast radius.
     * The explosion interacts with destructible and indestructible walls and the player.
     *
     * @param dx the x-direction of the explosion
     * @param dy the y-direction of the explosion
     */
    private void createExplosion(int dx, int dy) {
        for (int i = 1; i <= this.blastRadius; i++) {
            int x = (int) getX() + dx * i;
            int y = (int) getY() + dy * i;

            if (map.isIndestructibleWall(x, y)) break;

            Explosion explosion = new Explosion(this.world, this.map, x, y);

            if (map.isDestructibleWall(x, y)) {
                map.destroyWall(x, y);
                break;
            }
        }
    }

    /**
     * Triggers the bomb's explosion if it has not already exploded.
     * The explosion affects all directions based on the blast radius.
     */
    private void explode() {
        if (isExploded) {
            return;
        }
        isExploded = true;

        createExplosion(0, 0);
        createExplosion(0, 1);
        createExplosion(0, -1);
        createExplosion(1, 0);
        createExplosion(-1, 0);

        SoundEffect.BOMB_EXPLOSION.play();
        map.getPlayer().incrementBombNo();
    }

    /**
     * Returns the current visual appearance of the bomb based on its fuse time.
     *
     * @return a {@link TextureRegion} representing the bomb's texture
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        if (fuseTime < 0) {
            System.err.println("Invalid fuseTime: " + fuseTime);
            return Textures.BOMB;
        }
        return Animations.BOMB_ANIMATION.getKeyFrame(fuseTime, true);
    }

    /**
     * Returns whether the bomb has exploded.
     *
     * @return {@code true} if the bomb has exploded, otherwise {@code false}
     */
    public boolean getIsExploded() {
        return this.isExploded;
    }
}
