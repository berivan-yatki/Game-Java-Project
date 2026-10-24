package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Represents the Blast Radius power-up in the Bomber Quest game.
 * Increases bomb explosion radius by one tile when collected.
 *
 * @see PowerUp
 * @see Textures#BLAST_RADIUS
 */
public class BlastRadius extends PowerUp {
    /**
     * Constructs a BlastRadius power-up at the specified coordinates.
     *
     * @param x the x-coordinate of the BlastRadius power-up
     * @param y the y-coordinate of the BlastRadius power-up
     */
    public BlastRadius(float x, float y) {
        super(x, y);
    }

    /**
     * Returns the graphical appearance of the Blast Radius power-up.
     * <p>
     * This method is used by the rendering system to display the power-up on the game map.
     * It overrides the {@link PowerUp#getCurrentAppearance()} method to provide a
     * specific texture for the Blast Radius power-up.
     *
     * @return a {@link TextureRegion} representing the visual appearance of the power-up.
     * @see Textures#BLAST_RADIUS
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.BLAST_RADIUS;
    }
}
