package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * The Speed power-up increases the player's movement speed.
 * <p>
 * When collected, it enhances the player's ability to navigate the map more quickly.
 */
public class Speed extends PowerUp {

    /**
     * Creates a Speed power-up at the specified coordinates.
     * <p>
     * The position determines where the speed boost appears on the game map.
     *
     * @param x the x-coordinate of the speed power-up.
     * @param y the y-coordinate of the speed power-up.
     */
    public Speed(float x, float y) {
        super(x, y);
    }

    /**
     * Returns the texture representing the Speed power-up's appearance.
     *
     * @return a {@link TextureRegion} for the Speed power-up.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.SPEED;
    }
}
