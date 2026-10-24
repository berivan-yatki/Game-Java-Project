package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {
    public static final TextureRegion CHARACTER = SpriteSheet.CHARACTER.at(1,1);

    public static final TextureRegion BLAST_RADIUS = SpriteSheet.ORIGINAL_BOMBERMAN.at(15,2);

    public static final TextureRegion CONCURRENT_BOMB = SpriteSheet.ORIGINAL_BOMBERMAN.at(15,1);

    public static final TextureRegion ENTRANCE = SpriteSheet.BASIC_TILES.at(2,2);

    public static final TextureRegion DESTRUCTIBLE_WALL = SpriteSheet.ORIGINAL_BOMBERMAN.at(4,5);

    public static final TextureRegion INDESTRUCTIBLE_WALL = SpriteSheet.ORIGINAL_BOMBERMAN.at(4,4);

    public static final TextureRegion EXIT = SpriteSheet.ORIGINAL_BOMBERMAN.at(4,12);

    public static final TextureRegion FLOOR = SpriteSheet.BASIC_TILES.at(2,5);

    public static final TextureRegion EXPLOSION = SpriteSheet.ORIGINAL_BOMBERMAN.at(12,3);

    public static final TextureRegion BOMB = SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 1);

    public static final TextureRegion SHIELD = SpriteSheet.ORIGINAL_BOMBERMAN.at(15,7);

    public static final TextureRegion SPEED = SpriteSheet.ORIGINAL_BOMBERMAN.at(15,4);
}
