package de.tum.cit.ase.bomberquest.map;

/**
 * The CollisionCategories class defines collision category bits and their corresponding
 * collision masks for the BomberQuest game. These are used to specify which game objects
 * can collide with each other during the game's physics simulation.
 *
 * <p>The collision categories and masks help in controlling how different entities in the
 * game (e.g., player, walls, enemies, bombs, explosions, and power-ups) interact with one
 * another. Each category is assigned a unique bit, and masks determine the interaction rules.
 *
 * <h2>Usage</h2>
 * <p>These categories and masks are used in the game's physics engine to filter collisions
 * and ensure that interactions are handled correctly.</p>
 */
public class CollisionCategories {

    /** Collision category bits. */
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_INDESTRUCTIBLE_WALL = 0x0002;
    public static final short CATEGORY_DESTRUCTIBLE_WALL = 0x0004;
    public static final short CATEGORY_ENEMY = 0x0008;
    public static final short CATEGORY_EXPLOSION = 0x0020;
    public static final short CATEGORY_POWERUP = 0x0040;

    /** Masks (what each category collides with). */
    public static final short MASK_ENEMY = CATEGORY_INDESTRUCTIBLE_WALL | CATEGORY_DESTRUCTIBLE_WALL | CATEGORY_PLAYER | CATEGORY_EXPLOSION;
    public static final short MASK_PLAYER = CATEGORY_INDESTRUCTIBLE_WALL | CATEGORY_DESTRUCTIBLE_WALL | CATEGORY_ENEMY | CATEGORY_EXPLOSION | CATEGORY_POWERUP;
    public static final short MASK_WALL = CATEGORY_PLAYER | CATEGORY_ENEMY | CATEGORY_EXPLOSION;

}
