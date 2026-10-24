package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Textures;
import static de.tum.cit.ase.bomberquest.map.CollisionCategories.CATEGORY_DESTRUCTIBLE_WALL;
import static de.tum.cit.ase.bomberquest.map.CollisionCategories.MASK_WALL;

/**
 * The DestructibleWall class represents a type of wall in the BomberQuest game
 * that can be destroyed by explosions. It extends the {@link Wall} class and
 * provides specific attributes and behaviors for destructible walls.
 *
 * <p>Each destructible wall has a physical hitbox for collision detection and
 * is rendered with a specific texture. The hitbox is created using the Box2D
 * physics engine and adheres to the collision filtering rules defined in
 * {@link CollisionCategories}.
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Creation of a physical hitbox for the destructible wall in the game world.</li>
 *   <li>Rendering the wall using the appropriate texture.</li>
 *   <li>Interaction with other objects in the game, such as explosions and players.</li>
 * </ul>
 *
 * @see Wall
 * @see CollisionCategories
 * @see Textures
 */
public class DestructibleWall extends Wall {

    private final Body hitbox;

    /**
     * Constructs a DestructibleWall object at the specified position in the game world.
     *
     * @param world the Box2D world in which the wall exists
     * @param x the x-coordinate of the wall's position
     * @param y the y-coordinate of the wall's position
     */
    public DestructibleWall(World world, float x, float y) {
        super(x, y);
        this.hitbox = createHitbox(world, new PolygonShape(), false,
                x, y, CATEGORY_DESTRUCTIBLE_WALL, MASK_WALL);
    }

    /**
     * Returns the current visual appearance of the destructible wall.
     *
     * @return a {@link TextureRegion} representing the destructible wall texture
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.DESTRUCTIBLE_WALL;
    }

    /**
     * Returns the physical hitbox of the destructible wall.
     * The hitbox is used for collision detection and interaction in the game world.
     *
     * @return the {@link Body} representing the hitbox of the destructible wall
     */
    public Body getHitbox() {
        return this.hitbox;
    }
}
