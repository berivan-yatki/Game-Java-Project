package de.tum.cit.ase.bomberquest.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * A Heads-Up Display (HUD) that displays game information on the screen.
 * <p>
 * The HUD shows details such as the player's blast radius, bomb capacity, remaining enemies, and more.
 * It uses a dedicated {@link OrthographicCamera} to ensure the HUD is always fixed on the screen,
 * independent of the game world camera.
 */
public class Hud {

    /** The SpriteBatch used to draw the HUD. This is the same as the one used in the GameScreen. */
    private final SpriteBatch spriteBatch;
    /** The font used to draw text on the screen. */
    private final BitmapFont font;
    /** The camera used to render the HUD. */
    private final OrthographicCamera camera;
    /** The screen width at the time of initialization. */
    private final int screenWidth;
    /** The screen height at the time of initialization. */
    private final int screenHeight;

    /**
     * Constructs a new HUD instance.
     * <p>
     * Initializes the camera, font, and SpriteBatch for rendering HUD elements.
     *
     * @param spriteBatch The {@link SpriteBatch} used for rendering HUD elements.
     * @param font        The {@link BitmapFont} used for displaying text.
     */
    public Hud(SpriteBatch spriteBatch, BitmapFont font) {
        this.spriteBatch = spriteBatch;
        this.font = font;
        this.camera = new OrthographicCamera();
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        resize(screenWidth, screenHeight);
    }

    /**
     * Renders the HUD (Heads-Up Display) with game statistics and controls information.
     * <p>
     * Displays player stats such as blast radius, concurrent bombs, shield status, speed, bomb limit,
     * remaining enemies, and time left. It also shows messages for pausing, muting, and guiding the
     * player when the exit becomes visible.
     * </p>
     *
     * @param blastRadius    The current blast radius of the player's bombs.
     * @param concurrentBomb The number of bombs the player can place simultaneously.
     * @param bombLimit      The maximum number of bombs the player can use.
     * @param enemyNo        The number of remaining enemies on the map.
     * @param isExitVisible  Whether the exit is currently visible to the player.
     * @param isShielded     Whether the player is shielded.
     * @param speed          The player's current speed.
     * @param time           The remaining time in the game.
     */
    public void render(int blastRadius, int concurrentBomb, int bombLimit, int enemyNo, boolean isExitVisible,
                       boolean isShielded, float speed, float time) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        float posY = screenHeight - 10;
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, "Press \"ESC\" to Pause!", 10, posY);
        posY -= 20;
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, "Press \"M\" to Mute!", 10, posY);
        posY -= 20;
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, "Blast Radius: " + blastRadius, 10, posY);
        posY -= 20;
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, "Concurrent Bomb: " + concurrentBomb, 10, posY);
        posY -= 20;
        font.getData().setScale(0.5f);
        if (isShielded) {
            font.draw(spriteBatch, "Shielded: " + "YES" ,10, posY);
        } else {
            font.draw(spriteBatch, "Shielded: " + "NO" ,10, posY);
        }
        posY -= 20;
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, "Speed: " + speed, 10, posY);
        posY -= 20;
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, "Bomb Limit: " + bombLimit, 10, posY);
        posY -= 20;
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, "Remaining Enemies: " + enemyNo, 10, posY);
        posY -= 20;
        font.getData().setScale(0.5f);
        int min = (int) (time / 60);
        int sec = (int) (time % 60);
        String str1 = "Time: %02d:%02d";
        String str2 = String.format(str1, min, sec);
        //font.draw(spriteBatch, "Time: " + (int) time, 10, screenHeight - 150);
        font.draw(spriteBatch, str2, 10, posY);
        posY -= 30;
        font.getData().setScale(1.0f);
        font.setColor(Color.FOREST);
        if (isExitVisible) {
            font.draw(spriteBatch, "All Enemies Are Dead!\n" +
                    "Go To Exit!", 10, posY);
        }
        font.getData().setScale(1.0f);
        font.setColor(Color.BLACK);

        spriteBatch.end();
    }

    /**
     * Resizes the HUD when the screen size changes.
     * This is called when the window is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
}
