package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Textures;
import static de.tum.cit.ase.bomberquest.map.CollisionCategories.CATEGORY_INDESTRUCTIBLE_WALL;
import static de.tum.cit.ase.bomberquest.map.CollisionCategories.MASK_WALL;

/**
 * The IndestructibleWall class represents a type of wall in the BomberQuest game
 * that cannot be destroyed by explosions or other interactions.
 *
 * <p>This class extends the {@link Wall} class and provides specific behaviors
 * and attributes for indestructible walls, including creating a static hitbox
 * and rendering the appropriate texture.</p>
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Defines the indestructible wall's position on the game map.</li>
 *   <li>Creates a static hitbox using the Box2D physics engine for collision handling.</li>
 *   <li>Renders the wall with a unique texture.</li>
 * </ul>
 *
 * @see Wall
 * @see CollisionCategories
 * @see Textures
 */
public class IndestructibleWall extends Wall {

    private final Body hitbox;

    /**
     * Constructs an IndestructibleWall object at the specified position in the game world.
     *
     * @param world the Box2D {@link World} in which the wall exists
     * @param x     the x-coordinate of the wall's position
     * @param y     the y-coordinate of the wall's position
     */
    public IndestructibleWall(World world, float x, float y) {
        super(x, y);
        this.hitbox = createHitbox(world, new PolygonShape(), false,
                x, y, CATEGORY_INDESTRUCTIBLE_WALL, MASK_WALL);
    }

    /**
     * Returns the current visual appearance of the indestructible wall.
     *
     * @return a {@link TextureRegion} representing the indestructible wall texture
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.INDESTRUCTIBLE_WALL;
    }

    /**
     * Returns the physical hitbox of the indestructible wall.
     * The hitbox is used for collision detection and interaction in the game world.
     *
     * @return the {@link Body} representing the hitbox of the indestructible wall
     */
    public Body getHitbox() {
        return this.hitbox;
    }
}
