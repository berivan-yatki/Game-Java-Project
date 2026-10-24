package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Represents the Concurrent Bomb power-up in the Bomber Quest game.
 * Increases the number of bombs the player can place simultaneously when collected.
 *
 * @see PowerUp
 * @see Textures#CONCURRENT_BOMB
 */
public class ConcurrentBomb extends PowerUp {

    /**
     * Initializes the Concurrent Bomb power-up at the given coordinates.
     *
     * @param x the x-coordinate of the power-up.
     * @param y the y-coordinate of the power-up.
     */
    public ConcurrentBomb(float x, float y) {
        super(x, y);
    }

    /**
     * Returns the visual texture for the Concurrent Bomb power-up.
     *
     * This method overrides {@link PowerUp#getCurrentAppearance()} to specify
     * the unique appearance of the Concurrent Bomb in the game.
     *
     * @return a {@link TextureRegion} defining the Concurrent Bomb's appearance.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.CONCURRENT_BOMB;
    }
}
