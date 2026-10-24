package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.physics.box2d.*;

/**
 * The GameMapContactListener class implements the {@link ContactListener} interface
 * to handle collision events in the BomberQuest game. It defines behaviors for different
 * collision scenarios between game objects, such as players, walls, and enemies.
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Handles player collisions with walls and adjusts their movement.</li>
 *   <li>Handles player collisions with enemies and ends the game when a collision occurs.</li>
 *   <li>Provides hooks for additional collision events via the {@code preSolve} and {@code postSolve} methods.</li>
 * </ul>
 *
 * <h2>Collision Handling</h2>
 * <p>This class processes collision events based on the following scenarios:</p>
 * <ul>
 *   <li><strong>Player and Wall Collisions:</strong> Adds linear damping to the player's hitbox to reduce friction when colliding with walls.</li>
 *   <li><strong>Player and Enemy Collisions:</strong> Ends the game if the player collides with an enemy.</li>
 * </ul>
 *
 * @see ContactListener
 * @see CollisionCategories
 * @see GameObject
 */
public class GameMapContactListener implements ContactListener {

    /**
     * Called when two fixtures begin to touch.
     * Handles collisions between players, walls, and enemies.
     *
     * @param contact the {@link Contact} object representing the collision
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        GameObject objectA = (GameObject) fixtureA.getBody().getUserData();
        GameObject objectB = (GameObject) fixtureB.getBody().getUserData();

        /** Check for Player and Wall collisions */
        if ((fixtureA.getFilterData().categoryBits == CollisionCategories.CATEGORY_PLAYER &&
                (fixtureB.getFilterData().categoryBits == CollisionCategories.CATEGORY_INDESTRUCTIBLE_WALL
                        || fixtureB.getFilterData().categoryBits == CollisionCategories.CATEGORY_DESTRUCTIBLE_WALL)) ||
                (fixtureB.getFilterData().categoryBits == CollisionCategories.CATEGORY_PLAYER &&
                        (fixtureA.getFilterData().categoryBits == CollisionCategories.CATEGORY_INDESTRUCTIBLE_WALL
                                || fixtureA.getFilterData().categoryBits == CollisionCategories.CATEGORY_DESTRUCTIBLE_WALL))) {
            Player player = objectA instanceof Player ? (Player) objectA : (Player) objectB;
            player.getHitbox().setLinearDamping(10f);
        }

        /** Check for Player and Enemy collisions */
        if ((fixtureA.getFilterData().categoryBits == CollisionCategories.CATEGORY_PLAYER &&
                fixtureB.getFilterData().categoryBits == CollisionCategories.CATEGORY_ENEMY) ||
                (fixtureB.getFilterData().categoryBits == CollisionCategories.CATEGORY_PLAYER &&
                        fixtureA.getFilterData().categoryBits == CollisionCategories.CATEGORY_ENEMY)) {
            Player player = objectA instanceof Player ? (Player) objectA : (Player) objectB;
            if (player.getIsShielded()) {
                player.setIsShieldActivated(true);
            } else {
                player.getMap().endGame();
            }
        }

        /** Handles the collision between an enemy and a wall in the game. */
        if ((objectA instanceof Enemy && isWall(objectB)) ||
                (objectB instanceof Enemy && isWall(objectA))) {
            Enemy enemy = objectA instanceof Enemy ? (Enemy) objectA : (Enemy) objectB;
            enemy.getHitbox().setLinearDamping(10f);
            enemy.chooseRandomDirection();
        }
    }

    /**
     * Handles the end of a collision between two game objects.
     * <p>
     * This method is invoked when two fixtures stop colliding. It performs specific actions
     * depending on the type of objects involved in the collision:
     * <ul>
     *     <li>If the collision involved a {@link Player}, the linear damping of the player's hitbox
     *     is reset to 0 to restore normal movement.</li>
     *     <li>If the collision involved an {@link Enemy}, the linear damping of the enemy's hitbox
     *     is also reset to 0 to allow normal movement.</li>
     * </ul>
     *
     * @param contact the {@link Contact} object representing the collision event.
     * */
    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        GameObject objectA = (GameObject) fixtureA.getBody().getUserData();
        GameObject objectB = (GameObject) fixtureB.getBody().getUserData();

        if (objectA instanceof Player || objectB instanceof Player) {
            Player player = objectA instanceof Player ? (Player) objectA : (Player) objectB;
            player.getHitbox().setLinearDamping(0f);
        }

        if (objectA instanceof Enemy || objectB instanceof Enemy) {
            Enemy enemy = objectA instanceof Enemy ? (Enemy) objectA : (Enemy) objectB;
            enemy.getHitbox().setLinearDamping(0f);
        }
    }

    /**
     * Checks if a given game object is a wall.
     * <p>
     * This method determines if a {@link GameObject} is either an {@link IndestructibleWall}
     * or a {@link DestructibleWall}.
     *
     * @param object the {@link GameObject} to check.
     * @return {@code true} if the object is a wall, {@code false} otherwise.
     */
    private boolean isWall(GameObject object) {
        return object instanceof IndestructibleWall || object instanceof DestructibleWall;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
