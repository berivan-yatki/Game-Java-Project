package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * The GameObject class serves as a base class for all in-game objects in the BomberQuest game.
 * It implements the {@link Drawable} interface, providing a blueprint for objects that
 * have a position and appearance.
 *
 * <p>This class defines common attributes and methods, such as position and hitbox creation,
 * that can be shared among various game objects. Subclasses can override the
 * {@code getCurrentAppearance()} method to provide their specific visual representation.</p>
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Defines the x and y coordinates for the object's position.</li>
 *   <li>Implements the {@link Drawable} interface for rendering game objects.</li>
 *   <li>Provides a utility method to create physical hitboxes for objects using the Box2D physics engine.</li>
 * </ul>
 *
 * @see Drawable
 * @see com.badlogic.gdx.physics.box2d.Body
 */
public class GameObject implements Drawable {

    private final float x;
    private final float y;

    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a physical hitbox for the game object using the Box2D physics engine.
     *
     * @param world    the {@link World} instance in which the hitbox will exist
     * @param shape    the {@link Shape} of the hitbox (e.g., {@link CircleShape} or {@link PolygonShape})
     * @param isDynamic whether the hitbox should be dynamic (true) or static (false)
     * @param x        the x-coordinate of the hitbox's position
     * @param y        the y-coordinate of the hitbox's position
     * @param catBits  the collision category bits for the hitbox
     * @param maskBits the collision mask bits for the hitbox
     * @return the created {@link Body} representing the hitbox
     */
    protected Body createHitbox(World world, Shape shape, boolean isDynamic,
                                float x, float y, short catBits, short maskBits) {
        BodyDef bodyDef = new BodyDef();
        if (isDynamic) {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }
        else {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }

        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);

        if (shape instanceof CircleShape) {
            shape.setRadius(0.45f);
        } else if (shape instanceof PolygonShape) {
            ((PolygonShape) shape).setAsBox(0.5f, 0.5f);
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.1f;
        fixtureDef.filter.categoryBits = catBits;
        fixtureDef.filter.maskBits = maskBits;

        body.createFixture(fixtureDef);
        shape.dispose();
        body.setUserData(this);
        return body;
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        return null;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
