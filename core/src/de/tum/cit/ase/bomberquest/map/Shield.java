package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * The Shield power-up grants temporary protection to the player.
 * <p>
 * When collected, it allows the player to survive bomb explosions or enemy contact
 * for a limited time.
 */
public class Shield extends PowerUp {

    /**
     * Creates a Shield power-up at the specified coordinates.
     * <p>
     * The position determines where the shield appears on the game map.
     *
     * @param x the x-coordinate of the shield power-up.
     * @param y the y-coordinate of the shield power-up.
     */
    public Shield(float x, float y) {
        super(x, y);
    }

    /**
     * Returns the texture representing the Shield power-up's appearance.
     *
     * @return a {@link TextureRegion} for the Shield power-up.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.SHIELD;
    }
}
