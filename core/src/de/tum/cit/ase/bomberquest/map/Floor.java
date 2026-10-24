package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * The Floor class represents a floor tile in the BomberQuest game.
 * It is a stationary game object that serves as the traversable surface
 * for players and other entities.
 *
 * <p>The floor is rendered using a specific texture, and its position
 * is defined by the x and y coordinates provided during initialization.
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Defines the floor tile's position on the game map.</li>
 *   <li>Renders the floor tile using the appropriate texture.</li>
 * </ul>
 *
 * @see GameObject
 * @see Textures
 */
public class Floor extends GameObject {

    public Floor(float x, float y) {
        super(x, y);
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.FLOOR;
    }
}
