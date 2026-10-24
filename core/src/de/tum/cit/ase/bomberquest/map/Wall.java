package de.tum.cit.ase.bomberquest.map;

/**
 * The Wall class represents a base class for all wall objects in the BomberQuest game.
 * Walls are stationary game objects that obstruct movement and can either be indestructible
 * or destructible, depending on their specific type.
 *
 * <p>This class extends {@link GameObject}, inheriting its basic properties such as position
 * on the game map. Specific types of walls, such as {@link IndestructibleWall} and
 * {@link DestructibleWall}, can extend this class to provide additional functionalities.</p>
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Defines the position of the wall on the game map.</li>
 *   <li>Serves as a base class for various types of walls, enabling extensibility.</li>
 * </ul>
 *
 * @see GameObject
 * @see IndestructibleWall
 * @see DestructibleWall
 */
public class Wall extends GameObject {

    public Wall(float x, float y) {
        super(x, y);
    }

}
