package de.tum.cit.ase.bomberquest.map;

/**
 * Represents a power-up in the Bomber Quest game.
 * <p>
 * Power-ups are collectible items that provide the player with enhanced abilities,
 * such as increasing the bomb blast radius or the number of bombs that can be placed simultaneously.
 */
public class PowerUp extends GameObject {

    /**
     * Creates a PowerUp instance at the specified coordinates.
     * <p>
     * The position determines where the power-up appears on the game map.
     *
     * @param x the x-coordinate of the power-up.
     * @param y the y-coordinate of the power-up.
     */
    public PowerUp(float x, float y) {
        super(x, y);
    }
}

