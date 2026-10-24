package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * The Exit class represents the exit point in the BomberQuest game.
 * It is a stationary game object that serves as the destination or goal
 * for players to complete a level.
 *
 * <p>The exit is rendered using a specific texture, and its position
 * is defined by the x and y coordinates provided during initialization.
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Defines the exit's position on the game map.</li>
 *   <li>Renders the exit using the appropriate texture.</li>
 * </ul>
 *
 * @see GameObject
 * @see Textures
 */
public class Exit extends GameObject {

    public Exit(float x, float y) {
        super(x, y);
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.EXIT;
    }
}
