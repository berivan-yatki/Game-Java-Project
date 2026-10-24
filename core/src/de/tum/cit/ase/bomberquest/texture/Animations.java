package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all animation constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Animations {

    /**
     * The animation for the character walking down.
     * <p>
     * This animation cycles through frames from the sprite sheet to depict the character's movement.
     * </p>
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_DOWN = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(1, 1),
            SpriteSheet.CHARACTER.at(1, 2),
            SpriteSheet.CHARACTER.at(1, 3),
            SpriteSheet.CHARACTER.at(1, 4)
    );

    /**
     * The animation for the character walking to the right.
     * <p>
     * This animation cycles through frames from the sprite sheet to depict the character's movement to the right.
     * </p>
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_RIGHT = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(2, 1),
            SpriteSheet.CHARACTER.at(2, 2),
            SpriteSheet.CHARACTER.at(2, 3),
            SpriteSheet.CHARACTER.at(2, 4)
    );

    /**
     * The animation for the character walking up.
     * <p>
     * This animation cycles through frames from the sprite sheet to depict the character's upward movement.
     * </p>
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_UP = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(3, 1),
            SpriteSheet.CHARACTER.at(3, 2),
            SpriteSheet.CHARACTER.at(3, 3),
            SpriteSheet.CHARACTER.at(3, 4)
    );

    /**
     * The animation for the character walking to the left.
     * <p>
     * This animation cycles through frames from the sprite sheet to depict the character's movement to the left.
     * </p>
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_LEFT = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(4, 1),
            SpriteSheet.CHARACTER.at(4, 2),
            SpriteSheet.CHARACTER.at(4, 3),
            SpriteSheet.CHARACTER.at(4, 4)
    );

    /**
     * The animation for the bomb.
     * <p>
     * This animation shows the bomb's countdown effect, indicating when it will explode.
     */
    public static final Animation<TextureRegion> BOMB_ANIMATION = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 1),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 2),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 3)
    );

    /**
     * The animation for enemies.
     * <p>
     * This animation cycles through frames to depict enemy movement or behavior on the map.
     */
    public static final Animation<TextureRegion> ENEMY_ANIMATION = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(21, 1),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(21, 2),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(21, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(21, 4),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(21, 5),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(21, 6)
    );
}

