package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * The Entrance class represents the entrance point in the BomberQuest game.
 * It serves as a stationary game object that players can identify visually on the map.
 *
 * <p>The entrance is rendered using a specific texture, and its position
 * is defined by the x and y coordinates passed during its initialization.
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Defines the entrance's position on the game map.</li>
 *   <li>Renders the entrance using the appropriate texture.</li>
 * </ul>
 *
 * @see GameObject
 * @see Textures
 */
public class Entrance extends GameObject {

    public Entrance(float x, float y) {
        super(x, y);
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.ENTRANCE;
    }
}
